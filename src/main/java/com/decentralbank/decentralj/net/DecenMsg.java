package com.decentralbank.decentralj.net;

/*  =========================================================================
DecenMsg.java

Generated codec class for DecenMsg
*/

/*  These are the zre_msg messages
HELLO - Greet a peer so it can connect back to us
    sequence      number 2
    ipaddress     string
    port       number 2
    pools        strings
    status        number 1
    headers       dictionary
ALIVE - Send a message to a peer
    sequence      number 2
    content       frame
BROADCAST - Send a message to a pool
    sequence      number 2
    pool         string
    content       frame
JOIN - Join a pool
    sequence      number 2
    pool         string
    status        number 1
LEAVE - Leave a pool
    sequence      number 2
    pool         string
    status        number 1
PING - Ping a peer that has gone silent
    sequence      number 2
DECENACK - Reply to a peer's ping
    sequence      number 2
*/

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.ByteBuffer;

import com.decentralbank.decentralj.core.Request;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

//Opaque class structure
public class DecenMsg
{
public static final int DECEN_MSG_VERSION     = 1;

public static final int HELLO                 = 1;
public static final int ALIVE                 = 2;
public static final int BROADCAST             = 3;
public static final int JOIN                  = 4;
public static final int LEAVE                 = 5;
public static final int PING                  = 6;
public static final int DECENACK              = 7;

//  Structure of our class
private ZFrame address;             //  Address of peer if any
private int id;                     //  DecenMsg message ID
private ByteBuffer needle;          //  Read/write pointer for serialization
private int sequence;
private String ipaddress;
private int port;
private List <String> pools;
private int status;
private Map <String, String> headers;
private int headersBytes;
private ZFrame content;
private String pool;


//  --------------------------------------------------------------------------
//  Create a new DecenMsg

public DecenMsg(int id)
{
    this.id = id;
}


//  --------------------------------------------------------------------------
//  Destroy the zre_msg

public void destroy ()
{
    //  Free class properties
    if (address != null)
        address.destroy ();
    address = null;

    //  Destroy frame fields
    if (content != null)
        content.destroy ();
    content = null;
}


//  --------------------------------------------------------------------------
//  Network data encoding macros


//  Put a 1-byte number to the frame
private final void putNumber1 (int value) 
{
    needle.put ((byte) value);
}

//  Get a 1-byte number to the frame
//  then make it unsigned
private int getNumber1 () 
{ 
    int value = needle.get (); 
    if (value < 0)
        value = (0xff) & value;
    return value;
}

//  Put a 2-byte number to the frame
private final void putNumber2 (int value) 
{
    needle.putShort ((short) value);
}

//  Get a 2-byte number to the frame
private int getNumber2 () 
{ 
    int value = needle.getShort (); 
    if (value < 0)
        value = (0xffff) & value;
    return value;
}

//  Put a 4-byte number to the frame
private final void putNumber4 (long value) 
{
    needle.putInt ((int) value);
}

//  Get a 4-byte number to the frame
//  then make it unsigned
private long getNumber4 () 
{ 
    long value = needle.getInt (); 
    if (value < 0)
        value = (0xffffffff) & value;
    return value;
}

//  Put a 8-byte number to the frame
public void putNumber8 (long value) 
{
    needle.putLong (value);
}

//  Get a 8-byte number to the frame
public long getNumber8 () 
{
    return needle.getLong ();
}


//  Put a block to the frame
private void putBlock (byte [] value, int size) 
{
    needle.put (value, 0, size);
}

private byte [] getBlock (int size) 
{
    byte [] value = new byte [size]; 
    needle.get (value);

    return value;
}

//  Put a string to the frame
public void putString (String value) 
{
    needle.put ((byte) value.length ());
    needle.put (value.getBytes());
}

//  Get a string from the frame
public String getString () 
{
    int size = getNumber1 ();
    byte [] value = new byte [size];
    needle.get (value);

    return new String (value);
}

//  --------------------------------------------------------------------------
//  Receive and parse a DecenMsg from the socket. Returns new object or
//  null if error. Will block if there's no message waiting.

public static DecenMsg recv(Socket input)
{
    assert (input != null);
    DecenMsg self = new DecenMsg(0);
    ZFrame frame = null;

    try {
        //  Read valid message frame from socket; we loop over any
        //  garbage data we might receive from badly-connected peers
        while (true) {
            //  If we're reading from a ROUTER socket, get address
            if (input.getType () == ZMQ.ROUTER) {
                self.address = ZFrame.recvFrame (input);
                if (self.address == null)
                    return null;         //  Interrupted
                if (!input.hasReceiveMore ())
                    throw new IllegalArgumentException ();
            }
            //  Read and parse command in frame
            frame = ZFrame.recvFrame (input);
            if (frame == null)
                return null;             //  Interrupted

            //  Get and check protocol signature
            self.needle = ByteBuffer.wrap (frame.getData ()); 
            int signature = self.getNumber2 ();
            if (signature == (0xAAA0 | 1))
                break;                  //  Valid signature

            //  Protocol assertion, drop message
            while (input.hasReceiveMore ()) {
                frame.destroy ();
                frame = ZFrame.recvFrame (input);
            }
            frame.destroy ();
        }

        //  Get message id, which is first byte in frame
        self.id = self.getNumber1 ();
        int listSize;
        int hashSize;

        switch (self.id) {
        case HELLO:
            self.sequence = self.getNumber2 ();
            self.ipaddress = self.getString ();
            self.port = self.getNumber2 ();
            listSize = self.getNumber1 ();
            self.pools = new ArrayList<String> ();
            while (listSize-- > 0) {
                String string = self.getString ();
                self.pools.add (string);
            }
            self.status = self.getNumber1 ();
            hashSize = self.getNumber1 ();
            self.headers = new HashMap <String, String> ();
            while (hashSize-- > 0) {
                String string = self.getString ();
                String [] kv = string.split("=");
                self.headers.put(kv[0], kv[1]);
            }

            break;

        case ALIVE:
            self.sequence = self.getNumber2 ();
            //  Get next frame, leave current untouched
            if (!input.hasReceiveMore ())
                throw new IllegalArgumentException ();
            self
                    .content = ZFrame.recvFrame (input);
            break;

        case BROADCAST:
            self.sequence = self.getNumber2 ();
            self.pool = self.getString ();
            //  Get next frame, leave current untouched
            if (!input.hasReceiveMore ())
                throw new IllegalArgumentException ();
            self.content = ZFrame.recvFrame (input);
            break;

        case JOIN:
            self.sequence = self.getNumber2 ();
            self.pool = self.getString ();
            self.status = self.getNumber1 ();
            break;

        case LEAVE:
            self.sequence = self.getNumber2 ();
            self.pool = self.getString ();
            self.status = self.getNumber1 ();
            break;

        case PING:
            self.sequence = self.getNumber2 ();
            break;

        case DECENACK:
            self.sequence = self.getNumber2 ();
            break;

        default:
            throw new IllegalArgumentException ();
        }

        return self;

    } catch (Exception e) {
        //  Error returns
        System.out.printf ("E: malformed message '%d'\n", self.id);
        self.destroy ();
        return null;
    } finally {
        if (frame != null)
            frame.destroy ();
    }
}


//  Count size of key=value pair
private static void 
headersCount (final Map.Entry <String, String> entry, DecenMsg self)
{
    self.headersBytes += entry.getKey ().length () + 1 + entry.getValue ().length () + 1;
}

//  Serialize headers key=value pair
private static void
headersWrite (final Map.Entry <String, String> entry, DecenMsg self)
{
    String string = entry.getKey () + "=" + entry.getValue ();
    self.putString (string);
}


//  --------------------------------------------------------------------------
//  Send the DecenMsg to the socket, and destroy it

public boolean send(Socket port2)
{
    assert (port2 != null);

    //  Calculate size of serialized data
    int frameSize = 2 + 1;          //  Signature and message ID
    switch (id) {
    case HELLO:
        //  sequence is a 2-byte integer
        frameSize += 2;
        //  ipaddress is a string with 1-byte length
        frameSize++;       //  Size is one octet
        if (ipaddress != null)
            frameSize += ipaddress.length ();
        //  port is a 2-byte integer
        frameSize += 2;
        //  pools is an array of strings
        frameSize++;       //  Size is one octet
        if (pools != null) {
            for (String value : pools) 
                frameSize += 1 + value.length ();
        }
        //  status is a 1-byte integer
        frameSize += 1;
        //  headers is an array of key=value strings
        frameSize++;       //  Size is one octet
        if (headers != null) {
            headersBytes = 0;
            for (Map.Entry <String, String> entry: headers.entrySet ()) {
                headersCount (entry, this);
            }
            frameSize += headersBytes;
        }
        break;
        
    case ALIVE:
        //  sequence is a 2-byte integer
        frameSize += 2;
        break;
        
    case BROADCAST:
        //  sequence is a 2-byte integer
        frameSize += 2;
        //  pool is a string with 1-byte length
        frameSize++;       //  Size is one octet
        if (pool != null)
            frameSize += pool.length ();
        break;
        
    case JOIN:
        //  sequence is a 2-byte integer
        frameSize += 2;
        //  pool is a string with 1-byte length
        frameSize++;       //  Size is one octet
        if (pool != null)
            frameSize += pool.length ();
        //  status is a 1-byte integer
        frameSize += 1;
        break;
        
    case LEAVE:
        //  sequence is a 2-byte integer
        frameSize += 2;
        //  pool is a string with 1-byte length
        frameSize++;       //  Size is one octet
        if (pool != null)
            frameSize += pool.length ();
        //  status is a 1-byte integer
        frameSize += 1;
        break;
        
    case PING:
        //  sequence is a 2-byte integer
        frameSize += 2;
        break;
        
    case DECENACK:
        //  sequence is a 2-byte integer
        frameSize += 2;
        break;
        
    default:
        System.out.printf ("E: bad message type '%d', not sent\n", id);
        assert (false);
    }
    //  Now serialize message into the frame
    ZFrame frame = new ZFrame (new byte [frameSize]);
    needle = ByteBuffer.wrap (frame.getData ()); 
    int frameFlags = 0;
    putNumber2 (0xAAA0 | 1);
    putNumber1 ((byte) id);

    switch (id) {
    case HELLO:
        putNumber2 (sequence);
        if (ipaddress != null)
            putString (ipaddress);
        else
            putNumber1 ((byte) 0);      //  Empty string
        putNumber2 (port);
        if (pools != null) {
            putNumber1 ((byte) pools.size ());
            for (String value : pools) {
                putString (value);
            }
        }
        else
            putNumber1 ((byte) 0);      //  Empty string array
        putNumber1 (status);
        if (headers != null) {
            putNumber1 ((byte) headers.size ());
            for (Map.Entry <String, String> entry: headers.entrySet ()) {
                headersWrite (entry, this);
            }
        }
        else
            putNumber1 ((byte) 0);      //  Empty dictionary
        break;
        
    case ALIVE:
        putNumber2 (sequence);
        frameFlags = ZMQ.SNDMORE;
        break;
        
    case BROADCAST:
        putNumber2 (sequence);
        if (pool != null)
            putString (pool);
        else
            putNumber1 ((byte) 0);      //  Empty string
        frameFlags = ZMQ.SNDMORE;
        break;
        
    case JOIN:
        putNumber2 (sequence);
        if (pool != null)
            putString (pool);
        else
            putNumber1 ((byte) 0);      //  Empty string
        putNumber1 (status);
        break;
        
    case LEAVE:
        putNumber2 (sequence);
        if (pool != null)
            putString (pool);
        else
            putNumber1 ((byte) 0);      //  Empty string
        putNumber1 (status);
        break;
        
    case PING:
        putNumber2 (sequence);
        break;
        
    case DECENACK:
        putNumber2 (sequence);
        break;
        
    }
    //  If we're sending to a ROUTER, we send the address first
    if (port2.getType () == ZMQ.ROUTER) {
        assert (address != null);
        if (!address.send (port2, ZMQ.SNDMORE)) {
            destroy ();
            return false;
        }
    }
    //  Now send the data frame
  /*  if (!frame.sendAndDestroy (socket, frameFlags)) {
        frame.destroy ();
        destroy ();
        return false;
    }
    */
    //  Now send any frame fields, in order
    switch (id) {
    case ALIVE:
        //  If content isn't set, send an empty frame
        if (content == null)
            content = new ZFrame ("".getBytes ());
        if (!content.send (port2, 0)) {
            frame.destroy ();
            destroy ();
            return false;
        }
        break;
    case BROADCAST:
        //  If content isn't set, send an empty frame
        if (content == null)
            content = new ZFrame ("".getBytes ());
        if (!content.send (port2, 0)) {
            frame.destroy ();
            destroy ();
            return false;
        }
        break;
    }
    //  Destroy DecenMsg object
    destroy ();
    return true;
}


//--------------------------------------------------------------------------
//Send the HELLO to the socket in one step

public static void sendHello (
    Socket output,
    int sequence,
    String ipaddress,
    int port,
    Collection <String> pools,
    int status,
    Map <String, String> headers) 
{
    DecenMsg self = new DecenMsg(DecenMsg.HELLO);
    self.setSequence (sequence);
    self.setIpaddress (ipaddress);
    self.setport (port);
    self.setpools (new ArrayList <String> (pools));
    self.setStatus (status);
   // self.setHeaders (new HashMap <String, String> (headers));
    self.send (output); 
}

//--------------------------------------------------------------------------
//Send the ALIVE to the socket in one step

public static void sendALIVE (
    Socket output,
    int sequence,
    ZFrame content) 
{
    DecenMsg self = new DecenMsg(DecenMsg.ALIVE);
    self.setSequence (sequence);
    self.setContent (content.duplicate ());
    self.send (output); 
}

//--------------------------------------------------------------------------
//Send the BROADCAST to the socket in one step

public static void sendBROADCAST (
    Socket output,
    int sequence,
    String pool,
    ZFrame content) 
{
    DecenMsg self = new DecenMsg(DecenMsg.BROADCAST);
    self.setSequence (sequence);
    self.setpool (pool);
    self.setContent (content.duplicate ());
    self.send (output); 
}

//--------------------------------------------------------------------------
//Send the JOIN to the socket in one step

public static void sendJoin (
    Socket output,
    int sequence,
    String pool,
    int status) 
{
    DecenMsg self = new DecenMsg(DecenMsg.JOIN);
    self.setSequence (sequence);
    self.setpool (pool);
    self.setStatus (status);
    self.send (output); 
}

//--------------------------------------------------------------------------
//Send the LEAVE to the socket in one step

public static void sendLeave (
    Socket output,
    int sequence,
    String pool,
    int status) 
{
    DecenMsg self = new DecenMsg(DecenMsg.LEAVE);
    self.setSequence (sequence);
    self.setpool (pool);
    self.setStatus (status);
    self.send (output); 
}

//--------------------------------------------------------------------------
//Send the PING to the socket in one step

public static void sendPing (
    Socket output,
    int sequence) 
{
    DecenMsg self = new DecenMsg(DecenMsg.PING);
    self.setSequence (sequence);
    self.send (output); 
}

//--------------------------------------------------------------------------
//Send the DECENACK to the socket in one step

public static void sendDECENACK (
    Socket output,
    int sequence) 
{
    DecenMsg self = new DecenMsg(DecenMsg.DECENACK);
    self.setSequence (sequence);
    self.send (output); 
}


//  --------------------------------------------------------------------------
//  Duplicate the DecenMsg message

public DecenMsg dup ()
{
    DecenMsg copy = new DecenMsg(this.id);
    if (this.address != null)
        copy.address = this.address.duplicate ();
    switch (this.id) {
    case HELLO:
        copy.sequence = this.sequence;
        copy.ipaddress = this.ipaddress;
        copy.port = this.port;
        copy.pools = new ArrayList <String> (this.pools);
        copy.status = this.status;
        copy.headers = new HashMap <String, String> (this.headers);
    break;
    case ALIVE:
        copy.sequence = this.sequence;
        copy.content = this.content.duplicate ();
    break;
    case BROADCAST:
        copy.sequence = this.sequence;
        copy.pool = this.pool;
        copy.content = this.content.duplicate ();
    break;
    case JOIN:
        copy.sequence = this.sequence;
        copy.pool = this.pool;
        copy.status = this.status;
    break;
    case LEAVE:
        copy.sequence = this.sequence;
        copy.pool = this.pool;
        copy.status = this.status;
    break;
    case PING:
        copy.sequence = this.sequence;
    break;
    case DECENACK:
        copy.sequence = this.sequence;
    break;
    }
    return copy;
}

//  Dump headers key=value pair to stdout
public static void headersDump (Map.Entry <String, String> entry, DecenMsg self)
{
    System.out.printf ("        %s=%s\n", entry.getKey (), entry.getValue ());
}


//  --------------------------------------------------------------------------
//  Print contents of message to stdout

public void dump ()
{
    switch (id) {
    case HELLO:
        System.out.println ("HELLO:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        if (ipaddress != null)
            System.out.printf ("    ipaddress='%s'\n", ipaddress);
        else
            System.out.printf ("    ipaddress=\n");
        System.out.printf ("    port=%d\n", (long)port);
        System.out.printf ("    pools={");
        if (pools != null) {
            for (String value : pools) {
                System.out.printf (" '%s'", value);
            }
        }
        System.out.printf (" }\n");
        System.out.printf ("    status=%d\n", (long)status);
        System.out.printf ("    headers={\n");
        if (headers != null) {
            for (Map.Entry <String, String> entry : headers.entrySet ())
                headersDump (entry, this);
        }
        System.out.printf ("    }\n");
        break;
        
    case ALIVE:
        System.out.println ("ALIVE:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        System.out.printf ("    content={\n");
        if (content != null) {
            int size = content.size ();
            byte [] data = content.getData ();
            System.out.printf ("        size=%d\n", content.size ());
            if (size > 32)
                size = 32;
            int contentIndex;
            for (contentIndex = 0; contentIndex < size; contentIndex++) {
                if (contentIndex != 0 && (contentIndex % 4 == 0))
                    System.out.printf ("-");
                System.out.printf ("%02X", data [contentIndex]);
            }
        }
        System.out.printf ("    }\n");
        break;
        
    case BROADCAST:
        System.out.println ("BROADCAST:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        if (pool != null)
            System.out.printf ("    pool='%s'\n", pool);
        else
            System.out.printf ("    pool=\n");
        System.out.printf ("    content={\n");
        if (content != null) {
            int size = content.size ();
            byte [] data = content.getData ();
            System.out.printf ("        size=%d\n", content.size ());
            if (size > 32)
                size = 32;
            int contentIndex;
            for (contentIndex = 0; contentIndex < size; contentIndex++) {
                if (contentIndex != 0 && (contentIndex % 4 == 0))
                    System.out.printf ("-");
                System.out.printf ("%02X", data [contentIndex]);
            }
        }
        System.out.printf ("    }\n");
        break;
        
    case JOIN:
        System.out.println ("JOIN:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        if (pool != null)
            System.out.printf ("    pool='%s'\n", pool);
        else
            System.out.printf ("    pool=\n");
        System.out.printf ("    status=%d\n", (long)status);
        break;
        
    case LEAVE:
        System.out.println ("LEAVE:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        if (pool != null)
            System.out.printf ("    pool='%s'\n", pool);
        else
            System.out.printf ("    pool=\n");
        System.out.printf ("    status=%d\n", (long)status);
        break;
        
    case PING:
        System.out.println ("PING:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        break;
        
    case DECENACK:
        System.out.println ("DECENACK:");
        System.out.printf ("    sequence=%d\n", (long)sequence);
        break;
        
    }
}


//  --------------------------------------------------------------------------
//  Get/set the message address

public ZFrame address ()
{
    return address;
}

public void setAddress (ZFrame address)
{
    if (this.address != null)
        this.address.destroy ();
    this.address = address.duplicate ();
}


//  --------------------------------------------------------------------------
//  Get/set the zre_msg id

public int id ()
{
    return id;
}

public void setId (int id)
{
    this.id = id;
}

//  --------------------------------------------------------------------------
//  Get/set the sequence field

public int sequence ()
{
    return sequence;
}

public void setSequence (int sequence)
{
    this.sequence = sequence;
}


//  --------------------------------------------------------------------------
//  Get/set the ipaddress field

public String ipaddress ()
{
    return ipaddress;
}

public void setIpaddress (String format, Object ... args)
{
    //  Format into newly allocated string
    ipaddress = String.format (format, args);
}


//  --------------------------------------------------------------------------
//  Get/set the port field

public int port ()
{
    return port;
}

public void setport (int port)
{
    this.port = port;
}


//  --------------------------------------------------------------------------
//  Iterate through the pools field, and append a pools value

public List <String> pools ()
{
    return pools;
}

public void appendpools (String format, Object ... args)
{
    //  Format into newly allocated string
    
    String string = String.format (format, args);
    //  Attach string to list
    if (pools == null)
        pools = new ArrayList <String> ();
    pools.add (string);
}

public void setpools (Collection <String> value)
{
    pools = new ArrayList (value); 
}


//  --------------------------------------------------------------------------
//  Get/set the status field

public int status ()
{
    return status;
}

public void setStatus (int status)
{
    this.status = status;
}


//  --------------------------------------------------------------------------
//  Get/set a value in the headers dictionary

public Map <String, String> headers ()
{
    return headers;
}

public String headersString (String key, String defaultValue)
{
    String value = null;
    if (headers != null)
        value = headers.get (key);
    if (value == null)
        value = defaultValue;

    return value;
}

public long headersNumber (String key, long defaultValue)
{
    long value = defaultValue;
    String string = null;
    if (headers != null)
        string = headers.get (key);
    if (string != null)
        value = Long.valueOf (string);

    return value;
}

public void insertHeaders (String key, String format, Object ... args)
{
    //  Format string into buffer
    String string = String.format (format, args);

    //  Store string in hash table
    if (headers == null)
        headers = new HashMap <String, String> ();
    headers.put (key, string);
    headersBytes += key.length () + 1 + string.length ();
}

public void setHeaders (HashMap<String, Request> value)
{
    //if (value != null)
       // headers = new HashMap <String, String> (value);
   // else
       // headers = value;
}


//  --------------------------------------------------------------------------
//  Get/set the content field

public ZFrame content ()
{
    return content;
}

//  Takes ownership of supplied frame
public void setContent (ZFrame frame)
{
    if (content != null)
        content.destroy ();
    content = frame;
}

//  --------------------------------------------------------------------------
//  Get/set the pool field

public String pool ()
{
    return pool;
}

public void setpool (String format, Object ... args)
{
    //  Format into newly allocated string
    pool = String.format (format, args);
}




}
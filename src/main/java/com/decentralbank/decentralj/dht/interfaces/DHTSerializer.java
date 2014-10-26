package com.decentralbank.decentralj.dht.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A Serializer is used to transform data to and from a specified form.
 */
public interface DHTSerializer<T>
{

    /**
     * Write DHT Content to a DataOutput stream
     */
    public void write(T data, DataOutputStream out) throws IOException;

    /**
     * Read data of type T from a DataInput Stream
     */
    public T read(DataInputStream in) throws IOException, ClassNotFoundException;
}

#TCP Protocol Grammar

Decen-protocol    = greeting *traffic

				greeting        = S:HELLO

				traffic         = S:ALIVE

                / S:BROADCAST

                / S:JOIN

                / S:LEAVE

                / S:PING R:PING-OK

                ###   Greet a peer so it can connect back to us

                S:HELLO         = signature %x01 sequence ipaddress mailbox groups status headers

                signature       = %xAA %xA1

                sequence        = 2OCTET        ; Incremental sequence number

                ipaddress       = string        ; Sender IP address

                string          = size *VCHAR   ; Information about a node

                size            = OCTET

                port            = 2OCTET        ; Sender port number

                pools          = strings       ; List of pools sender is in

                strings         = size *string

                status          = OCTET         ; Sender group status sequence

                headers         = dictionary    ; Sender header properties

                key-value       = string        ; Formatted as name=value

                ### Send a message to a peer

                S:ALIVE       = signature %x02 sequence content

                content         = FRAME         ; Message content as 0MQ frame

                ### Send a message to a pool

                S:BROADCAST         = signature %x03 sequence group content

                pool           = string        ; Name of pool

                content         = FRAME         ; Message content as 0MQ frame

                ### Join a pool

                S:JOIN          = signature %x04 sequence pool status

                status          = OCTET         ; Sender group status sequence

                ### Leave a group

                S:LEAVE         = signature %x05 sequence group status

                ### Ping a peer that has gone silent

                S:PING          = signature %06 sequence

                ### Reply to a peer's ping request
                S:DECENACK      = signature %07 sequence



#DECEN Commands

##The HELLO Command

Each node SHALL start a dialog by sending HELLO as the first command on an connection to a peer.

The HELLO command contains these fields:

    ipaddress - IP address that the sender will accept connections on.

    port - port number of the sender's.

    pool - the list of pools that the sender is present in, as a list of strings.

    status - the sender's group status sequence.

If the recipient has not already connected to this peer it SHALL create a ZeroMQ DEALER socket and connect it to the endpoint specified as "tcp://ipaddress:port".

The "pool status sequence" is a one-octet number that is incremented each time the peer joins or leaves a pool. Each peer MAY use this to assert the accuracy of its own pool management information.

##The ALIVE Command

When a node wishes to let a peer know it's alive it SHALL use the ALIVE command. The ALIVE command contains a single field, which is the message content defined as one 0MQ frame.

##The BROADCAST Command

When a node wishes to send a message to a set of nodes participating in a pool it SHALL use the BROADCAST command. The BROADCAST command contains two fields: the name of the pool, and the the message content defined as one 0MQ frame.

Note that messages are sent via ZeroMQ over TCP, so the BROADCAST command is unicast to each peer that should receive it.

FIND OUT HOW TO DO UDP or RUDP Multicast functionality.

##The JOIN Command

When a node joins a group it SHALL broadcast a JOIN command to all its peers. The JOIN command has two fields: the name of the group to join, and the group status sequence number after joining the group. Group names are case sensitive.

##The LEAVE Command

When a node leaves a group it SHALL broadcast a LEAVE command to all its peers. The LEAVE command has two fields: the name of the group to leave, and the group status sequence number after leaving the group.

##The PING Command

A node SHOULD send a PING command to any peer that it has not received a UDP beacon from within a certain time (typically five seconds). Note that UDP traffic may be dropped on a network that is heavily saturated. If a node receives no reply to a PING command, and no other traffic from a peer within a somewhat longer time (typically 30 seconds), it SHOULD treat that peer as dead.

Note that PING commands SHOULD be used only in targeted cases where a peer is otherwise silent. Otherwise, the cost of PING commands will rise exponentially with the number of peers connected, and can degrade network performance.

##The DECENACK Command

A node sends DECENACK as a reply to a PING command
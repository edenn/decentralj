Command-line arguments
======================

Give Decentralj (or Decentraljd) the -? or –-help argument and it will print out a list of the most commonly used command-line arguments.

##Usage:

 decentralj [command-line options]                     

###Options:

 -?                     This help message

 -help                  This help message

 -start                 Starts Decentral sever listens for peers

 -exit                  Shutdowns Decentral Server

 -conf=<file>           Specify configuration file (default: bitcoin.conf)

 -datadir=<directory>   Specify data directory

 -testnet               Use the test network

 -pid=<file>            Specify pid file (TODO)

 -gen                   Generate Deposit Multisig Addresses (default: null)

 -dbcache=<n>           Set database cache size in megabytes (4 to 4096, default: 100)

 -timeout=<n>           Specify connection timeout in milliseconds (default: 5000)

 -dns                   Allow DNS lookups for -addnode, -seednode and -connect

 -port=<port>           Listen for connections on <port> (default: 8333 or testnet: 18333)

 -maxconnections=<n>    Maintain at most <n> connections to peers (default: 125)

 -addnode=<ip>          Add a node to connect to and attempt to keep the connection open

 -connect=<ip>          Connect only to the specified node(s)

 -seednode=<ip>         Connect to a node to retrieve peer addresses, and disconnect

 -listen                Accept connections from outside

 -bind=<addr>           Bind to given address and always listen on it. Use [host]:port notation for IPv6

 -dnsseed               Find peers using DNS lookup (default: 1 unless -connect)

 -banscore=<n>          Threshold for disconnecting misbehaving peers (default: 100)

 -bantime=<n>           Number of seconds to keep misbehaving peers from reconnecting (default: 86400)

 ###Server options:
 
 -server                Accept command line and JSON-RPC commands

 -rpcuser=<user>        Username for JSON-RPC connections

 -rpcpassword=<pw>      Password for JSON-RPC connections

 -rpcport=<port>        Listen for JSON-RPC connections on <port> (default: 8332 or testnet: 18332)

 -rpcallowip=<ip>       Allow JSON-RPC connections from specified IP address

 -rpcthreads=<n>        Set the number of threads to service RPC calls (default: 4)

 -poolmembers=<n>       Set pool members size to <n> (default: 11)

 -checkmembers			Pings all pool members to check availability status

 -check =<address>		Check multisig address Balance



###Wallet options:

 -disablewallet         Do not load the wallet and disable wallet RPC calls

 -deposit               Generate Deposit Guarantee Addresses for Node

 -transactions			List History of Transactions

 -paytxfee=<amt>        Fee per kB to add to transactions you send

 -rescan                Rescan the block chain for missing wallet transactions

 -clearTransactions     Clear list of wallet transactions (diagnostic tool; implies -rescan)

 -wallet=<file>         Specify wallet file (within data directory)
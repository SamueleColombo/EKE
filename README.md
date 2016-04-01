EKE
====

This program implements a Java master/slave chat and use the Encrypted Key 
Exchange (<a href="https://en.wikipedia.org/wiki/Encrypted_key_exchange">EKE</a>)
algorithm to authenticate the users. 


Requirements
-----
This program is a NetBeans (8.1) project written in Java 8 and requires those libraries:
* SQLite Driver
* Java Cryptography Extension (<a href="https://en.wikipedia.org/wiki/Java_Cryptography_Extension">JCE</a>)
    * <a href="http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html">Java 7</a>
    * <a href="http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html">Java 7</a>
    * <a href="http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html">Java 8</a>

Server
----
The command below starts the EKE server.
```
java -cp EKE.jar server.Server <port> 
```
The command below is used to create a new account.
```
java -cp EKE.jar account.AccountManager <username> <password>
```

Client
----
The command below starts the EKE client.
```
java -cp EKE.jar client.Client <host> <port> <username> <password>
```









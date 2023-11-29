#Overview

## Program description


## Code description
This project is a simple client-server program that simulates an online market.
It uses socket communication to share data between a server and a single client, so it doesn't implement multithread.

At first you have to run the server class called MarketServer (server package), than you have to execute the class Main (client package).
There's a login 

The program structure is simple:
- The server opens the predefined port (5001) and listens to connections. The connection is opened when the client sends the connection request.
  The server can handle the following requests from the client:
    1) Login request: check if credentials send are on the database (credentials.txt)
     

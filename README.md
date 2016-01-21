# ClientHostServer
A basic 3 part system consisting of a server, a host and a client. The client sends 11 requests to an intermediate host (either a read request, a write request or an invalid request), through ports, who passes it to the server. The server then responds with to the corresponsing read or write request to the host, who passes it to the client.

To run, open the project in Eclipse. Start by running either the Host.java or the Server.java. This order does not matter. Next, run the Client.java. 11 loops of the program will then execute. The client will terminate and the Host and Server will not.

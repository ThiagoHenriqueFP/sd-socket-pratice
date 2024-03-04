# Multi thread client/server

This project is a practical college activity to create a client-server application with multiple threads using only java sockets. This activity needs to develop two topologies (ring and star) and send unicast and broadcast messages.

## Ring
This topology is a P2P approach, a server is also a client to communicate with the next server.

For this implementation, the clients are already known and the message will pass through each node until it reaches the correct receiver

## Star
This topology is a centralized one, one server must serve and differ many clients.
# How to run
## Ring
Remember: each server is also a client.

Run inside `ring.main` package: s1, s2, s3, and s4 main classes.

each client will try to connect to next server, if fails, in 5 seconds a new try will be executed.

each client can send unicast or broadcast messages.

## Star
Run inside `star.main` package: S1 to start the server and C1, C2 and C3 to start the clients.

if a client disconnect, the socket will be closed on server and removed to list of active sockets.
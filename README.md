# README - How to Compile and Run 

 

## Application Overview 

This is a simple text-based multi-client chat application built in Java 

using socket programming. The application demonstrates client-server 

communication, multithreading, and file I/O concepts covered in Unit 7. 

 

## Files Included 

  ChatServer.java   - Server-side implementation 

  ChatClient.java   - Client-side implementation 

 

## Requirements 

  - Java JDK 8 or higher 

  - Command Prompt / Terminal 

 

## Compilation 

  Open terminal in the project folder and run: 

    javac ChatServer.java 

    javac ChatClient.java 

 

## Running the Application 

  Step 1: Start the server (in Terminal 1): 

    java ChatServer 

  You should see: === Chat Server Started on Port 5000 === 

 

  Step 2: Start a client (in Terminal 2): 

    java ChatClient 

 

  Step 3: Start another client (in Terminal 3): 

    java ChatClient 

 

  Now type messages in either client window. 

  Messages are broadcast to ALL connected clients. 

  Type /quit to disconnect a client. 

 

## Key Implementation Details 

  - Each client is handled in its own thread (ClientHandler) 

  - Unique User IDs are assigned automatically (User1, User2, ...) 

  - Messages are broadcast using a synchronized list of clients 

  - BufferedReader / PrintWriter used for efficient stream I/O 

  - try-with-resources ensures proper cleanup of all streams 

  - Daemon listener thread handles incoming messages without blocking input 
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 5000;

    private static List<ClientHandler> clients =
        Collections.synchronizedList(new ArrayList<>());

    private static int userIdCounter = 1;

    public static void main(String[] args) {
        System.out.println("=== Chat Server Started on Port " + PORT + " ===");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int userId = userIdCounter++;
                System.out.println("[Server] User" + userId + " connected from "
                    + clientSocket.getInetAddress().getHostAddress());

                ClientHandler handler = new ClientHandler(clientSocket, userId);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("[Server] Error: " + e.getMessage());
        }
    }

    /**
     * @param message
     * @param sender 
     */
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * @param handler
     */
    public static void removeClient(ClientHandler handler) {
        clients.remove(handler);
        System.out.println("[Server] User" + handler.getUserId() + " disconnected.");
    }
}

class ClientHandler implements Runnable {

    private Socket socket;
    private int userId;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, int userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public int getUserId() { return userId; }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            out.flush();
        }
    }

    @Override
    public void run() {
        try {
            // Set up I/O streams
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Welcome the new client
            sendMessage("Welcome to the Chat! You are User" + userId);
            ChatServer.broadcast("[Server] User" + userId + " has joined the chat.", this);

            String message;
            // Listen for incoming messages
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/quit")) {
                    break; // Client wants to leave
                }
                String formatted = "[User" + userId + "]: " + message;
                System.out.println(formatted);
                ChatServer.broadcast(formatted, this);
            }
        } catch (IOException e) {
            System.err.println("[Server] Connection error for User" + userId
                + ": " + e.getMessage());
        } finally {
            ChatServer.removeClient(this);
            ChatServer.broadcast("[Server] User" + userId + " has left the chat.", this);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int    SERVER_PORT    = 5000;

    public static void main(String[] args) {
        System.out.println("         Welcome to CS1103 Chat Application     ");
        System.out.println("Connecting to server at "
            + SERVER_ADDRESS + ":" + SERVER_PORT + " ...");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter    out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in  = new BufferedReader(
                 new InputStreamReader(socket.getInputStream()));
             Scanner scanner    = new Scanner(System.in)) {

            System.out.println("Connected! Type messages below.");
            System.out.println("Type /quit to exit.");
            System.out.println("------------------------------------------------");

            Thread listener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("[Disconnected from server]");
                }
            });
            listener.setDaemon(true); // Stops when main thread exits
            listener.start();

            while (true) {
                System.out.print("> "); // Input prompt
                String input = scanner.nextLine();

                if (input == null || input.equalsIgnoreCase("/quit")) {
                    out.println("/quit");
                    System.out.println("Goodbye!");
                    break;
                }

                if (!input.trim().isEmpty()) {
                    out.println(input);
                }
            }

        } catch (IOException e) {
            System.err.println("[Client] Could not connect to server: " + e.getMessage());
        }
    }
}

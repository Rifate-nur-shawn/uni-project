package com.iamshawn.uniproject;

import javafx.application.Platform;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Manages the chat server for admin-customer communication
 * This is a singleton server that handles multiple client connections
 */
public class ChatServerManager {
    private static ChatServerManager instance;
    private ServerSocket serverSocket;
    private boolean running = false;
    private Thread serverThread;
    private final int PORT = 8888;
    private Map<String, ClientHandler> clients = new HashMap<>();
    private BiConsumer<Socket, String> onNewClientCallback;
    private Consumer<String> onMessageReceived;
    private AdminChatController adminController;

    private ChatServerManager() {
        // Private constructor for singleton pattern
    }

    public static ChatServerManager getInstance() {
        if (instance == null) {
            instance = new ChatServerManager();
        }
        return instance;
    }

    /**
     * Set the admin controller reference
     */
    public void setAdminController(AdminChatController controller) {
        this.adminController = controller;
    }

    /**
     * Set callback for when a new client connects
     */
    public void setOnNewClientCallback(BiConsumer<Socket, String> callback) {
        this.onNewClientCallback = callback;
    }

    /**
     * Set callback for when a message is received
     */
    public void setOnMessageReceived(Consumer<String> callback) {
        this.onMessageReceived = callback;
    }

    /**
     * Send message from admin to specific client
     */
    public boolean sendToClient(String username, String message) {
        System.out.println("sendToClient called - Username: " + username + ", Message: " + message);
        ClientHandler handler = clients.get(username);
        if (handler != null) {
            System.out.println("Handler found for " + username + ", sending message...");
            handler.sendMessage("ADMIN:" + message);
            return true;
        } else {
            System.out.println("No handler found for " + username + ". Connected clients: " + clients.keySet());
            return false;
        }
    }

    /**
     * Send message to all connected clients
     */
    public void broadcastToClients(String message) {
        System.out.println("Broadcasting to " + clients.size() + " clients");
        for (ClientHandler handler : clients.values()) {
            handler.sendMessage("ADMIN:" + message);
        }
    }

    public void startServer() {
        if (running) {
            System.out.println("Chat server is already running.");
            return;
        }

        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                running = true;
                System.out.println("Chat server started on port " + PORT);

                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("New client connected: " + clientSocket.getInetAddress());

                        // Handle client connection in a separate thread
                        ClientHandler handler = new ClientHandler(clientSocket);
                        handler.start();
                    } catch (IOException e) {
                        if (running) {
                            System.err.println("Error accepting client connection: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to start chat server: " + e.getMessage());
                running = false;
            }
        });

        serverThread.setDaemon(true);
        serverThread.start();
    }

    /**
     * Inner class to handle individual client connections
     */
    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);

                    if (message.startsWith("USER:")) {
                        // Handle user identification
                        username = message.substring(5);
                        clients.put(username, this);
                        System.out.println("User identified: " + username);

                        // Notify callback about new client
                        if (onNewClientCallback != null) {
                            final String finalUsername = username;
                            Platform.runLater(() -> onNewClientCallback.accept(socket, finalUsername));
                        }
                    } else if (message.startsWith("MSG:")) {
                        // Handle message from customer
                        String userMessage = message.substring(4);
                        System.out.println("Message from " + username + ": " + userMessage);

                        // Forward to admin controller
                        if (adminController != null) {
                            final String finalMessage = userMessage;
                            final String finalUsername = username;
                            Platform.runLater(() -> adminController.receiveCustomerMessage(finalUsername, finalMessage));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Client disconnected: " + (username != null ? username : "unknown"));
            } finally {
                cleanup();
            }
        }

        public void sendMessage(String message) {
            if (writer != null) {
                writer.println(message);
            }
        }

        public String getUsername() {
            return username;
        }

        private void cleanup() {
            if (username != null) {
                clients.remove(username);
            }
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public void stopServer() {
        running = false;

        // Close all client sockets
        for (ClientHandler handler : clients.values()) {
            handler.cleanup();
        }
        clients.clear();

        // Close server socket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Chat server stopped.");
            } catch (IOException e) {
                System.err.println("Error stopping chat server: " + e.getMessage());
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getPort() {
        return PORT;
    }

    public List<String> getConnectedClients() {
        return new ArrayList<>(clients.keySet());
    }

    public ClientHandler getClientHandler(String username) {
        return clients.get(username);
    }
}

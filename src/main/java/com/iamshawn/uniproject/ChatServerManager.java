package com.iamshawn.uniproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the chat server for admin-customer communication
 */
public class ChatServerManager {
    private static ChatServerManager instance;
    private ServerSocket serverSocket;
    private boolean running = false;
    private Thread serverThread;
    private final int PORT = 8888;
    private List<Socket> clientSockets = new ArrayList<>();

    private ChatServerManager() {
        // Private constructor for singleton pattern
    }

    public static ChatServerManager getInstance() {
        if (instance == null) {
            instance = new ChatServerManager();
        }
        return instance;
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
                        clientSockets.add(clientSocket);
                        System.out.println("New client connected: " + clientSocket.getInetAddress());

                        // Handle client connection in a separate thread
                        handleClientConnection(clientSocket);
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

    private void handleClientConnection(Socket clientSocket) {
        // This method would handle individual client connections
        // For now, it's a placeholder
        Thread clientThread = new Thread(() -> {
            try {
                // Keep connection alive
                while (running && !clientSocket.isClosed()) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
    }

    public void stopServer() {
        running = false;

        // Close all client sockets
        for (Socket socket : clientSockets) {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
        clientSockets.clear();

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

    public List<Socket> getClientSockets() {
        return new ArrayList<>(clientSockets);
    }
}


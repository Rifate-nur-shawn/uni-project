package com.iamshawn.uniproject;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerManager {

    private static ChatServerManager instance;
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private final int SERVER_PORT = 5000;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Stage> activeChats = new HashMap<>();

    private ChatServerManager() {}

    public static synchronized ChatServerManager getInstance() {
        if (instance == null) instance = new ChatServerManager();
        return instance;
    }

    public void startServer() {
        if (isRunning) return;

        executorService.submit(() -> {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                isRunning = true;
                System.out.println("Chat server started on port " + SERVER_PORT);

                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    handleNewClient(clientSocket);
                }
            } catch (IOException e) {
                System.err.println("Server error: " + e.getMessage());
            }
        });
    }

    private void handleNewClient(Socket clientSocket) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_chat.fxml"));
                Parent root = loader.load();

                AdminChatController controller = loader.getController();
                controller.initializeWithClient(clientSocket);

                Stage stage = new Stage();
                String title = "e-Dispensary - Admin Chat";
                stage.setTitle(title);
                stage.setScene(new Scene(root, 1000, 700));
                stage.setResizable(false);

                String initialId = "Client-" + clientSocket.getInetAddress().getHostAddress();
                activeChats.put(initialId, stage);

                controller.setOnUserIdentified(username -> {
                    activeChats.remove(initialId);
                    activeChats.put(username, stage);
                    stage.setTitle(title + " - " + username);
                });

                stage.setOnCloseRequest(e -> {
                    controller.shutdown();
                    activeChats.values().remove(stage);
                });

                stage.show();

            } catch (IOException e) {
                System.err.println("Failed to open chat window: " + e.getMessage());
                try {
                    clientSocket.close();
                } catch (IOException ignored) {}
            }
        });
    }

    public void stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException ignored) {}

        Platform.runLater(() -> {
            for (Stage stage : activeChats.values()) stage.close();
            activeChats.clear();
        });

        executorService.shutdown();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getActiveChatsCount() {
        return activeChats.size();
    }
}

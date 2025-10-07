package com.iamshawn.uniproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox chatBox;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    @FXML
    private Label statusLabel;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Thread receiverThread;
    private boolean connected = false;
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 5000;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load navbar dynamically
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("navbar.fxml"));
            Parent navbar = loader.load();
            mainBorderPane.setTop(navbar);
        } catch (IOException e) {
            System.err.println("Navbar load failed: " + e.getMessage());
        }

        // Connect to server (optional if no server available)
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Platform.runLater(() -> {
                    statusLabel.setText("Connected to support");
                    statusLabel.setTextFill(Color.web("#27ae60"));
                    addSystemMessage("Connected to support. You can start chatting now.");
                });

                connected = true;
                writer.println("USER:" + (HelloController.getCurrentUsername() != null
                        ? HelloController.getCurrentUsername() : "Guest"));

                startMessageReceiver();

            } catch (IOException e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Connection failed");
                    statusLabel.setTextFill(Color.web("#e74c3c"));
                    addSystemMessage("Could not connect to support. Please try again later.");
                });
            }
        }).start();
    }

    private void startMessageReceiver() {
        receiverThread = new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    final String finalMessage = message;
                    Platform.runLater(() -> {
                        if (finalMessage.startsWith("ADMIN:")) {
                            addAdminMessage(finalMessage.substring(6));
                        } else {
                            addSystemMessage(finalMessage);
                        }
                    });
                }
            } catch (IOException e) {
                if (connected) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Disconnected");
                        statusLabel.setTextFill(Color.web("#e74c3c"));
                        addSystemMessage("Disconnected from support.");
                    });
                    connected = false;
                }
            }
        });
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty() || !connected) return;

        writer.println("MSG:" + message);
        addUserMessage(message);
        messageField.clear();
    }

    private void addUserMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        VBox textBox = new VBox();
        textBox.setStyle("-fx-background-color: #633364; -fx-background-radius: 10 0 10 10;");
        textBox.setPadding(new Insets(10));
        textBox.setMaxWidth(500);

        Text messageText = new Text(message);
        messageText.setFill(Color.WHITE);
        messageText.setWrappingWidth(480);

        Text timeText = new Text(LocalDateTime.now().format(timeFormatter));
        timeText.setFill(Color.LIGHTGRAY);
        timeText.setFont(Font.font("System", FontWeight.NORMAL, 10));

        textBox.getChildren().addAll(messageText, timeText);
        messageBox.getChildren().add(textBox);
        chatBox.getChildren().add(messageBox);
    }

    private void addAdminMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        VBox textBox = new VBox();
        textBox.setStyle("-fx-background-color: #449b6d; -fx-background-radius: 0 10 10 10;");
        textBox.setPadding(new Insets(10));
        textBox.setMaxWidth(500);

        Text messageText = new Text(message);
        messageText.setFill(Color.WHITE);
        messageText.setWrappingWidth(480);

        Text timeText = new Text(LocalDateTime.now().format(timeFormatter));
        timeText.setFill(Color.LIGHTGRAY);
        timeText.setFont(Font.font("System", FontWeight.NORMAL, 10));

        textBox.getChildren().addAll(messageText, timeText);
        messageBox.getChildren().add(textBox);
        chatBox.getChildren().add(messageBox);
    }

    private void addSystemMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        Label systemMessage = new Label(message);
        systemMessage.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10;");
        systemMessage.setPadding(new Insets(5, 10, 5, 10));
        systemMessage.setTextFill(Color.GRAY);

        messageBox.getChildren().add(systemMessage);
        chatBox.getChildren().add(messageBox);
    }

    public void closeConnection() {
        connected = false;
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (receiverThread != null) {
            receiverThread.interrupt();
        }
    }

    public void shutdown() {
        closeConnection();
    }
}

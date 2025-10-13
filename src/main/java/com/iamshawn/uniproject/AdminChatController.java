package com.iamshawn.uniproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Admin chat controller - manages the server-side chat interface
 * Starts the chat server and handles communication with connected customers
 */
public class AdminChatController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label userLabel;

    private boolean connected = false;
    private String currentUser = "";
    private ChatServerManager serverManager;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("AdminChatController initialized");

        // Get server manager instance
        serverManager = ChatServerManager.getInstance();

        // Register this controller with the server FIRST
        System.out.println("Registering admin controller with server manager");
        serverManager.setAdminController(this);

        // Start the chat server
        if (!serverManager.isRunning()) {
            System.out.println("Starting chat server...");
            serverManager.startServer();

            // Wait a bit for server to start
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                statusLabel.setText("Server running - Waiting for customers");
                statusLabel.setTextFill(Color.web("#27ae60"));
                addSystemMessage("Chat server started on port " + serverManager.getPort());
                addSystemMessage("Waiting for customers to connect...");
            });
        } else {
            System.out.println("Server already running, reconnecting admin controller");
            Platform.runLater(() -> {
                statusLabel.setText("Server already running - Waiting for customers");
                statusLabel.setTextFill(Color.web("#27ae60"));
                addSystemMessage("Connected to existing chat server on port " + serverManager.getPort());

                // Check if there are already connected clients
                List<String> connectedClients = serverManager.getConnectedClients();
                if (!connectedClients.isEmpty()) {
                    String firstClient = connectedClients.get(0);
                    addSystemMessage("Found existing connection: " + firstClient);
                    handleNewClient(firstClient);
                } else {
                    addSystemMessage("Waiting for customers to connect...");
                }
            });
        }

        // Set callback for new client connections (ALWAYS set this)
        System.out.println("Setting callback for new clients");
        serverManager.setOnNewClientCallback((socket, username) -> {
            System.out.println("Callback triggered for new client: " + username);
            handleNewClient(username);
        });

        // Enable send button and enter key
        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());

        System.out.println("AdminChatController initialization complete");
    }

    /**
     * Handle new client connection
     */
    private void handleNewClient(String username) {
        System.out.println("handleNewClient called for: " + username);
        this.currentUser = username;
        this.connected = true;

        Platform.runLater(() -> {
            statusLabel.setText("User connected");
            statusLabel.setTextFill(Color.web("#27ae60"));
            userLabel.setText(currentUser);
            addSystemMessage("User " + currentUser + " connected. You can now chat!");
        });
    }

    /**
     * Receive message from customer (called by ChatServerManager)
     */
    public void receiveCustomerMessage(String username, String message) {
        System.out.println("receiveCustomerMessage called - User: " + username + ", Message: " + message);

        // If this is a different user than current, switch to them
        if (!username.equals(currentUser)) {
            currentUser = username;
            userLabel.setText(currentUser);
            connected = true;
            addSystemMessage("Switched to conversation with " + currentUser);
        }

        addUserMessage(message);
        scrollToBottom();
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        System.out.println("sendMessage called - Message: '" + message + "', Connected: " + connected + ", CurrentUser: '" + currentUser + "'");

        if (message.isEmpty()) {
            return;
        }

        if (!connected || currentUser == null || currentUser.isEmpty()) {
            addSystemMessage("No customer connected. Waiting for customers to join...");
            messageField.clear();
            return;
        }

        // Send message through server manager
        boolean sent = serverManager.sendToClient(currentUser, message);
        System.out.println("Message sent status: " + sent);

        // Add message to chat UI
        addAdminMessage(message);
        scrollToBottom();

        // Clear input field
        messageField.clear();
    }

    private void addUserMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        VBox textBox = new VBox();
        textBox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 0 10 10 10;");
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
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        VBox textBox = new VBox();
        textBox.setStyle("-fx-background-color: #449b6d; -fx-background-radius: 10 0 10 10;");
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

    private void scrollToBottom() {
        Platform.runLater(() -> {
            if (scrollPane != null) {
                scrollPane.setVvalue(1.0);
            }
        });
    }

    // Method to be called when the window is closed
    public void shutdown() {
        connected = false;
        currentUser = "";
        // Note: We don't stop the server here as it should continue running
        // for other potential connections
    }
}

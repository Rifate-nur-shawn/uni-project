package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    // FXML UI Elements - Login Form
    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane login_form;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginbtn;

    @FXML
    private Button createAccountBtn;

    // FXML UI Elements - Signup Form
    @FXML
    private TextField signup_name;

    @FXML
    private TextField signup_username;

    @FXML
    private PasswordField signup_password;

    @FXML
    private Button signupbtn;

    @FXML
    private Button loginAccountBtn;

    // Close button
    @FXML
    private Button close;

    // In-memory storage for users (for demo purposes)
    private static final Map<String, UserData> users = new HashMap<>();
    private static String currentUsername;
    private static boolean isCurrentUserAdmin;

    // Simple user data class
    static class UserData {
        String name;
        String password;
        boolean isAdmin;

        UserData(String name, String password, boolean isAdmin) {
            this.name = name;
            this.password = password;
            this.isAdmin = isAdmin;
        }
    }

    // Initialize with some default users
    static {
        users.put("admin", new UserData("Administrator", "admin123", true));
        users.put("pharmacist", new UserData("John Pharmacist", "pharma123", false));
        users.put("user", new UserData("Regular User", "user123", false));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the forms - show login form by default
        if (login_form != null) {
            login_form.setVisible(true);
        }
        if (signup_form != null) {
            signup_form.setVisible(false);
        }
    }

    @FXML
    public void switchForm(ActionEvent event) {
        if (event.getSource() == createAccountBtn) {
            // Switch to signup form
            login_form.setVisible(false);
            signup_form.setVisible(true);
        } else if (event.getSource() == loginAccountBtn) {
            // Switch to login form
            login_form.setVisible(true);
            signup_form.setVisible(false);
        }
    }

    @FXML
    private void loginAdmin(ActionEvent event) {
        Alert alert;

        // Validate input fields
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        String inputUsername = username.getText().trim();
        String inputPassword = password.getText();

        // Check if user exists and password matches
        if (users.containsKey(inputUsername)) {
            UserData userData = users.get(inputUsername);
            if (userData.password.equals(inputPassword)) {
                // Successful login
                currentUsername = inputUsername;
                isCurrentUserAdmin = userData.isAdmin;

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + userData.name + "!");
                alert.showAndWait();

                // Clear the login fields
                username.clear();
                password.clear();

                // Close current window
                ((Stage) loginbtn.getScene().getWindow()).close();

                try {
                    // Load the appropriate dashboard based on user role
                    if (isCurrentUserAdmin) {
                        // Admin user - load admin dashboard
                        loadNewScene("dashboard.fxml", "MediTrack - Admin Dashboard");
                    } else {
                        // Regular user/pharmacist - load user dashboard
                        loadNewScene("pharmacy-dashboard.fxml", "MediTrack - Pharmacy Dashboard");
                    }
                } catch (Exception e) {
                    // If dashboard FXML files don't exist, show a success message
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Navigation");
                    alert.setHeaderText(null);
                    alert.setContentText("Login successful! " + (isCurrentUserAdmin ? "Admin" : "Pharmacist") + " dashboard would load here.\n\nUser: " + userData.name);
                    alert.showAndWait();
                }
            } else {
                // Wrong password
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect password!");
                alert.showAndWait();
                password.clear(); // Clear password field
            }
        } else {
            // User doesn't exist
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Username not found!");
            alert.showAndWait();
        }
    }

    @FXML
    private void signupAdmin(ActionEvent event) {
        Alert alert;

        // Validate input fields
        if (signup_name.getText().isEmpty() || signup_username.getText().isEmpty() || signup_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        String newName = signup_name.getText().trim();
        String newUsername = signup_username.getText().trim();
        String newPassword = signup_password.getText();

        // Validate username length
        if (newUsername.length() < 3) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username");
            alert.setHeaderText(null);
            alert.setContentText("Username must be at least 3 characters long!");
            alert.showAndWait();
            return;
        }

        // Validate password length
        if (newPassword.length() < 6) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Password");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 6 characters long!");
            alert.showAndWait();
            return;
        }

        // Check if username already exists
        if (users.containsKey(newUsername)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Username Taken");
            alert.setHeaderText(null);
            alert.setContentText("Username '" + newUsername + "' already exists!\nPlease choose a different username.");
            alert.showAndWait();
        } else {
            // Add new user (default as pharmacist/regular user, not admin)
            users.put(newUsername, new UserData(newName, newPassword, false));

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully!\nYou can now login with your credentials.");
            alert.showAndWait();

            // Clear signup fields
            signup_name.clear();
            signup_username.clear();
            signup_password.clear();

            // Switch to login form
            login_form.setVisible(true);
            signup_form.setVisible(false);
        }
    }

    @FXML
    private void close(ActionEvent event) {
        // Close the application
        System.exit(0);
    }

    // Helper method to load new scenes
    private void loadNewScene(String fxmlFile, String title) throws IOException {
        URL resource = getClass().getResource(fxmlFile);
        if (resource != null) {
            Parent root = FXMLLoader.load(resource);
            Stage stage = new Stage();
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } else {
            throw new IOException("FXML file not found: " + fxmlFile);
        }
    }

    // Getter methods for other classes to access current user info
    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static boolean isCurrentUserAdmin() {
        return isCurrentUserAdmin;
    }

    public static UserData getCurrentUserData() {
        return users.get(currentUsername);
    }
}

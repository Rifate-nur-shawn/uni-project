package com.iamshawn.uniproject;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        // Try to authenticate from database first
        Connection connect = database.connectDb();
        boolean authenticatedFromDB = false;
        boolean isAdmin = false;
        String userName = "";

        if (connect != null) {
            try {
                String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
                PreparedStatement prepare = connect.prepareStatement(sql);
                prepare.setString(1, inputUsername);
                prepare.setString(2, inputPassword);
                ResultSet result = prepare.executeQuery();

                if (result.next()) {
                    // User found in database
                    authenticatedFromDB = true;
                    userName = result.getString("username");
                    String role = result.getString("role");
                    isAdmin = "admin".equalsIgnoreCase(role);
                }

                result.close();
                prepare.close();
                connect.close();
            } catch (Exception e) {
                System.err.println("Database authentication error: " + e.getMessage());
                // Fall back to in-memory authentication
            }
        }

        // Fall back to in-memory authentication if database fails
        if (!authenticatedFromDB && users.containsKey(inputUsername)) {
            UserData userData = users.get(inputUsername);
            if (userData.password.equals(inputPassword)) {
                authenticatedFromDB = true;
                userName = userData.name;
                isAdmin = userData.isAdmin;
            }
        }

        // Check authentication result
        if (authenticatedFromDB) {
            // Successful login
            currentUsername = inputUsername;
            isCurrentUserAdmin = isAdmin;

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, " + inputUsername + "!");
            alert.showAndWait();

            // Clear the login fields
            username.clear();
            password.clear();

            // Close current window
            Stage currentStage = (Stage) loginbtn.getScene().getWindow();
            currentStage.close();

            try {
                // Load the dashboard for all users
                loadAdminDashboard();
            } catch (Exception e) {
                System.err.println("Error loading dashboard: " + e.getMessage());
                e.printStackTrace();

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Navigation Error");
                alert.setHeaderText(null);
                alert.setContentText("Error loading dashboard: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Authentication failed
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password!");
            alert.showAndWait();
            password.clear();
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

        // Try to register user in database
        Connection connect = database.connectDb();
        if (connect != null) {
            try {
                // First check if username already exists
                String checkSql = "SELECT * FROM user WHERE username = ?";
                PreparedStatement checkStmt = connect.prepareStatement(checkSql);
                checkStmt.setString(1, newUsername);
                ResultSet checkResult = checkStmt.executeQuery();

                if (checkResult.next()) {
                    // Username already exists
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Username Taken");
                    alert.setHeaderText(null);
                    alert.setContentText("Username '" + newUsername + "' already exists!\nPlease choose a different username.");
                    alert.showAndWait();
                    checkResult.close();
                    checkStmt.close();
                    connect.close();
                    return;
                }
                checkResult.close();
                checkStmt.close();

                // Insert new user into database
                String insertSql = "INSERT INTO user (username, password, email, role) VALUES (?, ?, ?, 'customer')";
                PreparedStatement insertStmt = connect.prepareStatement(insertSql);
                insertStmt.setString(1, newUsername);
                insertStmt.setString(2, newPassword);
                insertStmt.setString(3, newName); // Using name as email for now

                int result = insertStmt.executeUpdate();

                if (result > 0) {
                    // Also add to in-memory storage for compatibility
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
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Registration Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to create account. Please try again.");
                    alert.showAndWait();
                }

                insertStmt.close();
                connect.close();

            } catch (Exception e) {
                System.err.println("Database registration error: " + e.getMessage());
                e.printStackTrace();

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Error connecting to database: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Database not available, fall back to in-memory storage
            if (users.containsKey(newUsername)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Username Taken");
                alert.setHeaderText(null);
                alert.setContentText("Username '" + newUsername + "' already exists!\nPlease choose a different username.");
                alert.showAndWait();
            } else {
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
    }

    @FXML
    private void close(ActionEvent event) {
        // Close the application
        System.exit(0);
    }

    // Helper method to load admin dashboard
    private void loadAdminDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("e-Dispensary - Admin Dashboard");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to load home page for regular users
    private void loadHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root, 1400, 800);
        stage.setTitle("e-Dispensary - Home");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to load new scenes (kept for compatibility)
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

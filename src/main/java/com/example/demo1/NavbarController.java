package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {

    // FXML UI Elements
    @FXML
    private AnchorPane navbar;

    @FXML
    private Button homeBtn;

    @FXML
    private Button productsBtn;

    @FXML
    private Button aboutBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signupBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button cartBtn;

    @FXML
    private Button chatBtn;

    @FXML
    private Button logoutBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the navbar based on user login status
        updateNavbarBasedOnLoginStatus();
    }

    // Navigation methods for each button
    @FXML
    private void navigateToHome(ActionEvent event) {
        try {
            loadScene("home.fxml", "e-Dispensary - Home", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Home page would load here.");
        }
    }

    @FXML
    private void navigateToProducts(ActionEvent event) {
        try {
            loadScene("products.fxml", "e-Dispensary - Products", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Products page would load here.");
        }
    }

    @FXML
    private void navigateToAbout(ActionEvent event) {
        try {
            // Debug: Print to console
            System.out.println("Attempting to load About page...");

            // Check if the FXML file exists
            URL aboutResource = getClass().getResource("about.fxml");
            if (aboutResource == null) {
                throw new IOException("about.fxml file not found in resources");
            }

            System.out.println("About.fxml found, loading...");
            FXMLLoader loader = new FXMLLoader(aboutResource);
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle("e-Dispensary - About");
            stage.setScene(scene);
            stage.show();

            System.out.println("About page loaded successfully!");

        } catch (Exception e) {
            System.err.println("Error loading About page: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Navigation Error", "Could not load About page. Error: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            loadScene("hello-view.fxml", "e-Dispensary - Login", event);
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load login page.");
        }
    }

    @FXML
    private void navigateToSignup(ActionEvent event) {
        try {
            // Load the login page but switch to signup form
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 639, 561);
            stage.setScene(scene);
            stage.setTitle("e-Dispensary - Sign Up");
            stage.show();
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load signup page.");
        }
    }

    @FXML
    private void navigateToDashboard(ActionEvent event) {
        try {
            // Check if user is admin or regular user
            if (HelloController.isCurrentUserAdmin()) {
                loadScene("admin-dashboard.fxml", "e-Dispensary - Admin Dashboard", event);
            } else {
                loadScene("user-dashboard.fxml", "e-Dispensary - User Dashboard", event);
            }
        } catch (Exception e) {
            String userType = HelloController.isCurrentUserAdmin() ? "Admin" : "User";
            showInfoAlert("Navigation", userType + " dashboard would load here.");
        }
    }

    @FXML
    private void navigateToCart(ActionEvent event) {
        try {
            loadScene("cart.fxml", "e-Dispensary - Shopping Cart", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Shopping cart would load here.");
        }
    }

    @FXML
    private void navigateToChat(ActionEvent event) {
        try {
            loadScene("chat.fxml", "e-Dispensary - Chat", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Chat feature would load here.");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Clear user session data
            // Note: This would typically clear any stored user session

            // Show logout confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Logout");
            alert.setHeaderText(null);
            alert.setContentText("You have been logged out successfully!");
            alert.showAndWait();

            // Navigate back to login page
            loadScene("hello-view.fxml", "e-Dispensary - Login", event);

        } catch (Exception e) {
            showErrorAlert("Logout Error", "Could not complete logout process.");
        }
    }

    // Helper method to load new scenes
    private void loadScene(String fxmlFile, String title, ActionEvent event) throws IOException {
        URL resource = getClass().getResource(fxmlFile);
        if (resource != null) {
            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } else {
            throw new IOException("FXML file not found: " + fxmlFile);
        }
    }

    // Helper method to show information alerts
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show error alerts
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to update navbar visibility based on login status
    private void updateNavbarBasedOnLoginStatus() {
        // Check if user is logged in (you can integrate this with your authentication system)
        boolean isLoggedIn = HelloController.getCurrentUsername() != null;

        if (isLoggedIn) {
            // Hide login/signup buttons, show user-specific buttons
            loginBtn.setVisible(false);
            signupBtn.setVisible(false);
            dashboardBtn.setVisible(true);
            cartBtn.setVisible(true);
            chatBtn.setVisible(true);
            logoutBtn.setVisible(true);
        } else {
            // Show login/signup buttons, hide user-specific buttons
            loginBtn.setVisible(true);
            signupBtn.setVisible(true);
            dashboardBtn.setVisible(false);
            cartBtn.setVisible(false);
            chatBtn.setVisible(false);
            logoutBtn.setVisible(false);
        }
    }

    // Public method to refresh navbar when login status changes
    public void refreshNavbar() {
        updateNavbarBasedOnLoginStatus();
    }

    // Method to highlight the current page button
    public void setActiveButton(String buttonName) {
        // Reset all buttons to default style
        resetButtonStyles();

        // Apply active style to the specified button
        String activeStyle = "-fx-background-color: rgba(255,255,255,0.3); -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;";

        switch (buttonName.toLowerCase()) {
            case "home":
                homeBtn.setStyle(activeStyle);
                break;
            case "products":
                productsBtn.setStyle(activeStyle);
                break;
            case "about":
                aboutBtn.setStyle(activeStyle);
                break;
            case "dashboard":
                dashboardBtn.setStyle(activeStyle);
                break;
            case "cart":
                cartBtn.setStyle(activeStyle);
                break;
            case "chat":
                chatBtn.setStyle(activeStyle);
                break;
        }
    }

    // Helper method to reset all button styles to default
    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;";

        homeBtn.setStyle(defaultStyle);
        productsBtn.setStyle(defaultStyle);
        aboutBtn.setStyle(defaultStyle);

        if (dashboardBtn.isVisible()) {
            dashboardBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-cursor: hand; -fx-border-radius: 5; -fx-border-color: white; -fx-border-width: 1; -fx-background-radius: 5;");
        }
        if (cartBtn.isVisible()) {
            cartBtn.setStyle(defaultStyle);
        }
        if (chatBtn.isVisible()) {
            chatBtn.setStyle(defaultStyle);
        }
    }
}

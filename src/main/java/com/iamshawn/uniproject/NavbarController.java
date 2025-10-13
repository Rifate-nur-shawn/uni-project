package com.iamshawn.uniproject;

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
    private Button cartBtn;

    @FXML
    private Button chatBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Update navbar based on login status
        updateNavbarBasedOnLoginStatus();
    }

    // Navigation methods for each button
    @FXML
    private void navigateToHome(ActionEvent event) {
        try {
            loadScene("home.fxml", "e-Dispensary - Home", event);
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load home page.");
        }
    }

    @FXML
    private void navigateToProducts(ActionEvent event) {
        try {
            loadScene("products.fxml", "e-Dispensary - Products", event);
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load products page.");
        }
    }

    @FXML
    private void navigateToAbout(ActionEvent event) {
        try {
            loadScene("about.fxml", "e-Dispensary - About", event);
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load about page.");
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
    private void navigateToCart(ActionEvent event) {
        try {
            loadScene("cart.fxml", "e-Dispensary - Shopping Cart", event);
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load cart page.");
        }
    }

    @FXML
    private void navigateToChat(ActionEvent event) {
        try {
            loadScene("chat.fxml", "e-Dispensary - Chat", event);
        } catch (Exception e) {
            showErrorAlert("Navigation Error", "Could not load chat page.");
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
            chatBtn.setVisible(true);
            cartBtn.setVisible(true);
        } else {
            // Show login/signup buttons, hide user-specific buttons
            loginBtn.setVisible(true);
            signupBtn.setVisible(true);
            chatBtn.setVisible(false);
            cartBtn.setVisible(false);
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
        }
    }

    // Helper method to reset all button styles to default
    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;";

        homeBtn.setStyle(defaultStyle);
        productsBtn.setStyle(defaultStyle);
        aboutBtn.setStyle(defaultStyle);
    }
}

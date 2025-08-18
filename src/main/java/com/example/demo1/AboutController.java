package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    // FXML UI Elements
    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load navbar at the top
        loadNavbar();
    }

    private void loadNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("navbar.fxml"));
            Parent navbar = loader.load();

            // Get the navbar controller and set active button
            NavbarController navbarController = loader.getController();
            if (navbarController != null) {
                navbarController.setActiveButton("about");
            }

            // Add navbar to the top of BorderPane
            mainBorderPane.setTop(navbar);

        } catch (IOException e) {
            System.err.println("Could not load navbar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Navigation methods for footer links
    @FXML
    private void navigateToHome(ActionEvent event) {
        try {
            loadScene("home.fxml", "MediTrack - Home", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Home page would load here.\nThis would show the main pharmacy homepage with featured products and services.");
        }
    }

    @FXML
    private void navigateToProducts(ActionEvent event) {
        try {
            loadScene("products.fxml", "MediTrack - Products", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Products page would load here.\nThis would show the full catalog of medicines and health products.");
        }
    }

    @FXML
    private void navigateToAbout(ActionEvent event) {
        // Already on about page; do nothing to avoid unnecessary navigation
        System.out.println("Already on About page");
    }

    // Helper method to load new scenes
    private void loadScene(String fxmlFile, String title, ActionEvent event) throws IOException {
        URL resource = getClass().getResource(fxmlFile);
        if (resource != null) {
            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            Scene scene = new Scene(root, 1440, 800);
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

    // Method to refresh page content (useful for when returning from other pages)
    public void refreshContent() {
        // About page is static, no content to refresh
        System.out.println("About page refreshed");
    }

    // Method to handle window closing
    public void handleClose() {
        System.out.println("About page closing");
    }
}

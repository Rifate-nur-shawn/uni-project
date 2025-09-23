package com.iamshawn.uniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    // FXML UI Elements
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ImageView bannerImage;

    @FXML
    private GridPane medicinesContainer;

    @FXML
    private Button viewAllBtn;

    // Sample medicine data for display
    private static class Medicine {
        String name;
        String description;
        String price;
        String imagePath;

        Medicine(String name, String description, String price, String imagePath) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imagePath = imagePath;
        }
    }

    // Updated medicines data for e-Dispensary
    private final Medicine[] sampleMedicines = {
        new Medicine("Amoxicillin 500mg", "Antibiotic for bacterial infections", "$18.99", "/images/m.png"),
        new Medicine("Omega-3 Fish Oil", "Heart health and brain function supplement", "$24.99", "/images/addmed.png"),
        new Medicine("Lisinopril 10mg", "ACE inhibitor for blood pressure control", "$12.50", "/images/purchaseMedi.png"),
        new Medicine("Metformin 850mg", "Diabetes management medication", "$15.75", "/images/m.png"),
        new Medicine("Vitamin D3 2000IU", "Bone health and immune system support", "$19.99", "/images/addmed.png"),
        new Medicine("Atorvastatin 20mg", "Cholesterol-lowering statin medication", "$22.25", "/images/purchaseMedi.png")
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load navbar at the top
        loadNavbar();

        // Load sample medicines into the grid
        loadLatestProducts();

        // Set up banner image
        setupBannerImage();
    }

    private void loadNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("navbar.fxml"));
            Parent navbar = loader.load();

            // Get the navbar controller and set active button
            NavbarController navbarController = loader.getController();
            if (navbarController != null) {
                navbarController.setActiveButton("home");
            }

            // Add navbar to the top of BorderPane
            mainBorderPane.setTop(navbar);

        } catch (IOException e) {
            System.err.println("Could not load navbar: " + e.getMessage());
        }
    }

    private void setupBannerImage() {
        try {
            // You can set a different banner image if needed
            // bannerImage is already set in FXML, this method is for any additional setup
            bannerImage.setFitHeight(200);
            bannerImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Could not setup banner image: " + e.getMessage());
        }
    }

    private void loadLatestProducts() {
        try {
            // Clear existing content
            medicinesContainer.getChildren().clear();

            // Add up to 6 medicines in a 3x2 grid
            int row = 0;
            int col = 0;
            int maxItems = Math.min(6, sampleMedicines.length);

            for (int i = 0; i < maxItems; i++) {
                Medicine medicine = sampleMedicines[i];
                VBox medicineCard = createMedicineCard(medicine);

                medicinesContainer.add(medicineCard, col, row);

                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }

        } catch (Exception e) {
            System.err.println("Could not load medicines: " + e.getMessage());
        }
    }

    private VBox createMedicineCard(Medicine medicine) {
        VBox card = new VBox();
        card.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        card.setSpacing(10);
        card.setPrefWidth(280);
        card.setPrefHeight(220);
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); " +
                     "-fx-padding: 15; " +
                     "-fx-border-color: #e0e0e0; " +
                     "-fx-border-radius: 15; " +
                     "-fx-border-width: 1;");

        try {
            // Medicine image
            ImageView imageView = new ImageView();
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setPreserveRatio(true);

            // Try to load the image, use default if not found
            try {
                Image image = new Image(getClass().getResourceAsStream(medicine.imagePath));
                imageView.setImage(image);
            } catch (Exception e) {
                // Use default image if medicine image not found
                try {
                    Image defaultImage = new Image(getClass().getResourceAsStream("/images/m.png"));
                    imageView.setImage(defaultImage);
                } catch (Exception ex) {
                    // If even default image fails, create a placeholder
                    imageView.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5;");
                }
            }

            // Medicine name
            Label nameLabel = new Label(medicine.name);
            nameLabel.setStyle("-fx-font-family: 'System Bold'; " +
                              "-fx-font-size: 16px; " +
                              "-fx-text-fill: #633364; " +
                              "-fx-text-alignment: center;");
            nameLabel.setWrapText(true);
            nameLabel.setPrefWidth(250);

            // Medicine description
            Label descLabel = new Label(medicine.description);
            descLabel.setStyle("-fx-font-size: 12px; " +
                              "-fx-text-fill: #666666; " +
                              "-fx-wrap-text: true; " +
                              "-fx-text-alignment: center;");
            descLabel.setPrefWidth(250);
            descLabel.setWrapText(true);

            // Medicine price
            Label priceLabel = new Label(medicine.price);
            priceLabel.setStyle("-fx-font-family: 'System Bold'; " +
                               "-fx-font-size: 18px; " +
                               "-fx-text-fill: #449b6d;");

            // Add prescription label for prescription medicines
            Label rxLabel = new Label("Rx Required");
            rxLabel.setStyle("-fx-font-size: 10px; " +
                            "-fx-text-fill: #ff6b6b; " +
                            "-fx-background-color: #ffe0e0; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 2 8 2 8;");

            card.getChildren().addAll(imageView, nameLabel, descLabel, priceLabel);

            // Add prescription label for certain medicines
            if (medicine.name.contains("Amoxicillin") || medicine.name.contains("Lisinopril") ||
                medicine.name.contains("Metformin") || medicine.name.contains("Atorvastatin")) {
                card.getChildren().add(rxLabel);
            }

        } catch (Exception e) {
            // Fallback if card creation fails
            Label errorLabel = new Label("Product unavailable");
            errorLabel.setStyle("-fx-text-fill: #999999;");
            card.getChildren().add(errorLabel);
        }

        return card;
    }

    // Navigation methods
    @FXML
    private void navigateToProducts(ActionEvent event) {
        try {
            loadScene("products.fxml", "e-Dispensary - Medicines", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "Medicines catalog would load here.\nBrowse our complete inventory of prescription and over-the-counter medications.");
        }
    }

    @FXML
    private void navigateToHome(ActionEvent event) {
        // Already on home page, just show info
        showInfoAlert("Navigation", "You are already on the e-Dispensary Home page!");
    }

    @FXML
    private void navigateToAbout(ActionEvent event) {
        try {
            loadScene("about.fxml", "e-Dispensary - About", event);
        } catch (Exception e) {
            showInfoAlert("Navigation", "About e-Dispensary page would load here.");
        }
    }

    // Helper method to load new scenes
    private void loadScene(String fxmlFile, String title, ActionEvent event) throws IOException {
        URL resource = getClass().getResource(fxmlFile);
        if (resource != null) {
            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
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

    // Method to refresh page content (useful for when returning from other pages)
    public void refreshContent() {
        loadLatestProducts();
    }
}

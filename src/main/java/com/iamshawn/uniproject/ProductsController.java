package com.iamshawn.uniproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProductsController implements Initializable {

    // FXML UI Elements
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private GridPane productsContainer;

    // Product data class
    public static class Product {
        private String name;
        private String description;
        private double price;
        private String category;
        private String imagePath;
        private boolean inStock;
        private int stockQuantity;

        public Product(String name, String description, double price, String category, String imagePath, boolean inStock, int stockQuantity) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.imagePath = imagePath;
            this.inStock = inStock;
            this.stockQuantity = stockQuantity;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
        public String getImagePath() { return imagePath; }
        public boolean isInStock() { return inStock; }
        public int getStockQuantity() { return stockQuantity; }
    }

    // Sample product data
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load navbar
        loadNavbar();

        // Initialize sample product data
        initializeProductData();

        // Setup sort combo box
        setupSortComboBox();

        // Display all products initially
        displayProducts(allProducts);
    }

    private void loadNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("navbar.fxml"));
            Parent navbar = loader.load();
            mainBorderPane.setTop(navbar);
        } catch (IOException e) {
            System.err.println("Error loading navbar: " + e.getMessage());
        }
    }

    private void initializeProductData() {
        allProducts.add(new Product("Aspirin", "Pain relief medication for headaches and fever", 12.99, "Pain Relief", "/images/addmed.png", true, 50));
        allProducts.add(new Product("Vitamin C", "Immune system booster with essential nutrients", 15.49, "Vitamins", "/images/addmed.png", true, 30));
        allProducts.add(new Product("Ibuprofen", "Anti-inflammatory pain reliever", 8.99, "Pain Relief", "/images/addmed.png", true, 25));
        allProducts.add(new Product("Multivitamin", "Complete daily vitamin supplement", 22.99, "Vitamins", "/images/addmed.png", true, 40));
        allProducts.add(new Product("Cough Syrup", "Effective cough suppressant for dry cough", 11.49, "Cold & Flu", "/images/addmed.png", true, 20));
        allProducts.add(new Product("Antihistamine", "Allergy relief medication", 9.99, "Allergy", "/images/addmed.png", true, 35));
        allProducts.add(new Product("Calcium Tablets", "Bone health support supplement", 18.99, "Vitamins", "/images/addmed.png", true, 45));
        allProducts.add(new Product("Eye Drops", "Lubricating drops for dry eyes", 7.99, "Eye Care", "/images/addmed.png", true, 28));
        allProducts.add(new Product("Thermometer", "Digital fever thermometer", 25.99, "Medical Devices", "/images/addmed.png", true, 15));
        allProducts.add(new Product("First Aid Kit", "Complete emergency first aid kit", 34.99, "Medical Devices", "/images/addmed.png", true, 12));
        allProducts.add(new Product("Blood Pressure Monitor", "Digital BP monitoring device", 45.99, "Medical Devices", "/images/addmed.png", true, 8));
        allProducts.add(new Product("Probiotics", "Digestive health supplement", 28.99, "Digestive Health", "/images/addmed.png", true, 22));

        filteredProducts = new ArrayList<>(allProducts);
    }

    private void setupSortComboBox() {
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
            "Name (A-Z)", "Name (Z-A)", "Price (Low to High)", "Price (High to Low)", "Category"
        );
        sortComboBox.setItems(sortOptions);
        sortComboBox.setOnAction(e -> sortProducts());
    }

    @FXML
    private void searchProducts(KeyEvent event) {
        String searchQuery = searchField.getText().toLowerCase().trim();

        if (searchQuery.isEmpty()) {
            filteredProducts = new ArrayList<>(allProducts);
        } else {
            filteredProducts = allProducts.stream()
                .filter(product ->
                    product.getName().toLowerCase().contains(searchQuery) ||
                    product.getDescription().toLowerCase().contains(searchQuery) ||
                    product.getCategory().toLowerCase().contains(searchQuery)
                )
                .collect(Collectors.toList());
        }

        displayProducts(filteredProducts);
    }

    @FXML
    private void sortProducts() {
        String selectedSort = sortComboBox.getValue();
        if (selectedSort == null) return;

        switch (selectedSort) {
            case "Name (A-Z)":
                Collections.sort(filteredProducts, (p1, p2) -> p1.getName().compareTo(p2.getName()));
                break;
            case "Name (Z-A)":
                Collections.sort(filteredProducts, (p1, p2) -> p2.getName().compareTo(p1.getName()));
                break;
            case "Price (Low to High)":
                Collections.sort(filteredProducts, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
                break;
            case "Price (High to Low)":
                Collections.sort(filteredProducts, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "Category":
                Collections.sort(filteredProducts, (p1, p2) -> p1.getCategory().compareTo(p2.getCategory()));
                break;
        }

        displayProducts(filteredProducts);
    }

    private void displayProducts(List<Product> products) {
        productsContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productsContainer.add(productCard, column, row);

            column++;
            if (column >= 3) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(12);
        card.setPadding(new Insets(20));
        card.setPrefWidth(280);
        card.setPrefHeight(350);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // Product Image
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
            imageView.setImage(image);
        } catch (Exception e) {
            // Use default image if product image not found
            try {
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/addmed.png"));
                imageView.setImage(defaultImage);
            } catch (Exception ex) {
                System.err.println("Could not load product image: " + e.getMessage());
            }
        }
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);

        // Product Name
        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);

        // Product Description
        Label descLabel = new Label(product.getDescription());
        descLabel.setFont(Font.font("System", 12));
        descLabel.setStyle("-fx-text-fill: #5a6c7d;");
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setPrefHeight(50);

        // Category Badge
        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
        categoryLabel.setStyle(
            "-fx-background-color: #f8f9fa;" +
            "-fx-text-fill: #6c757d;" +
            "-fx-padding: 4 8 4 8;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;"
        );

        // Price
        Label priceLabel = new Label("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        priceLabel.setStyle("-fx-text-fill: #449b6d;");

        // Stock Status
        Label stockLabel = new Label(product.isInStock() ? "In Stock (" + product.getStockQuantity() + ")" : "Out of Stock");
        stockLabel.setFont(Font.font("System", 10));
        stockLabel.setStyle(product.isInStock() ? "-fx-text-fill: #28a745;" : "-fx-text-fill: #dc3545;");

        // Add to Cart Button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setPrefWidth(180);
        addToCartBtn.setPrefHeight(35);
        addToCartBtn.setFont(Font.font("System", FontWeight.NORMAL, 13));
        addToCartBtn.setStyle(
            "-fx-background-color: #449b6d;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;" +
            "-fx-border-width: 0;"
        );

        // Simple hover effect for button
        addToCartBtn.setOnMouseEntered(e -> addToCartBtn.setStyle(
            "-fx-background-color: #3d8b5d;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;" +
            "-fx-border-width: 0;"
        ));

        addToCartBtn.setOnMouseExited(e -> addToCartBtn.setStyle(
            "-fx-background-color: #449b6d;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;" +
            "-fx-border-width: 0;"
        ));

        addToCartBtn.setDisable(!product.isInStock());
        addToCartBtn.setOnAction(e -> addToCart(product));

        card.getChildren().addAll(imageView, nameLabel, descLabel, categoryLabel, priceLabel, stockLabel, addToCartBtn);

        // Simple hover effect for the entire card
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #f8f9fa;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #449b6d;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);"
        ));

        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        ));

        return card;
    }

    private void addToCart(Product product) {
        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Added");
        alert.setHeaderText(null);
        alert.setContentText(product.getName() + " has been added to your cart!");
        alert.showAndWait();
    }

    // Navigation methods (these are called from the footer hyperlinks)
    @FXML
    private void navigateToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle("e-Dispensary - Home");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not load home page.");
        }
    }

    @FXML
    private void navigateToAbout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("about.fxml"));
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle("e-Dispensary - About");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not load about page.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package com.iamshawn.uniproject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class  ProductDetailsController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label breadcrumbProductName;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label brandLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Button addToCartButton;

    @FXML
    private Label addToCartMessage;

    @FXML
    private Text descriptionText;

    @FXML
    private Text usageText;

    // demo data (instead of database)
    private String productName = "Demo Medicine";
    private String productBrand = "Demo Brand";
    private String productCategory = "Antibiotics";
    private double productPrice = 15.99;
    private String productStatus = "Available";
    private String productImagePath = "/images/m.png";

    private Map<String, String> productDescriptions = new HashMap<>();
    private Map<String, String> productUsages = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // setup spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        quantitySpinner.setValueFactory(valueFactory);

        // init info maps
        productDescriptions.put("default", "This is a sample product description.");
        productUsages.put("default", "Use this medicine as directed by your doctor.");

        // load product demo info
        loadProductDetails();
    }

    private void loadProductDetails() {
        breadcrumbProductName.setText(productName);
        productNameLabel.setText(productName);
        brandLabel.setText(productBrand);
        categoryLabel.setText(productCategory);
        priceLabel.setText(String.format("$%.2f", productPrice));
        statusLabel.setText(productStatus);

        if ("Available".equalsIgnoreCase(productStatus)) {
            statusLabel.setStyle("-fx-text-fill: #449b6d;");
            addToCartButton.setDisable(false);
        } else {
            statusLabel.setStyle("-fx-text-fill: #cc0000;");
            addToCartButton.setDisable(true);
        }

        // image load
        try {
            Image image = new Image(getClass().getResourceAsStream(productImagePath));
            productImage.setImage(image);
        } catch (Exception e) {
            System.out.println("Could not load product image: " + e.getMessage());
        }

        // set description
        descriptionText.setText(productDescriptions.getOrDefault(productCategory, productDescriptions.get("default")));
        usageText.setText(productUsages.getOrDefault(productCategory, productUsages.get("default")));
    }

    @FXML
    private void addToCart(ActionEvent event) {
        int quantity = quantitySpinner.getValue();

        // demo message
        addToCartMessage.setText("Added to cart: " + quantity + " x " + productName);
        addToCartMessage.setVisible(true);

        // fade animation
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), addToCartMessage);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
        });
        pause.play();

        System.out.println("Added to cart: " + productName + " (Qty: " + quantity + ")");
    }

    @FXML
    private void navigateToHome(ActionEvent event) throws Exception {
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iamshawn/uniproject/home.fxml"));
        stage.setScene(new javafx.scene.Scene(loader.load(), 1000, 700));
        stage.setTitle("e-Dispensary - Home");
        stage.show();
    }

    @FXML
    private void navigateToProducts(ActionEvent event) throws Exception {
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iamshawn/uniproject/products.fxml"));
        stage.setScene(new javafx.scene.Scene(loader.load(), 1000, 700));
        stage.setTitle("e-Dispensary - Products");
        stage.show();
    }

    @FXML
    private void navigateToAbout(ActionEvent event) throws Exception {
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iamshawn/uniproject/about.fxml"));
        stage.setScene(new javafx.scene.Scene(loader.load(), 1000, 700));
        stage.setTitle("e-Dispensary - About");
        stage.show();
    }
}

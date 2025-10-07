package com.iamshawn.uniproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the home page as the starting point to showcase the e-Dispensary website
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
        Scene scene = new Scene(root, 1400, 800);

        stage.setTitle("e-Dispensary - Pharmacy Management System");
        stage.setResizable(false);
        stage.setScene(scene);

        // Make the window draggable
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.show();}

    // fix the repo

    public static void main(String[] args) {
        launch();
    }
}

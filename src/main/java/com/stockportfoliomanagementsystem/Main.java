package com.stockportfoliomanagementsystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        stage.setHeight(700);
        stage.setWidth(1200);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Book Nook");

        stage.setResizable(false);

        // Load the image using ClassLoader
        String relativePath = "/com/stockportfoliomanagementsystem/Images/logoIcon.png";
        InputStream iconStream = Main.class.getResourceAsStream(relativePath);
        Image iconImage = new Image(iconStream);
        stage.getIcons().add(iconImage);

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}
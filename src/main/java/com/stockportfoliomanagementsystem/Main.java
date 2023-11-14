package com.stockportfoliomanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Application {
    Connection conn = MySqlCon.MysqlMethod();
    @Override
    public void start(Stage stage) throws IOException {
        sqlUpdates();



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

    public void sqlUpdates() {
        String sql = "Update stock SET Total = Selling_price*Qty";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
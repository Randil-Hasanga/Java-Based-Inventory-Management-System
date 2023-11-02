package com.stockportfoliomanagementsystem.StockKeeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectSupplierType {
    @FXML
    private Button btnExistingCus;

    @FXML
    private ImageView imageView;

    @FXML
    private Label txtName;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    void onExistingSupplier(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectExistingSupplier.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onNewSupplier(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/AddNewSupplier.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
    @FXML
    void onCustomersButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewCustomers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    @FXML
    void onBackButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/StockKeeperDashboard.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewSuppliers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

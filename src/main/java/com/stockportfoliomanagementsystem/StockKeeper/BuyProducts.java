package com.stockportfoliomanagementsystem.StockKeeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BuyProducts implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label txtName;

    private static int isExisting;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    private void setProductExisting(int existing) {
        System.out.println("setter"+isExisting);
        this.isExisting = existing;
    }

    public static int getProductExisting() {
        return isExisting;
    }
    @FXML
    void onCustomersButton(MouseEvent event) {

    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) {

    }

    @FXML
    void onNewProduct(MouseEvent event) {

    }

    @FXML
    void onExistingProducts(MouseEvent event) throws IOException {

        int existing = 1;
        System.out.println("declare"+existing);

        setProductExisting(existing);

        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectCustomerType.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

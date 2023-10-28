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

public class SelectCustomerType implements Initializable {



    @FXML
    private ImageView imageView;
    @FXML
    private Label txtName;

    int isExisting;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       isExisting = BuyProducts.getProductExisting();
    }
    @FXML
    void onCustomersButton(MouseEvent event) {

    }

    @FXML
    void onExistingCustomer(MouseEvent event) throws IOException {
        System.out.println("SelectCus type"+isExisting);

        if(isExisting == 1){
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectEsistingCustomer.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else{
        }

    }

    @FXML
    void onNewCustomer(MouseEvent event) {

    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) {

    }


}

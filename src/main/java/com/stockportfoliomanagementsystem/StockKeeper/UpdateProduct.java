package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateProduct implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private ImageView imageView;

    @FXML
    private Label lblSuccess;

    @FXML
    private TextField txtDescription;

    @FXML
    private Label txtName;

    @FXML
    private TextField txtPid;

    @FXML
    private TextField txtPname;

    @FXML
    private TextField txtPriceTaken;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtSellingPrice;

    @FXML
    private TextField txtSupplierID;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String pid = ManageProducts.getSelectedProduct();
    private String p_name;
    private String p_description;
    private double p_priceTaken;
    private double p_sellingPrice;
    private int p_quantity;
    private String p_supplierID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql = "SELECT * FROM stock WHERE P_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,pid);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                p_name = rs.getString("P_Name");
                p_description = rs.getString("P_Description");
                p_priceTaken = rs.getDouble("Price_taken");
                p_sellingPrice = rs.getDouble("Selling_price");
                p_quantity = rs.getInt("Qty");
                p_supplierID = rs.getString("S_ID");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtPid.setText(pid);
        txtPname.setText(p_name);
        txtDescription.setText(p_description);
        txtPriceTaken.setText(String.valueOf(p_priceTaken));
        txtSellingPrice.setText(String.valueOf(p_sellingPrice));
        txtQuantity.setText(String.valueOf(p_quantity));
        txtSupplierID.setText(p_supplierID);

        txtSupplierID.setEditable(false);
        txtPid.setEditable(false);
    }

    @FXML
    void onUpdateButton(MouseEvent event) {
        p_name = txtPname.getText();
        p_description = txtDescription.getText();
        p_priceTaken = Double.parseDouble(txtPriceTaken.getText());
        p_sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        p_quantity = Integer.parseInt(txtQuantity.getText());

        if(p_name.isEmpty() || p_description.isEmpty() || txtPriceTaken.getText().isEmpty() || txtSellingPrice.getText().isEmpty() || txtQuantity.getText().isEmpty()){
            MainController.fillAllTheFieldsAlert();
        }else{
            String sql = "UPDATE stock SET P_Name = ?, P_Description = ?, Price_taken = ?, Selling_price = ?, Qty = ? WHERE P_ID = ?";

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,p_name);
                preparedStatement.setString(2,p_description);
                preparedStatement.setDouble(3,p_priceTaken);
                preparedStatement.setDouble(4,p_sellingPrice);
                preparedStatement.setInt(5,p_quantity);
                preparedStatement.setString(6,pid);
                preparedStatement.executeUpdate();
                lblSuccess.setText("Product Updated Successfully");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onBuyProduct(MouseEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectSupplierType.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new IOException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onSellProducts(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectCustomerType.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }
    @FXML
    void onCustomersButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewCustomers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }



    @FXML
    void onSupplierButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewSuppliers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onBackButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/ManageProducts.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }

}

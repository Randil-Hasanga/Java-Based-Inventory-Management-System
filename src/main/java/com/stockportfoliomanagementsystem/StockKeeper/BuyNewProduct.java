package com.stockportfoliomanagementsystem.StockKeeper;

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

public class BuyNewProduct implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    private int stockRowCount;
    private String productName;
    private String productID;
    private double priseTaken;
    private double sellingPrice;
    private int quantity;
    private String description;
    private String getSupID;
    private String SupplierTypeExisting = SelectExistingSupplier.getSupplierType();
    private String SupplierTypeNew = AddNewSupplier.getSupplierType();
    private String supID;
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

    @FXML
    void onAddButton(MouseEvent event) throws IOException {
        productName = txtPname.getText();
        priseTaken = Double.parseDouble(txtPriceTaken.getText());
        sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        quantity = Integer.parseInt(txtQuantity.getText());
        description = txtDescription.getText();

        String sql = "INSERT INTO stock (P_ID, P_Name, Price_taken, Selling_price, Qty, P_Description, S_ID, Total) VALUES (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(stockRowCount+1));
            pstmt.setString(2,productName);
            pstmt.setDouble(3,priseTaken);
            pstmt.setDouble(4,sellingPrice);
            pstmt.setInt(5,quantity);
            pstmt.setString(6,description);
            pstmt.setString(7,supID);
            pstmt.setDouble(8,sellingPrice*quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Stock updated");

        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/BuyyExisting.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(SupplierTypeNew != null) {
            supID = AddNewSupplier.getSupIDNew();
            System.out.println("Customer ID: " + supID);
        }else
        if(SupplierTypeExisting != null){
            supID = SelectExistingSupplier.getSupplierIndex();
            System.out.println("Supplier ID: " + supID);
        }

        txtSupplierID.setEditable(false);
        txtPid.setEditable(false);

        String sql = "SELECT COUNT(*) FROM stock";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                stockRowCount = rs.getInt(1);
                System.out.println("Stock row count : "+stockRowCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(stockRowCount == 0){
            txtPid.setText("P001");
        }else if(stockRowCount < 9) {
            txtPid.setText("P00" + (stockRowCount + 1));
        }else if(stockRowCount < 99){
            txtPid.setText("P0" + (stockRowCount + 1));
        }else{
            txtPid.setText("P" + (stockRowCount + 1));
        }

        txtSupplierID.setText(supID);

    }
}

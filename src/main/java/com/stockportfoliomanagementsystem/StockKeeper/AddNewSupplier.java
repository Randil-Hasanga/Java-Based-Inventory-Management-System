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

public class AddNewSupplier implements Initializable {

    private Connection conn = MySqlCon.MysqlMethod();
    private int count;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView imageView;
    @FXML
    private Label lblSuccess;
    @FXML
    private Label txtName;
    @FXML
    private TextField txtSupAddress;
    @FXML
    private TextField txtSupContact;
    @FXML
    private TextField txtSupID;
    @FXML
    private TextField txtSupName;

    private String supName;
    private static String supID;
    private String supAddress;
    private String supContact;
    protected static String supplierType;

    public static String getSupIDNew(){
        return supID;
    }

    private void setSupplierType(String existing) {
        this.supplierType = existing;
    }
    public static String getSupplierType() {
        return supplierType;
    }

    @FXML
    void onAddButton(MouseEvent event) throws IOException {
        supName = txtSupName.getText();
        supAddress = txtSupAddress.getText();
        supContact = txtSupContact.getText();
        supID = txtSupID.getText();

        String sql = "INSERT INTO supplier (S_ID, S_Name, S_Location, S_Contact) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(count+1));
            pstmt.setString(2, supName);
            pstmt.setString(3, supAddress);
            pstmt.setString(4, supContact);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        lblSuccess.setText("Customer Added Successfully");
        setSupplierType("New");
        SelectExistingSupplier.supplierType = null;

        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/BuyNewProduct.fxml"));
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
        String sql = "SELECT COUNT(*) FROM supplier";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(count == 0){
            txtSupID.setText("S001");
        }else if(count < 9) {
            txtSupID.setText("S00" + (count + 1));
        }else if(count < 99){
            txtSupID.setText("S0" + (count + 1));
        }else{
            txtSupID.setText("S" + (count + 1));
        }
        supID = txtSupID.getText();
        System.out.println("supID :"+supID);
        txtSupID.setText(supID);
        txtSupID.setEditable(false);
    }
}

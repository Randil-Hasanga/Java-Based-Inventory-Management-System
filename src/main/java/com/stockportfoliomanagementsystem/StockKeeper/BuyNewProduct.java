package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.Main;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyNewProduct implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();

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
    private String max;
    private int numericId;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void onAddButton(MouseEvent event) throws IOException {
        productName = txtPname.getText();
        priseTaken = Double.parseDouble(txtPriceTaken.getText());
        sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        quantity = 0;
        description = txtDescription.getText();

        if(productName.isEmpty() || txtPriceTaken.getText().isEmpty() || txtSellingPrice.getText().isEmpty() || txtDescription.getText().isEmpty()){
            MainController.fillAllTheFieldsAlert();
        }else{
            String sql = "INSERT INTO stock (P_ID, P_Name, Price_taken, Selling_price,Qty, P_Description, S_ID) VALUES (?,?,?,?,?,?,?)";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, String.valueOf(numericId+1));
                pstmt.setString(2,productName);
                pstmt.setDouble(3,priseTaken);
                pstmt.setDouble(4,sellingPrice);
                pstmt.setInt(5,quantity);
                pstmt.setString(6,description);
                pstmt.setString(7,supID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
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
    }

    @FXML
    void onCustomersButton(MouseEvent event) throws IOException {
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
            throw new IOException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) throws IOException {
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
            throw new IOException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onHomeButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/StockKeeperDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onSellProducts(MouseEvent event) throws IOException {
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
            throw new IOException(e);
        } catch (NullPointerException e) {
        }
    }
    @FXML
    void onUpdateProducts(MouseEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/ManageProducts.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (NullPointerException e) {
        } catch (IOException e) {
            throw new IOException(e);
        }
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

        String sql2 = "SELECT MAX(P_ID) FROM stock";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                max = rs.getString(1);
                System.out.println("Last : "+max);
            }
            Pattern pattern = Pattern.compile("\\d+");

            // Use a Matcher to find the numeric part
            Matcher matcher = pattern.matcher(max);

            if (matcher.find()) {
                // Extract the numeric part as a string
                String numericPart = matcher.group();

                // Convert the numeric part to an integer if needed
                numericId = Integer.parseInt(numericPart);

                // Now you have the numeric ID as an integer
                System.out.println("Numeric ID: " + numericId);
            } else {
                System.out.println("No numeric part found in the C_ID value.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(numericId == 0){
            txtPid.setText("P001");
        }else if(numericId < 9) {
            txtPid.setText("P00" + (numericId + 1));
        }else if(numericId < 99){
            txtPid.setText("P0" + (numericId + 1));
        }else{
            txtPid.setText("P" + (numericId + 1));
        }
        txtPid.setEditable(false);

        txtSupplierID.setText(supID);
        txtSupplierID.setEditable(false);

        txtSupplierID.setText(supID);

    }
}

//package com.stockportfoliomanagementsystem.StockKeeper;
//
//import com.stockportfoliomanagementsystem.MySqlCon;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ResourceBundle;
//
//public class BuyExisting implements Initializable {
//
//    Connection conn = MySqlCon.MysqlMethod();
//    @FXML
//    private ImageView imageView;
//
//    @FXML
//    private TableView<?> tblCart;
//
//    @FXML
//    private TableView<ObservableList<String>> tblProducts;
//
//    @FXML
//    private Label txtName;
//
//    @FXML
//    void onAddBtnClick(MouseEvent event) {
//
//    }
//
//    @FXML
//    void onCustomersButton(MouseEvent event) {
//
//    }
//
//    @FXML
//    void onManageStock(MouseEvent event) {
//
//    }
//
//    @FXML
//    void onReportsButton(MouseEvent event) {
//
//    }
//
//    @FXML
//    void onSupplierButton(MouseEvent event) {
//
//    }
//
//    @FXML
//    void onAddToCart(MouseEvent event) {
//
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        loadFromDB();
//    }
//
//    public void loadFromDB() {
//        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblProducts.getColumns();
//        columns.clear();
//
//        // Define fixed column names
//        String[] columnNames = {"Product ID","Name","Price","Remaining QTY","Description","Supplier"};
//
//        double columnWidth = (tblProducts.getPrefWidth()) / (columnNames.length)-1;
//
//        // Add the columns to the TableView with fixed names
//        for (int i = 0; i < columnNames.length; i++) {
//            final int columnIndex = i;
//            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
//            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
//            column.setPrefWidth(columnWidth);
//            columns.add(column);
//        }
//
//        String sql = "SELECT stock.P_ID, stock.P_Name, stock.Selling_price, stock.Qty, stock.P_Description, supplier.S_Name FROM stock,supplier WHERE stock.S_ID = supplier.S_ID";
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            ResultSet rs = pstmt.executeQuery();
//
//            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
//            while (rs.next()) {
//                ObservableList<String> row = FXCollections.observableArrayList();
//                for (int i = 1; i <= columnNames.length; i++) {
//                    row.add(rs.getString(i));
//                }
//                data.add(row);
//            }
//
//            tblProducts.setItems(data);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
//

package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BuyExisting implements Initializable {


    Connection conn = MySqlCon.MysqlMethod();

    @FXML
    private TableView<ObservableList<String>> tblCart;

    @FXML
    private TableView<ObservableList<String>> tblProducts;

    @FXML
    private Label txtName;

    @FXML
    private Spinner<Integer> quantitySpinner; // Add this spinner

    @FXML
    private Button addToCartButton; // Add the "Add to Cart" button

    @FXML
    void onAddBtnClick(MouseEvent event) {

    }

    @FXML
    void onCustomersButton(MouseEvent event) {

    }

    @FXML
    void onManageStock(MouseEvent event) {

    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
        tblProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        quantitySpinner.setValueFactory(valueFactory);

        ObservableList<TableColumn<ObservableList<String>, ?>> cartColumns = tblCart.getColumns();
        cartColumns.clear();

        // Define column names for the cart table
        String[] cartColumnNames = {"Product ID", "Name", "Price", "Description", "Supplier", "Quantity"};

        double cartColumnWidth = tblCart.getPrefWidth() / cartColumnNames.length;

        // Add the columns to the cart table with fixed names
        for (int i = 0; i < cartColumnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(cartColumnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(cartColumnWidth);
            cartColumns.add(column);
        }

        addToCartButton.setOnAction(addToCartEvent -> {
            onAddToCart();
        });
    }

    public void loadFromDB() {
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblProducts.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Product ID","Name","Price","Remaining QTY","Description","Supplier"};

        double columnWidth = tblProducts.getPrefWidth() / columnNames.length;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT stock.P_ID, stock.P_Name, stock.Selling_price, stock.Qty, stock.P_Description, supplier.S_Name FROM stock, supplier WHERE stock.S_ID = supplier.S_ID";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnNames.length; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tblProducts.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //addToCartButton.setOnAction(this::onAddToCart);
    }

    private void onAddToCart() {
//        ObservableList<String> selectedProduct = tblProducts.getSelectionModel().getSelectedItem();
//
//
//        if (selectedProduct != null) {
//            int selectedQuantity = quantitySpinner.getValue(); // Get the selected quantity
//
//
//            ObservableList<String> cartItem = FXCollections.observableArrayList();
//            cartItem.add(selectedProduct.get(0)); // Product ID
//            cartItem.add(selectedProduct.get(1)); // Name
//            cartItem.add(selectedProduct.get(2)); // Price
//            cartItem.add(selectedProduct.get(4)); // Description
//            cartItem.add(selectedProduct.get(5)); // Supplier
//            cartItem.add(String.valueOf(selectedQuantity)); // Quantity
//            tblCart.getItems().add(cartItem);
//
//            // Optionally, you can save the cart data to the database here
//        }

        ObservableList<String> selectedProduct = tblProducts.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            int selectedQuantity = quantitySpinner.getValue(); // Get the selected quantity
            String productId = selectedProduct.get(0);

            boolean itemFound = false;

            // Check if the product is already in the cart
            for (ObservableList<String> cartItem : tblCart.getItems()) {
                if (cartItem.get(0).equals(productId)) { // Check if the product ID matches
                    int currentQuantity = Integer.parseInt(cartItem.get(5)); // Quantity is at index 5
                    int newQuantity = currentQuantity + selectedQuantity;
                    System.out.println(newQuantity);
                    tblCart.setEditable(true);
                    cartItem.set(5, String.valueOf(newQuantity)); // Update the quantity
                    tblCart.setEditable(false);
                    itemFound = true;

                    String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
                    try {
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setInt(1, selectedQuantity);
                        preparedStatement.setString(2, selectedProduct.get(0));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    loadFromDB();
                    break;
                }

            }

            if (!itemFound) {
                // Product not found in the cart, add it as a new item
                ObservableList<String> cartItem = FXCollections.observableArrayList();
                cartItem.add(selectedProduct.get(0)); // Product ID
                cartItem.add(selectedProduct.get(1)); // Name
                cartItem.add(selectedProduct.get(2)); // Price
                cartItem.add(selectedProduct.get(4)); // Description
                cartItem.add(selectedProduct.get(5)); // Supplier
                cartItem.add(String.valueOf(selectedQuantity)); // Quantity
                tblCart.getItems().add(cartItem);

                String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, selectedQuantity);
                    preparedStatement.setString(2, selectedProduct.get(0));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                loadFromDB();
            }else{
                tblCart.refresh();

            }

//            String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
//            try {
//                PreparedStatement preparedStatement = conn.prepareStatement(sql);
//                preparedStatement.setInt(1, selectedQuantity);
//                preparedStatement.setString(2, selectedProduct.get(0));
//                preparedStatement.executeUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            loadFromDB();
//            tblProducts.refresh();
            //tblCart.refresh();

            // Optionally, you can save the cart data to the database here
        }
    }
}


package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.Main;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.stockportfoliomanagementsystem.StockKeeper.SellExisting.scene;

public class BuyyExisting implements Initializable {
    Connection conn = MySqlCon.MysqlMethod();
    private String index;
    @FXML
    private Button addToCartButton;

    @FXML
    private Button btnReduce;

    @FXML
    private Button btnRemove;

    @FXML
    private ImageView imageView;

    @FXML
    private Button onBuyBtn;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private TableView<ObservableList<String>> tblCart;

    @FXML
    private TableView<ObservableList<String>> tblProducts;
    @FXML
    private Label lblSuccess;


    @FXML
    private Label txtName;
    private static Scene scene;
    private int invoiceRowCount;

    private String SupplierTypeExisting = SelectExistingSupplier.getSupplierType();
    private String SupplierTypeNew = AddNewSupplier.getSupplierType();

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
    private void setScene(Scene scene) {
        this.scene = scene;
    }

    public static Scene getScene(){
        return scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SupplierTypeNew != null) {
            index = AddNewSupplier.getSupIDNew();
            System.out.println("Customer ID: " + index);
        }else
            if(SupplierTypeExisting != null){
            index = SelectExistingSupplier.getSupplierIndex();
            System.out.println("Supplier ID: " + index);
        }

        System.out.println(LocalDate.now());
        System.out.println(index);
        loadFromDB();
        tblProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblCart.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        quantitySpinner.setValueFactory(valueFactory);

        // Create the columns for the cart table
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
        onBuyBtn.setOnAction(sellEvent -> {
            buyProducts();
        });
        btnRemove.setOnAction(removeEvent -> {
            removeProduct();
        });
        btnReduce.setOnAction(reduceEvent -> {
            reduceQuantity();
            tblCart.getSelectionModel().select(tblCart.getItems().size() - 1);
        });

        tblCart.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super ObservableList<String>>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (ObservableList<String> item : change.getRemoved()) {
                        tblCart.getSelectionModel().select(item);
                    }
                }
            }
        });


    }

    private void reduceQuantity() {
        ObservableList<String> selected = tblCart.getSelectionModel().getSelectedItem();

        if(selected != null){
            int qt = Integer.parseInt(selected.get(5));
            String pid = selected.get(0);
            String name = selected.get(1);
            String price = selected.get(2);
            String desc = selected.get(3);
            String sup = selected.get(4);

            if(qt>1){
                qt = qt-1;
                ObservableList<ObservableList<String>> data = tblCart.getItems();
                data.remove(selected);
                ObservableList<String> cartItem = FXCollections.observableArrayList();
                cartItem.add(pid);
                cartItem.add(name);
                cartItem.add(price);
                cartItem.add(desc);
                cartItem.add(sup);
                cartItem.add(String.valueOf(qt));
                tblCart.getItems().add(cartItem);

                String sql = "Update stock SET Qty = Qty-1 WHERE P_ID = ?";
                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, pid);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else if(qt==1){
                String sql = "Update stock SET Qty = Qty-1 WHERE P_ID = ?";
                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, pid);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ObservableList<ObservableList<String>> data = tblCart.getItems();
                data.remove(selected);
                try {
                    tblCart.getSelectionModel().clearSelection();
                }catch (Exception e){
                    System.out.println("Selection Cleared");
                }
            }
            StockKeeperController.dbUpdate();
            loadFromDB();
        }else{
            showCustomDialog();
        }
    }

    private void removeProduct() {
        ObservableList<String> selected = tblCart.getSelectionModel().getSelectedItem();
        if(selected != null){
            int qt = Integer.parseInt(selected.get(5));
            String pid = selected.get(0);
            ObservableList<ObservableList<String>> data = tblCart.getItems();
            data.remove(selected);

            String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, qt);
                preparedStatement.setString(2, pid);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            tblCart.getSelectionModel().clearSelection();
            StockKeeperController.dbUpdate();
            loadFromDB();
        } else{
            showCustomDialog();
        }
    }

    private void buyProducts() {
        ObservableList<ObservableList<String>> table = tblCart.getItems();

        int rowCount = table.size();
        System.out.println("Row count: " + rowCount);


        System.out.println(table.size());

        if(rowCount > 0){
            for (ObservableList<String> cartItem2 : tblCart.getItems()) {
                String count = "SELECT COUNT(*) FROM transactions_sup";
                try {
                    PreparedStatement statement = conn.prepareStatement(count);
                    ResultSet rs = statement.executeQuery();

                    while (rs.next()) {
                        invoiceRowCount = rs.getInt(1);
                        System.out.println(count);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String sql = "INSERT INTO transactions_sup (transaction_id,Date_, Qty, Price, Total, S_ID, P_ID)\n" +
                        "VALUES\n" +
                        "(?,?,?,?,?,?,?)";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, String.valueOf(invoiceRowCount + 1));
                    pstmt.setDate(2, Date.valueOf(LocalDate.now()));
                    pstmt.setInt(3, Integer.parseInt(cartItem2.get(5)));
                    pstmt.setDouble(4, Double.parseDouble(cartItem2.get(2)));
                    pstmt.setDouble(5, Double.parseDouble(cartItem2.get(2)) * Integer.parseInt(cartItem2.get(5)));
                    pstmt.setString(6, index);
                    pstmt.setString(7, cartItem2.get(0));

                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("DB updated");
                StockKeeperController.dbUpdate();
                loadFromDB();
            }
            ObservableList<ObservableList<String>> data = tblCart.getItems();
            data.clear();
            lblSuccess.setText("Products Bought Successfully");

        }else{
            showTableEmptyDialog();
        }

    }

    public void showTableEmptyDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Warning !");

        Label messageLabel = new Label("Table Empty.");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialog.close());

        VBox dialogVBox = new VBox(10);
        dialogVBox.getChildren().addAll(messageLabel, closeButton);
        dialogVBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Scene dialogScene = new Scene(dialogVBox, 300, 100);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
    private void onAddToCart() {
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

                    String sql = "Update stock SET Qty = Qty+? WHERE P_ID = ?";
                    try {
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setInt(1, selectedQuantity);
                        preparedStatement.setString(2, selectedProduct.get(0));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    StockKeeperController.dbUpdate();
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

                String sql = "Update stock SET Qty = Qty+? WHERE P_ID = ?";
                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, selectedQuantity);
                    preparedStatement.setString(2, selectedProduct.get(0));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                StockKeeperController.dbUpdate();
                loadFromDB();
            }else{
                tblCart.refresh();

            }
        }else{
            showCustomDialog();
        }
    }

    private void loadFromDB() {
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

        String sql = "SELECT stock.P_ID, stock.P_Name, stock.Selling_price, stock.Qty, stock.P_Description, supplier.S_Name FROM stock, supplier WHERE stock.S_ID = supplier.S_ID AND supplier.S_ID = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,index);
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
    }
    public void showCustomDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Warning !");

        Label messageLabel = new Label("Please select a row from the table.");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialog.close());

        VBox dialogVBox = new VBox(10);
        dialogVBox.getChildren().addAll(messageLabel, closeButton);
        dialogVBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Scene dialogScene = new Scene(dialogVBox, 300, 100);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}

package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.Main;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SellExisting implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    private String index;
    private String customerTypeNew;
    private String customerTypeExisting;
    private String max;
    private int numericId;
    @FXML
    private Stage stage;
    private Scene scene2;
    private Parent root;

    @FXML
    private TableView<ObservableList<String>> tblCart;

    @FXML
    private TableView<ObservableList<String>> tblProducts;
    @FXML
    private Pane paneSell;

    @FXML
    private Label txtName;

    @FXML
    private Spinner<Integer> quantitySpinner; // Add this spinner

    @FXML
    private Button addToCartButton; // Add the "Add to Cart" button
    @FXML
    private Button onSellBtn;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnReduce;

    static Scene scene;

    private String CustomerTypeNew = AddNewCustomer.getCustomerType();
    private String CustomerTypeExisting = SelectExistingCustomer.getCustomerType();
    private BooleanProperty isTableViewNotEmpty = new SimpleBooleanProperty(false);
    @FXML
    private Pane paneCell;
    private int selectedQuantity;

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

        tblCart.getItems().addListener((ListChangeListener.Change<? extends ObservableList<String>> change) -> {
            isTableViewNotEmpty.set(!tblCart.getItems().isEmpty());
        });

        paneCell.disableProperty().bind(isTableViewNotEmpty);

        if(CustomerTypeNew != null) {
            index = AddNewCustomer.getCusIDNew();
            System.out.println("Customer ID: " + index);
        }else if(CustomerTypeExisting != null){
            index = SelectExistingCustomer.getCustomerIndex();
            System.out.println("Customer ID: " + index);
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
        onSellBtn.setOnAction(sellEvent -> {
            sellProducts();
        });
        btnRemove.setOnAction(removeEvent -> {
            try {
                removeProduct();
            } catch (Exception e) {
            }
        });

        btnReduce.setOnAction(reduceEvent -> {
            reduceQuantity();
            tblCart.getSelectionModel().select(tblCart.getItems().size() - 1);
        });

//        tblCart.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super ObservableList<String>>) change -> {
//            while (change.next()) {
//                if (change.wasRemoved()) {
//                    for (ObservableList<String> item : change.getRemoved()) {
//                        try {
//                            tblCart.getSelectionModel().select(item);
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }
//            }
//        });


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

                String sql = "Update stock SET Qty = Qty+1 WHERE P_ID = ?";
                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, pid);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else if(qt==1){
                String sql = "Update stock SET Qty = Qty+1 WHERE P_ID = ?";
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
        ObservableList<String> selectedProduct = tblProducts.getSelectionModel().getSelectedItem();

        int qty = Integer.parseInt(selectedProduct.get(3));
        if (selectedProduct != null) {
            if (qty > 0) {
                selectedQuantity = quantitySpinner.getValue(); // Get the selected quantit
                if (qty > selectedQuantity) {
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

                        String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
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
                    } else {
                        tblCart.refresh();
                    }

                } else{
                    selectedQuantity = quantitySpinner.getValue();
                    String productId = selectedProduct.get(0);
                    boolean itemFound = false;

                    // Check if the product is already in the cart
                    for (ObservableList<String> cartItem : tblCart.getItems()) {
                        if (cartItem.get(0).equals(productId)) { // Check if the product ID matches
                            int currentQuantity = Integer.parseInt(cartItem.get(5)); // Quantity is at index 5
                            System.out.println(qty);
                            tblCart.setEditable(true);
                            cartItem.set(5, String.valueOf(qty)); // Update the quantity
                            tblCart.setEditable(false);
                            itemFound = true;

                            String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
                            try {
                                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                                preparedStatement.setInt(1, qty);
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
                        cartItem.add(String.valueOf(qty)); // Quantity
                        tblCart.getItems().add(cartItem);

                        String sql = "Update stock SET Qty = Qty-? WHERE P_ID = ?";
                        try {
                            PreparedStatement preparedStatement = conn.prepareStatement(sql);
                            preparedStatement.setInt(1, qty);
                            preparedStatement.setString(2, selectedProduct.get(0));
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        StockKeeperController.dbUpdate();
                        loadFromDB();
                    } else {
                        tblCart.refresh();
                    }
                    tblProducts.refresh();
                    tblCart.refresh();
                }
            }else{
                System.out.println("Out of Stock");
            }
        }else {
            showCustomDialog();
        }

    }

    private void sellProducts() {

        ObservableList<ObservableList<String>> table = tblCart.getItems();

        int rowCount = table.size();
        System.out.println("Row count: " + rowCount);


        System.out.println(table.size());

        if(rowCount > 0){
            for (ObservableList<String> cartItem2 : tblCart.getItems()) {
                String count = "SELECT MAX(transaction_id) FROM transactions_cus";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(count);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        max = rs.getString(1);
                        System.out.println("Last : "+max);
                    }
                    Pattern pattern = Pattern.compile("\\d+");

                    if(max == null){
                        max = "T_000";
                    }

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

                String sql = "INSERT INTO transactions_cus (transaction_id,Date_, Qty, Price, Total, C_ID, P_ID)\n" +
                        "VALUES\n" +
                        "(?,?,?,?,?,?,?)";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, String.valueOf(numericId + 1));
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

                String sql2 = "INSERT INTO temp_invoice (transaction_id,Date_, Qty, Price, Total, C_ID, P_ID)\n" +
                        "VALUES\n" +
                        "(?,?,?,?,?,?,?)";
                try {
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                    pstmt2.setString(1, String.valueOf(numericId + 1));
                    pstmt2.setDate(2, Date.valueOf(LocalDate.now()));
                    pstmt2.setInt(3, Integer.parseInt(cartItem2.get(5)));
                    pstmt2.setDouble(4, Double.parseDouble(cartItem2.get(2)));
                    pstmt2.setDouble(5, Double.parseDouble(cartItem2.get(2)) * Integer.parseInt(cartItem2.get(5)));
                    pstmt2.setString(6, index);
                    pstmt2.setString(7, cartItem2.get(0));

                    pstmt2.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                System.out.println("DB updated");
            }

            table.clear();

            try {
                // Load the FXML file for the new window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/paymentRecieptCustomer.fxml"));
                Parent root = loader.load();
                Stage newStage = new Stage();
                Scene scene = new Scene(root);
                setScene(scene);
                newStage.setScene(scene);

                Screen primaryScreen = Screen.getPrimary();

                double screenWidth = primaryScreen.getBounds().getWidth();
                double screenHeight = primaryScreen.getBounds().getHeight();

                System.out.println(screenWidth);
                System.out.println(screenHeight);
                // Calculate the center position
                double centerX = screenWidth / 2;
                double centerY = screenHeight / 2;

                double boundX = centerX - (850 / 2);
                double boundY = centerY - (640 / 2);

                newStage.setX(boundX);
                newStage.setY(boundY);

                newStage.setResizable(false);
                newStage.setTitle("Invoice");

                String relativePath = "/com/stockportfoliomanagementsystem/Images/logoIcon.png";
                InputStream iconStream = Main.class.getResourceAsStream(relativePath);
                Image iconImage = new Image(iconStream);
                newStage.getIcons().add(iconImage);
                // Show the new stage
                newStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void removeProduct() {
        ObservableList<String> selected = tblCart.getSelectionModel().getSelectedItem();
        if(selected != null){
            int qt = Integer.parseInt(selected.get(5));
            String pid = selected.get(0);
            ObservableList<ObservableList<String>> data = tblCart.getItems();
            data.remove(selected);

            String sql = "Update stock SET Qty = Qty+? WHERE P_ID = ?";
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, qt);
                preparedStatement.setString(2, pid);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
//            try {
//                tblCart.getSelectionModel().clearSelection();
//            } catch (Exception e) {
//            }
            StockKeeperController.dbUpdate();
            loadFromDB();
        } else{
            showCustomDialog();
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



    @FXML
    void onBuyProduct(MouseEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectSupplierType.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene2 = new Scene(root);
            stage.setScene(scene2);
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
            scene2 = new Scene(root);
            stage.setScene(scene2);
            stage.setResizable(false);
            stage.show();
        } catch (NullPointerException e) {
        } catch (IOException e) {
        }
    }


    @FXML
    void onUpdateProducts(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/ManageProducts.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene2 = new Scene(root);
            stage.setScene(scene2);
            stage.setResizable(false);
            stage.show();
        } catch (NullPointerException e) {
        } catch (IOException e) {
        }
    }

}


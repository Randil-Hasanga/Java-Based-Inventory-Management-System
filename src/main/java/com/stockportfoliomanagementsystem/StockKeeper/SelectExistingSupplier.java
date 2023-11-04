package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class SelectExistingSupplier implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button btnSelect;
    @FXML
    private ImageView imageView;
    @FXML
    private TableView<ObservableList<String>> tblSuppliers;
    @FXML
    private Label txtName;
    private static String selectedIndex;
    protected static String supplierType;


    @FXML
    void onReportsButton(MouseEvent event) {
//===========================================================
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
    void onUpdateProducts(MouseEvent event) {
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
        }
    }


    private void setSupplierType(String existing) {
        this.supplierType = existing;
    }
    public static String getSupplierType() {
        return supplierType;
    }
    private void setSupplierIndex(String index) {
        this.selectedIndex = index;
    }
    public static String getSupplierIndex() {
        return selectedIndex;
    }
    public void loadFromDB() {
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblSuppliers.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Supplier Id","Name","Address","Contact"};

        double columnWidth = (tblSuppliers.getPrefWidth()) / (columnNames.length)-1;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT * FROM supplier"; // Replace with your table name
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

            tblSuppliers.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnSelect.setOnAction(event -> {
            try {
                selectSupplier(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void selectSupplier(ActionEvent event) throws IOException {
        ObservableList<String> index = tblSuppliers.getSelectionModel().getSelectedItem();

        if(index != null) {
            setSupplierIndex(index.get(0));
            setSupplierType("Existing");
            AddNewSupplier.supplierType = null;

            try {
                root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectPrductType.fxml"));
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
        }else{
            SellExisting sellExisting = new SellExisting();
            sellExisting.showCustomDialog();
        }
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
    void onBackButton(MouseEvent event){
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
        } catch (NullPointerException e) {
        }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
        tblSuppliers.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
    }
}

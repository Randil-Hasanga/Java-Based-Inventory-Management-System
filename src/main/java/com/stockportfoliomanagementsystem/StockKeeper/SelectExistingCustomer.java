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

public class SelectExistingCustomer implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private ImageView imageView;
    @FXML
    private Label txtName;
    @FXML
    private Button btnSelect;
    @FXML
    private TableView<ObservableList<String>> tblCustomers;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String selectedIndex;
    protected static String customerType;

    @FXML
    void onCustomersButton(MouseEvent event) {

    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) {

    }

    private void setCustomerType(String existing) {
        this.customerType = existing;
    }
    public static String getCustomerType() {
        return customerType;
    }

    private void setCustomerIndex(String index) {
        this.selectedIndex = index;
    }
    public static String getCustomerIndex() {
        return selectedIndex;
    }

    public void loadFromDB() {
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblCustomers.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Customer Id","Customer Name","Address","Contact"};

        double columnWidth = (tblCustomers.getPrefWidth()) / (columnNames.length)-1;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT * FROM customer"; // Replace with your table name
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

            tblCustomers.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnSelect.setOnAction(event -> {
            try {
                selectCustomer(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void selectCustomer(ActionEvent event) throws IOException {
        ObservableList<String> index = tblCustomers.getSelectionModel().getSelectedItem();

        if(index != null) {
            setCustomerIndex(index.get(0));
            setCustomerType("Existing");
            AddNewCustomer.customerType = null;

            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/BuyExisting.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else{
            BuyExisting buyExisting = new BuyExisting();
            buyExisting.showCustomDialog();
        }


    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
        tblCustomers.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
    }
}

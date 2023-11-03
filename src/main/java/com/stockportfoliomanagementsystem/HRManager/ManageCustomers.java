package com.stockportfoliomanagementsystem.HRManager;

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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageCustomers implements Initializable {

    private Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<ObservableList<String>> tblCustomers;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String cusID;

    private void setSelectedUser(String userId) {
        this.cusID = userId;
    }
    public static String getSelectedCustomer(){
        return cusID;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
        tblCustomers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void loadFromDB() {
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblCustomers.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Customer Id","Customer Name","Address","Contact Number"};

        double columnWidth = (tblCustomers.getPrefWidth()) / (columnNames.length)-1;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT C_ID, C_Name, C_Location, C_Contact FROM customer"; // Replace with your table name
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

        btnDelete.setOnAction(this::deleteSelectedRow);
    }

    private void deleteSelectedRow(ActionEvent event) {
        int selectedIndex = tblCustomers.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblCustomers.getItems().get(selectedIndex);
            String c_id = selectedRow.get(0); // Assuming User_Id is in the first column

            // Remove the selected row from the TableView
            tblCustomers.getItems().remove(selectedIndex);

            String deleteForeign = "DELETE FROM transactions_cus WHERE C_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteForeign);
                pstmt.setString(1, c_id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Delete the row from the database
            String deleteSQL = "DELETE FROM customer WHERE C_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
                pstmt.setString(1, c_id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No row selected");
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
    void onRefresh(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/ManageCustomers.fxml"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    void onManageSuppliers(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/ManageSuppliers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onAddBtnClick(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/AddCustomer.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/HRManagerDashboard.fxml"));
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


    @FXML
    void onUpdateButton(MouseEvent event) throws IOException {
        int selectedIndex = tblCustomers.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblCustomers.getItems().get(selectedIndex);
            String userId = selectedRow.get(0); // Assuming User_Id is in the first column
            setSelectedUser(userId);

            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/UpdateCustomer.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            showCustomDialog();
        }
    }
    @FXML
    void onStockButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/viewStock.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

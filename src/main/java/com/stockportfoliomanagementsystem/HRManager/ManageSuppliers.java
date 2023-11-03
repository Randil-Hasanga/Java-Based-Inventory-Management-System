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

public class ManageSuppliers implements Initializable {
    
    private Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<ObservableList<String>> tblSuppliers;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private static String supID;

    private void setSelectedUser(String userId) {
        this.supID = userId;
    }
    public static String getSelectedSupplier() {
        return supID;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
        tblSuppliers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void loadFromDB() {
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblSuppliers.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Supplier Id","Supplier Name","Address","Contact Number"};

        double columnWidth = (tblSuppliers.getPrefWidth()) / (columnNames.length)-1;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT S_ID, S_Name, S_Location, S_Contact FROM supplier"; // Replace with your table name
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

        btnDelete.setOnAction(this::deleteSelectedRow);
    }

    private void deleteSelectedRow(ActionEvent event) {
        int selectedIndex = tblSuppliers.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblSuppliers.getItems().get(selectedIndex);
            String s_id = selectedRow.get(0); // Assuming User_Id is in the first column

            // Remove the selected row from the TableView
            tblSuppliers.getItems().remove(selectedIndex);

            String deleteForeign = "DELETE FROM stock WHERE S_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteForeign);
                pstmt.setString(1, s_id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String deleteForeign2 = "DELETE FROM transactions_sup WHERE S_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteForeign2);
                pstmt.setString(1, s_id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Delete the row from the database
            String deleteSQL = "DELETE FROM supplier WHERE S_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
                pstmt.setString(1, s_id);
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
    void onAddBtnClick(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/AddSupplier.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onBackButton(MouseEvent event) {
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
    void onRefresh(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/ManageSuppliers.fxml"));

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
    void onManageCustomers(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/ManageCustomers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    void onStockButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/viewStock.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Stock button er");
        }
    }

    @FXML
    void onUpdateButton(MouseEvent event) throws IOException {
        int selectedIndex = tblSuppliers.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblSuppliers.getItems().get(selectedIndex);
            String userId = selectedRow.get(0); // Assuming User_Id is in the first column
            setSelectedUser(userId);

            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/UpdateSupplier.fxml"));
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
}

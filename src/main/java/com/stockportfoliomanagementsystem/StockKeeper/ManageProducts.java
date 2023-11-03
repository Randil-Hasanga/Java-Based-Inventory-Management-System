package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageProducts implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private Button btnDelete;

    @FXML
    private TableView<ObservableList<String>> tblProducts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDelete.setOnAction(this::deleteSelectedRow);
        tblProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadFromDB();
    }

    private void deleteSelectedRow(ActionEvent event) {

        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
        confirmationDialog.setTitle("Warning !");
        confirmationDialog.setHeaderText("Do you want to continue?");
        confirmationDialog.setContentText("If you delete this Product,\n" +
                "it will delete from the entire Database including transactions !!!\n" +
                "Recommend - Delete after a report generation.");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel");

        confirmationDialog.getButtonTypes().setAll(okButton, cancelButton);

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {

                System.out.println("OK button clicked.");
                int selectedIndex = tblProducts.getSelectionModel().getSelectedIndex();

                if (selectedIndex >= 0) {
                    ObservableList<String> selectedRow = tblProducts.getItems().get(selectedIndex);
                    String p_id = selectedRow.get(0); // Assuming User_Id is in the first column

                    // Remove the selected row from the TableView
                    tblProducts.getItems().remove(selectedIndex);

                    String deleteForeign = "DELETE FROM transactions_cus WHERE P_ID = ?";
                    try {
                        PreparedStatement pstmt = conn.prepareStatement(deleteForeign);
                        pstmt.setString(1, p_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String deleteForeign2 = "DELETE FROM transactions_sup WHERE P_ID = ?";
                    try {
                        PreparedStatement pstmt = conn.prepareStatement(deleteForeign2);
                        pstmt.setString(1, p_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Delete the row from the database
                    String deleteSQL = "DELETE FROM stock WHERE P_ID = ?";
                    try {
                        PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
                        pstmt.setString(1, p_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Row deleted");
                } else {
                    System.out.println("No row selected");
                    showCustomDialog();
                }
            } else if (response == cancelButton) {
                System.out.println("Cancel button clicked.");
            }
        });

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
    }

    @FXML
    void onBackButton(MouseEvent event) {

    }

    @FXML
    void onBuyProduct(MouseEvent event) {

    }

    @FXML
    void onRefresh(MouseEvent event) {

    }

    @FXML
    void onSellProducts(MouseEvent event) {

    }

    @FXML
    void onUpdateButton(MouseEvent event) {

    }
}

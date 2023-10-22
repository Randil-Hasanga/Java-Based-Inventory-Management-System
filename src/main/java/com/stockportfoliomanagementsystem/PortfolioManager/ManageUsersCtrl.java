package com.stockportfoliomanagementsystem.PortfolioManager;

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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageUsersCtrl implements Initializable {
    Connection conn = MySqlCon.MysqlMethod();

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<ObservableList<String>> tblUsers;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
        tblUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void loadFromDB() {
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblUsers.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"User Id","Username","First Name","Last name","NIC","Position","Contact"};

        double columnWidth = (tblUsers.getPrefWidth()) / (columnNames.length)-1;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT User_Id, Username, FName, Lname, NIC, Position, Contact FROM users"; // Replace with your table name
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

            tblUsers.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnDelete.setOnAction(this::deleteSelectedRow);
    }

    private void deleteSelectedRow(ActionEvent event) {
        int selectedIndex = tblUsers.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblUsers.getItems().get(selectedIndex);
            String userId = selectedRow.get(0); // Assuming User_Id is in the first column

            // Remove the selected row from the TableView
            tblUsers.getItems().remove(selectedIndex);

            // Delete the row from the database
            String deleteSQL = "DELETE FROM users WHERE User_Id = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
                pstmt.setString(1, userId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Inform the user that no row is selected
            // You can display a dialog or a message to indicate this.
        }
    }

    @FXML
    void onRefresh(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/ManageUsers.fxml"));
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
    void onBackButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManagerDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onAddBtnClick(MouseEvent event) {
        try {
            // Load the FXML file for the new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stockportfoliomanagementsystem/AddUser.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage addItemStage = new Stage();

            // Set the FXML content as the scene for the new stage
            Scene scene = new Scene(root);
            addItemStage.setScene(scene);

            // Set the title for the new stage
            addItemStage.setTitle("Add New User");

            // Show the new stage
            addItemStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

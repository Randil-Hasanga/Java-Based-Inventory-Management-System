package com.stockportfoliomanagementsystem.AccountingManager;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.awt.*;

public class ManageReports implements Initializable {
    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private TableView<ObservableList<String>> tblReports;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    InputStream pdfInputStream;

    @FXML
    void onDeleteButton(MouseEvent event) {
        int selectedIndex = tblReports.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblReports.getItems().get(selectedIndex);
            String r_id = selectedRow.get(0);

            tblReports.getItems().remove(selectedIndex);

            String sql = "DELETE FROM report WHERE R_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,r_id);
                pstmt.executeUpdate();
                System.out.println("Row deleted");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onOpenButton(MouseEvent event) {
        int selectedIndex = tblReports.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblReports.getItems().get(selectedIndex);
            String r_id = selectedRow.get(0);

            String sql = "SELECT pdf FROM report WHERE R_ID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,r_id);
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()){
                    pdfInputStream = rs.getBinaryStream("pdf");
                }
                showPDF(pdfInputStream);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void showPDF(InputStream pdfInputStream) {
        try {
            Path tempPdfFile = Files.createTempFile("temp_pdf_", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempPdfFile.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfInputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            // Open the temporary PDF file with the default PDF viewer
            Desktop.getDesktop().open(tempPdfFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRefresh(MouseEvent event) {
        loadFromDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblReports.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadFromDB();

    }

    private void loadFromDB(){
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblReports.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Report ID","Date"};

        double columnWidth = tblReports.getPrefWidth() / columnNames.length;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT R_ID, Date_ FROM report";
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

            tblReports.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCustomers(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/AccountingManager/ShowCustomers.fxml"));
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
    void onStock(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/AccountingManager/ShowStock.fxml"));
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
    void onSuppliers(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/AccountingManager/ShowSuppliers.fxml"));
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
    void onLogOutButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/Main.fxml"));
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
    void onBackButton(MouseEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/AccountingManager/AccountingManagerDashboard.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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

}

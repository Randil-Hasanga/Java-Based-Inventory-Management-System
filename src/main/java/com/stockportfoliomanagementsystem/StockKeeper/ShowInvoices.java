package com.stockportfoliomanagementsystem.StockKeeper;

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

import java.awt.*;
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

public class ShowInvoices implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableView<ObservableList<String>> tblInvoices;
    private InputStream pdfInputStream;

    @FXML
    void onBackButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/StockKeeperDashboard.fxml"));
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

    @FXML
    void onBuyProduct(MouseEvent event) throws IOException {
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
            throw new IOException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onDeleteButton(MouseEvent event) {
        int selectedIndex = tblInvoices.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblInvoices.getItems().get(selectedIndex);
            String r_id = selectedRow.get(0);

            tblInvoices.getItems().remove(selectedIndex);

            String sql = "DELETE FROM PDF_invoices WHERE invoice_id = ?";
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
        int selectedIndex = tblInvoices.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<String> selectedRow = tblInvoices.getItems().get(selectedIndex);
            String r_id = selectedRow.get(0);

            String sql = "SELECT pdf FROM PDF_invoices WHERE invoice_id = ?";
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

    private void loadFromDB(){
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblInvoices.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Invoice ID", "Date", "Customer ID"};

        double columnWidth = tblInvoices.getPrefWidth() / columnNames.length;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT invoice_id, date_,C_ID  FROM PDF_invoices";
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

            tblInvoices.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRefresh(MouseEvent event) {
        loadFromDB();
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
    void onSupplierButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewSuppliers.fxml"));
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblInvoices.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadFromDB();
    }
}

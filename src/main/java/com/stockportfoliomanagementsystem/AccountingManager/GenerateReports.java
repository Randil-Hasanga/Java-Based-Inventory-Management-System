package com.stockportfoliomanagementsystem.AccountingManager;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateReports implements Initializable {
    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private DatePicker dateChooser;
    @FXML
    private Label lblTotulPurchase;
    @FXML
    private Label lblOnThisDay;

    @FXML
    private TableView<ObservableList<String>> tblPurchased;

    @FXML
    private TableView<ObservableList<String>> tblSelld;

    @FXML
    private Label txtReportID;
    @FXML
    private Label lblTotalExpenses;

    @FXML
    private Label lblTotalProfit;
    @FXML
    private Label lblTotalRevenue;

    private LocalDate selectedDate;
    private String dateString;
    @FXML
    private Label lblProfitSum;

    @FXML
    private Label lblTotalSum;
    private int numericId;
    private String max;
    private String pdfFilename;
    private String pdfFilePath;
    private FileInputStream fis;
    private byte[] pdf;



    @FXML
    void onBtnPDF(MouseEvent event) {

        captureScne();
        showPDF();

        String sql1 = "SELECT MAX(R_ID) FROM report";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                max = rs.getString(1);
                System.out.println("Last : "+max);
            }
            Pattern pattern = Pattern.compile("\\d+");

            // Use a Matcher to find the numeric part
            if(max == null){
                max = "R_000";
            }
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

        if(numericId == 0){
            txtReportID.setText("R_001");
        }else if(numericId < 9) {
            txtReportID.setText("R_00" + (numericId + 1));
        }else if(numericId < 99){
            txtReportID.setText("R_0" + (numericId + 1));
        }else{
            txtReportID.setText("R_" + (numericId + 1));
        }

    }
    @FXML
    void onGenerateButton(MouseEvent event) {
        selectedDate = dateChooser.getValue();

        if (selectedDate != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            dateString = selectedDate.format(dateFormatter);

            System.out.println("Selected date as string: " + dateString);
//=======================================================================================================================


            ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblSelld.getColumns();
            columns.clear();

            // Define fixed column names
            String[] columnNames = {"Product Id","Supplier ID","Product Name","Price","Profit per Unit","Quantity","Total Profit","Total"};

            double columnWidth = (tblSelld.getPrefWidth()) / (columnNames.length)-2;

            // Add the columns to the TableView with fixed names
            for (int i = 0; i < columnNames.length; i++) {
                final int columnIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
                column.setPrefWidth(columnWidth);
                columns.add(column);
            }

            String sqlQuery = "SELECT " +
                    "stock.P_ID, " +
                    "stock.S_ID," +
                    "stock.P_Name, " +
                    "stock.Selling_price, " +
                    "(stock.Selling_price - stock.Price_taken) AS profit_per_Unit, " +
                    "SUM(transactions_cus.Qty) AS total_quantity_sold, " +
                    "(stock.Selling_price - stock.Price_taken) * SUM(transactions_cus.Qty) AS Total," +
                    "stock.Selling_price* SUM(transactions_cus.Qty) AS Total " +
                    "FROM " +
                    "stock " +
                    "JOIN " +
                    "transactions_cus ON transactions_cus.P_ID = stock.P_ID " +
                    "WHERE transactions_cus.Date_ = ? " +
                    "GROUP BY " +
                    "stock.P_ID, stock.P_Name, stock.Selling_price, stock.Price_taken, stock.S_ID";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setDate(1, Date.valueOf(dateString));
                ResultSet rs = pstmt.executeQuery();

                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= columnNames.length; i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }

                tblSelld.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ObservableList<TableColumn<ObservableList<String>, ?>> columns2 = tblPurchased.getColumns();
            columns2.clear();

            // Define fixed column names
            String[] columnNames2 = {"Product Id","Supplier ID","Product Name","Price taken","Quantity","Total"};

            double columnWidth2 = (tblSelld.getPrefWidth()) / (columnNames2.length)-2;

            // Add the columns to the TableView with fixed names
            for (int i = 0; i < columnNames2.length; i++) {
                final int columnIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames2[i]);
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
                column.setPrefWidth(columnWidth2);
                columns2.add(column);
            }

            String sqlQuery2 = "SELECT " +
                    "stock.P_ID, " +
                    "stock.S_ID, " +
                    "stock.P_Name, " +
                    "stock.Price_taken, " +
                    "SUM(transactions_sup.Qty) AS total_quantity_bought, " +
                    "(stock.Price_taken) * SUM(transactions_sup.Qty) AS Total " +
                    "FROM " +
                    "stock " +
                    "JOIN " +
                    "transactions_sup ON transactions_sup.P_ID = stock.P_ID " +
                    "WHERE transactions_sup.Date_ = ? " + // Filter by date
                    "GROUP BY " +
                    "stock.P_ID, stock.P_Name, stock.Price_taken, stock.S_ID;";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery2);
                pstmt.setDate(1, Date.valueOf(dateString));
                ResultSet rs = pstmt.executeQuery();

                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= columnNames2.length; i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }

                tblPurchased.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sqlQuery3 = "SELECT " +
                    "SUM((stock.Selling_price - stock.Price_taken) * transactions_cus.Qty) AS TotalProfit, " +
                    "SUM(stock.Selling_price * transactions_cus.Qty) AS Total " +
                    "FROM stock " +
                    "JOIN transactions_cus ON transactions_cus.P_ID = stock.P_ID " +
                    "WHERE transactions_cus.Date_ = ?";

            try {
                PreparedStatement pstmt3 = conn.prepareStatement(sqlQuery3);
                pstmt3.setDate(1, Date.valueOf(dateString));
                ResultSet rs3 = pstmt3.executeQuery();

                while (rs3.next()) {
                    lblProfitSum.setText(rs3.getString(1));
                    lblTotalSum.setText(rs3.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String sqlQuery4 = "SELECT " +
                    "SUM((stock.Price_taken)*(transactions_sup.Qty)) AS Total " +
                    "FROM " +
                    "stock " +
                    "JOIN " +
                    "transactions_sup ON transactions_sup.P_ID = stock.P_ID " +
                    "WHERE transactions_sup.Date_ = ?";

            try {
                PreparedStatement pstmt4 = conn.prepareStatement(sqlQuery4);
                pstmt4.setDate(1, Date.valueOf(dateString));
                ResultSet rs4 = pstmt4.executeQuery();

                while (rs4.next()) {
                    lblTotulPurchase.setText(rs4.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            lblOnThisDay.setText("On "+dateString);
            lblTotalProfit.setText(lblProfitSum.getText());
            lblTotalRevenue.setText(lblTotalSum.getText());
            lblTotalExpenses.setText(lblTotulPurchase.getText());

        } else {
            System.out.println("No date selected.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql1 = "SELECT MAX(R_ID) FROM report";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                max = rs.getString(1);
                System.out.println("Last : "+max);
            }
            Pattern pattern = Pattern.compile("\\d+");

            // Use a Matcher to find the numeric part
            if(max == null){
                max = "R_000";
            }
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

        if(numericId == 0){
            txtReportID.setText("R_001");
        }else if(numericId < 9) {
            txtReportID.setText("R_00" + (numericId + 1));
        }else if(numericId < 99){
            txtReportID.setText("R_0" + (numericId + 1));
        }else{
            txtReportID.setText("R_" + (numericId + 1));
        }

        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblSelld.getColumns();
        columns.clear();

        // Define fixed column names
        String[] columnNames = {"Product Id","Supplier ID","Product Name","Price","Profit per Unit","Quantity","Total Profit","Total"};

        double columnWidth = (tblSelld.getPrefWidth()) / (columnNames.length)-2;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sqlQuery = "SELECT " +
                "stock.P_ID, " +
                "stock.S_ID," +
                "stock.P_Name, " +
                "stock.Selling_price, " +
                "(stock.Selling_price - stock.Price_taken) AS profit_per_Unit, " +
                "SUM(transactions_cus.Qty) AS total_quantity_sold, " +
                "(stock.Selling_price - stock.Price_taken) * SUM(transactions_cus.Qty) AS Total," +
                "stock.Selling_price* SUM(transactions_cus.Qty) AS Total " +
                "FROM " +
                "stock " +
                "JOIN " +
                "transactions_cus ON transactions_cus.P_ID = stock.P_ID " +
                "WHERE transactions_cus.Date_ = ? " +
                "GROUP BY " +
                "stock.P_ID, stock.P_Name, stock.Selling_price, stock.Price_taken, stock.S_ID;";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnNames.length; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tblSelld.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<TableColumn<ObservableList<String>, ?>> columns2 = tblPurchased.getColumns();
        columns2.clear();

        // Define fixed column names
        String[] columnNames2 = {"Product Id","Supplier ID","Product Name","Price taken","Quantity","Total"};

        double columnWidth2 = (tblSelld.getPrefWidth()) / (columnNames2.length)-2;

        // Add the columns to the TableView with fixed names
        for (int i = 0; i < columnNames2.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames2[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth2);
            columns2.add(column);
        }

        String sqlQuery2 = "SELECT " +
                "stock.P_ID, " +
                "stock.S_ID, " +
                "stock.P_Name, " +
                "stock.Price_taken, " +
                "SUM(transactions_sup.Qty) AS total_quantity_bought, " +
                "(stock.Price_taken) * SUM(transactions_sup.Qty) AS Total " +
                "FROM " +
                "stock " +
                "JOIN " +
                "transactions_sup ON transactions_sup.P_ID = stock.P_ID " +
                "WHERE transactions_sup.Date_ = ? " + // Filter by date
                "GROUP BY " +
                "stock.P_ID, stock.P_Name, stock.Price_taken, stock.S_ID;";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery2);
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnNames2.length; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tblPurchased.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlQuery3 = "SELECT " +
                "SUM((stock.Selling_price - stock.Price_taken) * transactions_cus.Qty) AS TotalProfit, " +
                "SUM(stock.Selling_price * transactions_cus.Qty) AS Total " +
                "FROM stock " +
                "JOIN transactions_cus ON transactions_cus.P_ID = stock.P_ID " +
                "WHERE transactions_cus.Date_ = ?";

        try {
            PreparedStatement pstmt3 = conn.prepareStatement(sqlQuery3);
            pstmt3.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs3 = pstmt3.executeQuery();

            while (rs3.next()) {
                lblProfitSum.setText(rs3.getString(1));
                lblTotalSum.setText(rs3.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sqlQuery4 = "SELECT " +
                "SUM((stock.Price_taken)*(transactions_sup.Qty)) AS Total " +
                "FROM " +
                "stock " +
                "JOIN " +
                "transactions_sup ON transactions_sup.P_ID = stock.P_ID " +
                "WHERE transactions_sup.Date_ = ?";

        try {
            PreparedStatement pstmt4 = conn.prepareStatement(sqlQuery4);
            pstmt4.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs4 = pstmt4.executeQuery();

            while (rs4.next()) {
                lblTotulPurchase.setText(rs4.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        lblOnThisDay.setText("On "+LocalDate.now());
        lblTotalProfit.setText(lblProfitSum.getText());
        lblTotalRevenue.setText(lblTotalSum.getText());
        lblTotalExpenses.setText(lblTotulPurchase.getText());
    }

    private void captureScne() {
        String imgFilename = "img_"+ LocalDate.now()+".png";
        pdfFilename = "I_"+(numericId+1)+"_"+ LocalDate.now()+".pdf";

        Screen primaryScreen = Screen.getPrimary();

        double screenWidth = primaryScreen.getBounds().getWidth();
        double screenHeight = primaryScreen.getBounds().getHeight();

        System.out.println(screenWidth);
        System.out.println(screenHeight);
        // Calculate the center position
        double centerX = screenWidth / 2;
        double centerY = screenHeight / 2;

        double boundX = centerX - (850 / 2);
        double boundY = centerY - (820 / 2);

        try{
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle((int) boundX+7, (int) boundY+30,850,820);
            BufferedImage bi = robot.createScreenCapture(rectangle);
            ImageIO.write(bi, "png", new File("src/main/java/com/stockportfoliomanagementsystem/Reports/png/"+ imgFilename));

        } catch (AWTException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String imageFilePath = "src/main/java/com/stockportfoliomanagementsystem/Reports/png/"+ imgFilename;
        pdfFilePath = "src/main/java/com/stockportfoliomanagementsystem/Reports/pdf/"+ pdfFilename;

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imageFilePath));
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();

            page.setMediaBox(new PDRectangle(850, 820));
            document.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pdImage, 0,0);
            contentStream.close();

            try(OutputStream os = new FileOutputStream(pdfFilePath)) {
                document.save(os);
            }
            document.close();

            try{
                fis = new FileInputStream(pdfFilePath);
                pdf = new byte[fis.available()];
                fis.read(pdf);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String sql = "INSERT INTO report (R_ID, date_,pdf) VALUES (?,?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, numericId+1);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setBytes(3, pdf);

            pstmt.executeUpdate();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPDF() {
        try {
            InputStream pdfInputStream = new FileInputStream(pdfFilePath);
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
}

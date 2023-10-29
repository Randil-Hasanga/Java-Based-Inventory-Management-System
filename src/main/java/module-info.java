module com.stockportfoliomanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;
    requires org.apache.pdfbox;


    opens com.stockportfoliomanagementsystem to javafx.fxml;
    exports com.stockportfoliomanagementsystem;
    exports com.stockportfoliomanagementsystem.PortfolioManager;
    opens com.stockportfoliomanagementsystem.PortfolioManager to javafx.fxml;
    exports com.stockportfoliomanagementsystem.HRManager;
    opens com.stockportfoliomanagementsystem.HRManager to javafx.fxml;
    exports com.stockportfoliomanagementsystem.AccountingManager;
    opens com.stockportfoliomanagementsystem.AccountingManager to javafx.fxml;
    exports com.stockportfoliomanagementsystem.StockKeeper;
    opens com.stockportfoliomanagementsystem.StockKeeper to javafx.fxml;
}
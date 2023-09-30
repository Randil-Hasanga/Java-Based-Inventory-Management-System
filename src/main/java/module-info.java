module com.example.stockportfoliomanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.stockportfoliomanagementsystem to javafx.fxml;
    exports com.stockportfoliomanagementsystem;
    exports com.stockportfoliomanagementsystem.PortfolioManager;
    opens com.stockportfoliomanagementsystem.PortfolioManager to javafx.fxml;
}
module Menaxhimi.i.sistemit.te.fluturimeve {
    // SQL
    requires java.sql;

    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires javafx.graphics;

    opens controllers to javafx.fxml;
    opens models.dto to javafx.base;

    exports app;
}
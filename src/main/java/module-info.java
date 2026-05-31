module Menaxhimi.i.sistemit.te.fluturimeve {
    // SQL
    requires java.sql;

    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires javafx.graphics;

    exports app;

    opens controllers to javafx.fxml;
}
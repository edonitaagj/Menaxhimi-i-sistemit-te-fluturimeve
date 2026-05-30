module Menaxhimi.i.sistemit.te.fluturimeve {
    // SQL
    requires java.sql;

    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;

    exports app;

    opens controllers to javafx.fxml;
}
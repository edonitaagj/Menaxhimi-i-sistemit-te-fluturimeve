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
    opens models.dto to javafx.base;


    // Lejon JavaFX të lexojë skedarët message_sq dhe message_en brenda folderit i18n
    opens i18n;
}
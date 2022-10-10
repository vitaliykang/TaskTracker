module com.sunmyoung.tasktracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    requires lombok;


    requires org.controlsfx.controls;

    opens com.sunmyoung.tasktracker to javafx.fxml;
    opens com.sunmyoung.tasktracker.controllers to javafx.fxml;
    opens com.sunmyoung.tasktracker.controllers.dialogControllers to javafx.fxml;
    opens com.sunmyoung.tasktracker.pojos;

    exports com.sunmyoung.tasktracker;
    exports com.sunmyoung.tasktracker.pojos;
    exports com.sunmyoung.tasktracker.controllers;
}
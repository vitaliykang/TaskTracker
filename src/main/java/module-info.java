module com.sunmyoung.tasktracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    requires lombok;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;


    opens com.sunmyoung.task_tracker to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers.dialogControllersD to javafx.fxml;
    opens com.sunmyoung.task_tracker.pojos;

    exports com.sunmyoung.task_tracker;
    exports com.sunmyoung.task_tracker.pojos;
    exports com.sunmyoung.task_tracker.controllers;
}
module com.sunmyoung.tasktracker {
    requires jakarta.persistence;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires lombok;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires org.kordamp.ikonli.bootstrapicons;
    requires javafx.fxml;



    opens com.sunmyoung.task_tracker to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers.dialogControllers to javafx.fxml;
    opens com.sunmyoung.task_tracker.pojos;

    exports com.sunmyoung.task_tracker;
    exports com.sunmyoung.task_tracker.pojos;
    exports com.sunmyoung.task_tracker.controllers;
}
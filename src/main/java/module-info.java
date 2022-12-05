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
    requires org.kordamp.ikonli.bootstrapicons;
    requires javafx.fxml;
    requires org.apache.commons.lang3;


    opens com.sunmyoung.task_tracker to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers to javafx.fxml;
    opens com.sunmyoung.task_tracker.pojos;

    exports com.sunmyoung.task_tracker;
    exports com.sunmyoung.task_tracker.pojos;
    exports com.sunmyoung.task_tracker.controllers;
    opens com.sunmyoung.task_tracker.controllers.dialogControllers.client to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers.dialogControllers.code to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers.dialogControllers.utility to javafx.fxml;
    opens com.sunmyoung.task_tracker.controllers.dialogControllers.order to javafx.fxml;
}
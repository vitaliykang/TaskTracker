package com.sunmyoung.tasktracker.controllers;


import com.sunmyoung.tasktracker.pojos.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ArchiveController {

    @FXML
    private TableColumn<Task, String> clientCol;

    @FXML
    private TableColumn<?, ?> combiCol;

    @FXML
    private TableColumn<?, ?> countCol;

    @FXML
    private TableColumn<?, ?> dateInCol;

    @FXML
    private TableColumn<?, ?> dateOutCol;

    @FXML
    private TableColumn<?, ?> filmTypeCol;

    @FXML
    private TableColumn<?, ?> frameNewOldCol;

    @FXML
    private TableColumn<?, ?> frameSizeCol;

    @FXML
    private TableColumn<?, ?> meshCol;

    @FXML
    private TableColumn<?, ?> orderCol;

    @FXML
    private TableColumn<?, ?> shippingMethodCol;

    @FXML
    private TableView<?> tasksTableView;

}

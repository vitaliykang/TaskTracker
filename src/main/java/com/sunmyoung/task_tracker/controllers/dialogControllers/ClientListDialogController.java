package com.sunmyoung.task_tracker.controllers.dialogControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ClientListDialogController {

    @FXML
    private TableColumn<?, ?> clientCol;

    @FXML
    private TableView<?> clientsTableView;

    @FXML
    private TableColumn<?, ?> codeCol;

    @FXML
    private TextField codeSearchTF;

    @FXML
    private TableColumn<?, ?> managerCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    void addClient(ActionEvent event) {

    }

    @FXML
    void deleteClient(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

    @FXML
    void search(KeyEvent event) {

    }

}

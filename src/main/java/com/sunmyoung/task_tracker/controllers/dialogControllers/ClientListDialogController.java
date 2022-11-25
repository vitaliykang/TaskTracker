package com.sunmyoung.task_tracker.controllers.dialogControllers;

import com.sunmyoung.task_tracker.pojos.Client;
import com.sunmyoung.task_tracker.pojos.Model;
import com.sunmyoung.task_tracker.repositories.ClientRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class ClientListDialogController {

    @FXML
    private TableColumn<Client, String>
            codeCol,
            clientCol,
            typeCol,
            managerCol;

    @FXML
    private TableView<Client> clientsTableView;

    @FXML
    private TextField textField;

    @Getter
    private Client selectedClient;

    private final ObservableList<Client> content = FXCollections.observableArrayList();
    private final FilteredList<Client> filteredContent = new FilteredList<>(content, predicate -> true);

    @FXML
    void selectClient(MouseEvent event) {
        Client selectedClient = clientsTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            this.selectedClient = selectedClient;
        }
    }

    @FXML
    void addClient(ActionEvent event) {

    }

    @FXML
    void deleteClient(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {
        filter();
    }

    @FXML
    void search(KeyEvent event) {
        filter();
    }

    public void initialize() {
        initTableView();
        List<Client> clients = ClientRepository.findAll();
        Collections.sort(clients);
        content.addAll(clients);
    }

    private void filter() {
        String input = textField.getText();
        if (input == null || input.length() == 0) {
            filteredContent.setPredicate(predicate -> true);
        } else {
            filteredContent.setPredicate(predicate -> predicate.getCode().contains(input) || predicate.getClient().contains(input));
        }
    }

    private void initTableView() {
        clientsTableView.setItems(filteredContent);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("manager"));
    }

    private void edit() {

    }
}

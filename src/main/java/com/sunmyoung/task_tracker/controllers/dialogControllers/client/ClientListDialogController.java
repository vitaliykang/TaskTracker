package com.sunmyoung.task_tracker.controllers.dialogControllers.client;

import com.sunmyoung.task_tracker.Dialogs;
import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.controllers.dialogControllers.utility.ConfirmationDialogController;
import com.sunmyoung.task_tracker.pojos.Client;
import com.sunmyoung.task_tracker.repositories.ClientRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private boolean listChanged = false;

    @Getter
    private Client selectedClient;

    @Getter
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
    @SneakyThrows
    void addClient(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_CLIENT));
        Dialog<ButtonType> dialog = new Dialog<>();
        DialogPane dialogPane = fxmlLoader.load();
        dialog.setDialogPane(dialogPane);
        CreateClientDialogController controller = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            Client client = new Client();
            client.setClient(controller.getCompanyTF().getText());
            client.setType(controller.getTypeTF().getText());
            client.setManager(controller.getManagerTF().getText());
            client.setCode(controller.getCodeTF().getText());
            client = ClientRepository.save(client);
            content.add(client);
            listChanged = true;
        }
    }

    @FXML
    @SneakyThrows
    void editClient(ActionEvent event) {
        if (selectedClient != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_CLIENT));
            Dialog<ButtonType> dialog = new Dialog<>();
            DialogPane dialogPane = fxmlLoader.load();
            dialog.setDialogPane(dialogPane);
            CreateClientDialogController controller = fxmlLoader.getController();

            //populate window
            controller.getCompanyTF().setText(selectedClient.getClient());
            controller.getTypeTF().setText(selectedClient.getType());
            controller.getManagerTF().setText(selectedClient.getManager());
            controller.getCodeTF().setText(selectedClient.getCode());

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                if (!selectedClient.getClient().equals(controller.getCompanyTF().getText()) ||
                    !selectedClient.getType().equals(controller.getTypeTF().getText()) ||
                    !selectedClient.getManager().equals(controller.getManagerTF().getText()) ||
                    !selectedClient.getCode().equals(controller.getCodeTF().getText())
                ) {
                    Client updatedClient = new Client();
                    updatedClient.setId(selectedClient.getId());
                    updatedClient.setClient(controller.getCompanyTF().getText());
                    updatedClient.setType(controller.getTypeTF().getText());
                    updatedClient.setManager(controller.getManagerTF().getText());
                    updatedClient.setCode(controller.getCodeTF().getText());
                    ClientRepository.update(updatedClient);

                    content.remove(selectedClient);
                    content.add(updatedClient);
                    Collections.sort(content);

                    listChanged = true;
                }
            }
        }
    }

    @FXML
    @SneakyThrows
    void deleteClient(ActionEvent event) {
        if (selectedClient != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CONFIRMATION));
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(fxmlLoader.load());

            ConfirmationDialogController controller = fxmlLoader.getController();
            controller.getMessageLabel().setText("고객을 삭제하시겠습니까?");
            controller.getInfoLabel().setText(selectedClient.toString());

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                ClientRepository.delete(selectedClient.getId());
                content.remove(selectedClient);
                listChanged = true;
            }
        }
    }

    @FXML
    void manualFilter(ActionEvent event) {
        filter();
    }

    @FXML
    void autoFilter(KeyEvent event) {
        filter();
    }

    public void initialize() {
        initTableView();
        List<Client> clients = loadClientsFromFile();
        Collections.sort(clients);
        content.addAll(clients);
    }

    public boolean listChanged() {
        return listChanged;
    }

    private List<Client> loadClientsFromFile() {
        List<Client> clients = new ArrayList<>();
        List<String> content = Utilities.readFromFile("data/clients.txt");
        content.forEach(entry -> {
            String[] clientArr = entry.split(" :: ");
            if (clientArr.length == 5) {
                Client client = new Client(clientArr);
                clients.add(client);
            } else {
                System.out.println(entry + ": is compromised.");
            }
        });
        return clients;
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
}

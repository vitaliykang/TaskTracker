package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.Dialogs;
import com.sunmyoung.task_tracker.ErrorMessage;
import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.controllers.dialogControllers.code.CodeSearchDialogController;
import com.sunmyoung.task_tracker.controllers.dialogControllers.code.CreateNewCodeDialogController;
import com.sunmyoung.task_tracker.pojos.Code;
import com.sunmyoung.task_tracker.repositories.CodeRepository;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import jakarta.persistence.EntityManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Optional;

public class StockController {

    @FXML
    private TextField
            codeTF,
            clientTF,
            frameSizeTF,
            meshTF;

    @FXML
    private TableColumn<Code, String>
            orderCol,
            codeCol,
            clientCol,
            frameSizeCol,
            meshCol,
            combiCol,
            meshSizeCol,
            countCol;

    @FXML
    private TableView<Code> stockTableView;

    private final ObservableList<Code> observableList = FXCollections.observableArrayList();
    private final FilteredList<Code> filteredList = new FilteredList<>(observableList, predicate -> true);

    @FXML
    private void filter() {
        if (fieldsEmpty()) {
            filteredList.setPredicate(predicate -> true);
        } else {
            filteredList.setPredicate(predicate ->
                    StringUtils.containsIgnoreCase(predicate.getCode(), codeTF.getText())
                            && StringUtils.containsIgnoreCase(predicate.getClient(), clientTF.getText())
                            && StringUtils.containsIgnoreCase(predicate.getFrameSize(), frameSizeTF.getText())
                            && StringUtils.containsIgnoreCase(predicate.getMesh(), meshTF.getText()) );
        }
    }

    @FXML
    void add() {
        CodeSearchDialogController.createNewCodeDialog();
    }

    @FXML
    void edit(ActionEvent event) {
        edit();
    }

    @FXML
    void delete() {
        Code selectedCode = stockTableView.getSelectionModel().getSelectedItem();
        if (selectedCode != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(Main.getLogo());

            alert.setTitle("코드를 삭제합니다.");
            alert.setHeaderText("코드를 삭제하시겠습니까?");
            alert.setContentText(selectedCode.toString());

            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
                try {
                    entityManager.getTransaction().begin();
                    entityManager.createQuery("delete Code c where c.id = :id").setParameter("id", selectedCode.getId()).executeUpdate();
                    entityManager.getTransaction().commit();
                    Utilities.printStatus("Code deleted", this.getClass());
                    loadInfo();
                } catch (Exception e) {
                    ErrorMessage.show(e);
                    e.printStackTrace();
                    entityManager.getTransaction().rollback();
                } finally {
                    entityManager.close();
                }
            }
        }
    }

    @FXML
    void mainMenu(ActionEvent event) {
        MainV2Controller.showMainScreen(event);
    }

    public void initialize() {
        initTableView();
        makeCountEditable();
    }

    private void initTableView() {
        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(filteredList.indexOf(order.getValue()) + 1)));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        frameSizeCol.setCellValueFactory(new PropertyValueFactory<>("frameSize"));
        meshCol.setCellValueFactory(new PropertyValueFactory<>("mesh"));
        combiCol.setCellValueFactory(new PropertyValueFactory<>("combi"));
        meshSizeCol.setCellValueFactory(new PropertyValueFactory<>("meshSize"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        loadInfo();
        stockTableView.setItems(filteredList);

        //making tableView editable and activating single cell selection
        stockTableView.getSelectionModel().setCellSelectionEnabled(true);
        stockTableView.setEditable(true);
    }

    private void loadInfo() {
        observableList.clear();
        observableList.addAll(CodeRepository.findAll());
        Collections.sort(observableList);
    }

    private void makeCountEditable() {
        countCol.setEditable(true);
        countCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countCol.setOnEditCommit(event -> {
            Code selectedCode = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(Main.getLogo());

                alert.setTitle("카운트를 변경합니다.");
                alert.setHeaderText("카운트를 바꾸시겠습니까?");
                alert.setContentText(String.format("새 값: %s", newValue));

                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
                    try {
                        entityManager.getTransaction().begin();
                        selectedCode = entityManager.createQuery("from Code c where c.id = :id", Code.class).setParameter("id", selectedCode.getId()).getSingleResult();
                        selectedCode.setCount(newValue);
                        entityManager.getTransaction().commit();
                        loadInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorMessage.show(e);
                        entityManager.getTransaction().rollback();
                    } finally {
                        entityManager.close();
                    }
                }
            }
        });
    }

    private void edit() {
        Code selectedEntry = stockTableView.getSelectionModel().getSelectedItem();

        if (selectedEntry != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_CODE));
            Dialog<ButtonType> dialog = new Dialog<>();
            try {
                dialog.setDialogPane(fxmlLoader.load());
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.show(e);
            }
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(Main.getLogo());
            CreateNewCodeDialogController controller = fxmlLoader.getController();

            EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
            try {
                entityManager.getTransaction().begin();
                Code selectedCode = entityManager.createQuery("from Code c where c.id = :id", Code.class).setParameter("id", selectedEntry.getId())
                        .getSingleResult();

                controller.populateWindow(selectedCode);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    controller.readFields(selectedCode);
                    entityManager.getTransaction().commit();
                    loadInfo();
                }
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                e.printStackTrace();
                ErrorMessage.show(e);
            } finally {
                entityManager.close();
            }
        }
    }

    private boolean fieldsEmpty() {
        return     (codeTF.getText() == null || codeTF.getText().length() == 0)
                && (clientTF.getText() == null || clientTF.getText().length() == 0)
                && (frameSizeTF.getText() == null || frameSizeTF.getText().length() == 0)
                && (meshTF.getText() == null || meshTF.getText().length() == 0);
    }
}

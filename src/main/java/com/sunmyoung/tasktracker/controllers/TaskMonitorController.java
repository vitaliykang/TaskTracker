package com.sunmyoung.tasktracker.controllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.controllers.dialogControllers.CreateOrderDialogController;
import com.sunmyoung.tasktracker.pojos.Task;
import com.sunmyoung.tasktracker.repositories.Database;
import com.sunmyoung.tasktracker.repositories.TaskRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TaskMonitorController {
    @Getter
    private ObservableList<Task> data;

    @FXML
    private TableColumn<Task, String> deadlineCol,
            orderDateCol,
            orderCol,
            clientCol,
            countCol,
            sizeCol,
            meshCol,
            combiCol,
            typeCol,
            tensioningCol,
            coatingCol,
            washingCol,
            exposureCol;

    @FXML
    @Getter
    private TableView<Task> tableView;


    @FXML
    public void refresh(ActionEvent event) {
        loadData();
    }

    @FXML
    void createNewTask(ActionEvent event) {
        createOrderDialog();
    }

    @FXML
    void showDetails(ActionEvent event) {

    }

    @FXML
    void editTask(ActionEvent event) {
        editTask();
    }

    @FXML
    void test(ActionEvent event) {

    }

    public void initialize() {
        initTableView();
        loadData();
        activateStatusCells();
    }

    public void loadData() {
        List<Task> taskList = TaskRepository.getUnfinishedTasks();
        System.out.println(taskList);

        data = FXCollections.observableArrayList(taskList);
        tableView.setItems(data);
    }

    private void initTableView() {
        //Single cell selection
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        tableView.setEditable(true);

        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(tableView.getItems().indexOf(order.getValue()) + 1)));
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDateStr"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        meshCol.setCellValueFactory(new PropertyValueFactory<>("mesh"));
        combiCol.setCellValueFactory(new PropertyValueFactory<>("combi"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("aluminum"));
        tensioningCol.setCellValueFactory(new PropertyValueFactory<>("tensioning"));
        coatingCol.setCellValueFactory(new PropertyValueFactory<>("coating"));
        washingCol.setCellValueFactory(new PropertyValueFactory<>("washing"));
        exposureCol.setCellValueFactory(new PropertyValueFactory<>("exposure"));
    }

    //make tensioning, washing, coating and exposure cells editable
    private void activateStatusCells() {
        activateTensioningCol();
        activateCoatingCol();
        activateWashingCol();
        activateExposureCol();
    }

    private void activateTensioningCol() {
        tensioningCol.setEditable(true);
        tensioningCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tensioningCol.setOnEditCommit(event -> {
            Task selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = Database.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from Task t where t.id = :id", Task.class).setParameter("id", selectedTask.getId()).uniqueResult();
                    //here
                    selectedTask.setTensioning(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    session.close();
                }
            }
        });
    }

    private void activateCoatingCol() {
        coatingCol.setEditable(true);
        coatingCol.setCellFactory(TextFieldTableCell.forTableColumn());
        coatingCol.setOnEditCommit(event -> {
            Task selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = Database.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from Task t where t.id = :id", Task.class).setParameter("id", selectedTask.getId()).uniqueResult();
                    //here
                    selectedTask.setCoating(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    session.close();
                }
            }
        });
    }

    private void activateWashingCol() {
        washingCol.setEditable(true);
        washingCol.setCellFactory(TextFieldTableCell.forTableColumn());
        washingCol.setOnEditCommit(event -> {
            Task selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = Database.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from Task t where t.id = :id", Task.class).setParameter("id", selectedTask.getId()).uniqueResult();
                    //here
                    selectedTask.setWashing(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    session.close();
                }
            }
        });
    }

    private void activateExposureCol() {
        exposureCol.setEditable(true);
        exposureCol.setCellFactory(TextFieldTableCell.forTableColumn());
        exposureCol.setOnEditCommit(event -> {
            Task selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = Database.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from Task t where t.id = :id", Task.class).setParameter("id", selectedTask.getId()).uniqueResult();
                    //here
                    selectedTask.setExposure(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    session.close();
                }
            }
        });
    }

    @SneakyThrows
    private void editTask() {
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createOrderDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateOrderDialogController controller = fxmlLoader.getController();

        controller.populateWindow(taskId);

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            controller.editTask(taskId);
            loadData();
        }
    }

    @SneakyThrows
    private void createOrderDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createOrderDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateOrderDialogController controller = fxmlLoader.getController();

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            controller.createTask();
            loadData();
        }
    }

}

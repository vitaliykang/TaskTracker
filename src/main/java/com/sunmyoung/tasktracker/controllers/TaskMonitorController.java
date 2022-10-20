package com.sunmyoung.tasktracker.controllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.controllers.dialogControllers.CreateOrderDialogController;
import com.sunmyoung.tasktracker.controllers.dialogControllers.CreateOrderDialogControllerV2;
import com.sunmyoung.tasktracker.pojos.Task;
import com.sunmyoung.tasktracker.repositories.Database;
import com.sunmyoung.tasktracker.repositories.TaskRepository;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.*;

public class TaskMonitorController {
    @Getter
    private ObservableList<Task> tasksObservableList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Task, LocalDate>
            deadlineCol,
            dateInCol;

    @FXML
    private TableColumn<Task, Integer> countCol;

    @FXML
    private TableColumn<Task, String>
            orderCol,
            clientCol,
            sizeCol,
            meshCol,
            combiCol,
            tensioningCol,
            coatingCol,
            packagingCol,
            exposureCol;

    @FXML
    @Getter
    private TableView<Task> tableView;

    @FXML
    void createNewTask(ActionEvent event) {
        createOrderDialog();
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshTable();
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
        startAutoRefresh(600000);
    }

    public void loadData() {
        List<Task> taskList = TaskRepository.getUnfinishedTasks();
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        System.out.println(taskList);

        tasksObservableList.clear();
        tasksObservableList.addAll(taskList);
    }

    /**
     * Manually refresh the tasks table.
     */
    private void refreshTable() {
        tasksObservableList.clear();
        loadData();
    }

    private void initTableView() {
        tableView.setItems(tasksObservableList);

        //Single cell selection
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        tableView.setEditable(true);

        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(tableView.getItems().indexOf(order.getValue()) + 1)));
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        dateInCol.setCellValueFactory(new PropertyValueFactory<>("dateIn"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("frameSize"));
        meshCol.setCellValueFactory(new PropertyValueFactory<>("mesh"));
        combiCol.setCellValueFactory(new PropertyValueFactory<>("combi"));
        tensioningCol.setCellValueFactory(new PropertyValueFactory<>("tensioning"));
        coatingCol.setCellValueFactory(new PropertyValueFactory<>("coating"));
        packagingCol.setCellValueFactory(new PropertyValueFactory<>("packaging"));
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
        packagingCol.setEditable(true);
        packagingCol.setCellFactory(TextFieldTableCell.forTableColumn());
        packagingCol.setOnEditCommit(event -> {
            Task selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = Database.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from Task t where t.id = :id", Task.class).setParameter("id", selectedTask.getId()).uniqueResult();
                    //here
                    selectedTask.setPackaging(newValue);
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

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createOrderDialogV2.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();

        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            //get the task from db and load info into the form
            Task task = session.createQuery("from Task t where t.id = :taskId", Task.class).setParameter("taskId", taskId).uniqueResult();
            controller.populateWindow(task);
            controller.enableElements(false);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                //subtasksChanged is triggered if addSubtask or removeSubtask buttons were pressed.
                if (controller.isSubtasksChanged()) {
                    task.getSubtasks().forEach(subtask -> subtask.setTask(null));
                    controller.readFields(task);
                    session.createQuery("delete from Subtask where task = null").executeUpdate();
                } else {
                    controller.readFields(task);
                }

                transaction.commit();
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
    }

    @SneakyThrows
    private void createOrderDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createOrderDialogV2.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            Task task = new Task();
            controller.readFields(task);
            TaskRepository.save(task);
            loadData();
        }
    }

    /**
     * @param delay - pause time between table updates in milliseconds.
     */
    private void startAutoRefresh(int delay) {
        Thread thread = new Thread(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                while (true) {
                    Thread.sleep(delay);
                    Platform.runLater(() -> {
                        List<Task> taskList = TaskRepository.getUnfinishedTasks();
                        tasksObservableList.clear();
                        tasksObservableList.addAll(taskList);
                        System.out.println("Refreshed");
                    });
                }
            }
        });
        thread.start();
    }
}

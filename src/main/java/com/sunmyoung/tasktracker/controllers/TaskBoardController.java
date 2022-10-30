package com.sunmyoung.tasktracker.controllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.controllers.dialogControllers.ConfirmDeleteDialogController;
import com.sunmyoung.tasktracker.controllers.dialogControllers.CreateOrderDialogControllerV2;
import com.sunmyoung.tasktracker.controllers.dialogControllers.InspectionDialogController;
import com.sunmyoung.tasktracker.pojos.CompletedTask;
import com.sunmyoung.tasktracker.pojos.InspectionReport;
import com.sunmyoung.tasktracker.pojos.ActiveTask;
import com.sunmyoung.tasktracker.repositories.DatabaseConnection;
import com.sunmyoung.tasktracker.repositories.TaskRepository;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.*;

public class TaskBoardController {
    @Getter
    private ObservableList<ActiveTask> tasksObservableList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ActiveTask, LocalDate>
            deadlineCol,
            dateInCol;

    @FXML
    private TableColumn<ActiveTask, Integer> countCol;

    @FXML
    private TableColumn<ActiveTask, String>
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
    private TableView<ActiveTask> tableView;

    @FXML
    void createNewTask(ActionEvent event) {
        createOrderDialog();
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshTable();
    }

    @FXML
    void viewInspectionReport(ActionEvent event) {
        viewInspectionReport();
    }

    @FXML
    void viewDetails(ActionEvent event) {
        viewDetails();
    }

    @FXML
    void markAsCompleted(ActionEvent event) {
        markAsCompleted();
    }

    @FXML
    void cancelTask(ActionEvent event) {
        cancelTask();
    }

    @FXML
    @SneakyThrows
    void returnToMainMenu(ActionEvent event) {
        MainV2Controller.showMainScreen(event);
    }

    public void initialize() {
        initTableView();
        loadData();
        activateStatusCells();
        startAutoRefresh(600000);
    }

    public void loadData() {
        List<ActiveTask> taskList = TaskRepository.getActiveTasks();
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
            ActiveTask selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = DatabaseConnection.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).uniqueResult();
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
            ActiveTask selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = DatabaseConnection.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).uniqueResult();
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
            ActiveTask selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = DatabaseConnection.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).uniqueResult();
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
            ActiveTask selectedTask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            if (!newValue.equals(event.getOldValue())){
                Session session = DatabaseConnection.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    selectedTask = session.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).uniqueResult();
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
    private void viewInspectionReport() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/inspectionDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(fxmlLoader.load());

        InspectionDialogController controller = fxmlLoader.getController();

        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            //get the task from db and load info into the form
            ActiveTask task = session.createQuery("from ActiveTask t where t.id = :taskId", ActiveTask.class).setParameter("taskId", taskId).uniqueResult();
            Set<InspectionReport> reportList = task.getInspectionReports();
            System.out.println(reportList);
            controller.getInspectionReportObservableList().addAll(reportList);
            System.out.println(controller.getInspectionReportObservableList());

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                List<InspectionReport> inspectionReports = controller.getInspectionReportObservableList();
                inspectionReports.forEach(report -> report.setTask(task));
                task.getInspectionReports().addAll(inspectionReports);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
    }

    @SneakyThrows
    private void viewDetails() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createOrderDialogV2.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();
        controller.enableEditCheckBox(true);

        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            //get the task from db and load info into the form
            ActiveTask task = session.createQuery("from ActiveTask t where t.id = :taskId", ActiveTask.class).setParameter("taskId", taskId).uniqueResult();
            controller.populateWindow(task);
            controller.enableElements(false);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                //subtasksChanged is triggered when addSubtask or removeSubtask buttons were pressed.
//                if (controller.isSubtasksChanged()) {
//                    task.getSubtasks().forEach(subtask -> subtask.setTask(null));
//                    controller.readFields(task);
//                    session.createQuery("delete from Subtask where task = null").executeUpdate();
//                } else {
//                    controller.readFields(task);
//                }
                controller.readFields(task);

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
            ActiveTask task = new ActiveTask();
            controller.readFields(task);
            TaskRepository.save(task);
            loadData();
        }
    }

    @SneakyThrows
    private void cancelTask() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/confirmDeleteDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(false);
        dialog.setDialogPane(fxmlLoader.load());

        ConfirmDeleteDialogController controller = fxmlLoader.getController();
        controller.getTaskInfoLabel().setText(selectedTask.toString());

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            TaskRepository.delete(taskId);
            loadData();
        }
    }

    private void markAsCompleted() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            selectedTask = session.createQuery("from ActiveTask t where t.id = :taskId", ActiveTask.class)
                    .setParameter("taskId", taskId).uniqueResult();
            selectedTask.getSubtasks().forEach(subtask -> subtask.setTask(null));
            selectedTask.getInspectionReports().forEach(report -> report.setTask(null));

            CompletedTask completedTask = new CompletedTask(selectedTask);
            completedTask.setSubtasks(selectedTask.getSubtasks());
            completedTask.getSubtasks().forEach(subtask -> subtask.setCompletedTask(completedTask));
            completedTask.setInspectionReports(selectedTask.getInspectionReports());
            completedTask.getInspectionReports().forEach(report -> report.setCompletedTask(completedTask));
            selectedTask.setSubtasks(null);
            selectedTask.setInspectionReports(null);

            session.save(completedTask);
            session.createQuery("delete ActiveTask t where t.id = :taskId").setParameter("taskId", taskId)
                    .executeUpdate();

            loadData();

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
                        List<ActiveTask> taskList = TaskRepository.getActiveTasks();
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

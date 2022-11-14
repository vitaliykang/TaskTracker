package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.controllers.dialogControllers.ConfirmDeleteDialogController;
import com.sunmyoung.task_tracker.controllers.dialogControllers.CreateOrderDialogControllerV2;
import com.sunmyoung.task_tracker.controllers.dialogControllers.InspectionDialogController;
import com.sunmyoung.task_tracker.pojos.CompletedTask;
import com.sunmyoung.task_tracker.pojos.InspectionReport;
import com.sunmyoung.task_tracker.pojos.ActiveTask;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import com.sunmyoung.task_tracker.repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
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
        loadData();
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

    /**
     * Download the list of active tasks from database and load them into the tableview.
     * Use this method to refresh the table.
     */
    public void loadData() {
        List<ActiveTask> taskList = TaskRepository.getActiveTasks();
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        Utilities.printStatus(String.format("Number of active tasks: %d", taskList.size()));

        tasksObservableList.clear();
        tasksObservableList.addAll(taskList);
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
                EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
                EntityTransaction transaction = null;
                try {
                    transaction = entityManager.getTransaction();
                    transaction.begin();
                    selectedTask = entityManager.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).getSingleResult();
                    selectedTask.setTensioning(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    entityManager.close();
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
                EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
                EntityTransaction transaction = null;
                try {
                    transaction = entityManager.getTransaction();
                    transaction.begin();
                    selectedTask = entityManager.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).getSingleResult();
                    selectedTask.setCoating(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    entityManager.close();
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
                EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
                EntityTransaction transaction = null;
                try {
                    transaction = entityManager.getTransaction();
                    transaction.begin();
                    selectedTask = entityManager.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).getSingleResult();
                    selectedTask.setPackaging(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    entityManager.close();
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
                EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
                EntityTransaction transaction = null;
                try {
                    transaction = entityManager.getTransaction();
                    transaction.begin();
                    selectedTask = entityManager.createQuery("from ActiveTask t where t.id = :id", ActiveTask.class).setParameter("id", selectedTask.getId()).getSingleResult();
                    selectedTask.setExposure(newValue);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    entityManager.close();
                }
            }
        });
    }

    @SneakyThrows
    private void viewInspectionReport() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/inspectionDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(fxmlLoader.load());

        InspectionDialogController controller = fxmlLoader.getController();

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            //get the task from db and load info into the form
            ActiveTask task = entityManager.createQuery("from ActiveTask t where t.id = :taskId", ActiveTask.class).setParameter("taskId", taskId).getSingleResult();
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
            entityManager.close();
        }
    }

    @SneakyThrows
    private void viewDetails() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/createOrderDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();
        controller.enableEditCheckBox(true);

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            //get the task from db and load info into the form
            ActiveTask task = entityManager.createQuery("from ActiveTask t where t.id = :taskId", ActiveTask.class).setParameter("taskId", taskId).getSingleResult();
            controller.populateWindow(task);
            controller.enableElements(false);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                controller.readFields(task);

                transaction.commit();
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null)
                transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    @SneakyThrows
    private void createOrderDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/createOrderDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.fieldsOK()) {
                event.consume();
            }
        });

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            ActiveTask task = new ActiveTask();
            controller.readFields(task);
            TaskRepository.save(task);
            controller.saveClientInfo();
            Utilities.printStatus(String.format("New task [%s] is created.", task));
            loadData();
        }
    }

    @SneakyThrows
    private void cancelTask() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/confirmDeleteDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(false);
        dialog.setDialogPane(fxmlLoader.load());

        ConfirmDeleteDialogController controller = fxmlLoader.getController();
        controller.getTaskInfoLabel().setText(selectedTask.toString());

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            TaskRepository.delete(taskId);
            Utilities.printStatus(String.format("Task [%s] was cancelled", selectedTask));
            loadData();
        }
    }

    private void markAsCompleted() {
        ActiveTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        long taskId = selectedTask.getId();

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            selectedTask = entityManager.createQuery("from ActiveTask t where t.id = :taskId", ActiveTask.class)
                    .setParameter("taskId", taskId).getSingleResult();
            selectedTask.getSubtasks().forEach(subtask -> subtask.setTask(null));
            selectedTask.getInspectionReports().forEach(report -> report.setTask(null));

            CompletedTask completedTask = new CompletedTask(selectedTask);
            completedTask.setSubtasks(selectedTask.getSubtasks());
            completedTask.getSubtasks().forEach(subtask -> subtask.setCompletedTask(completedTask));
            completedTask.setInspectionReports(selectedTask.getInspectionReports());
            completedTask.getInspectionReports().forEach(report -> report.setCompletedTask(completedTask));
            selectedTask.setSubtasks(null);
            selectedTask.setInspectionReports(null);

            entityManager.persist(completedTask);
            entityManager.remove(selectedTask);

            Utilities.printStatus(String.format("Task [%s] was marked as completed", completedTask));

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }

        loadData();
    }

    /**
     * Sets tableView auto-refresh rate.
     * @param delay - pause time between table updates in milliseconds.
     */
    private void startAutoRefresh(int delay) {
        Utilities.printStatus(String.format("Refresh rate is set to: 1 in %d minutes", delay/60000));
        Thread thread = new Thread(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                while (true) {
                    Thread.sleep(delay);
                    Platform.runLater(() -> {
                        loadData();
                        Utilities.printStatus("Table Refreshed");
                    });
                }
            }
        });
        thread.start();
    }
}

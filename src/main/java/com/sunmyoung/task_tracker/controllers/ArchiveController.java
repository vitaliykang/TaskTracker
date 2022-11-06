package com.sunmyoung.task_tracker.controllers;


import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.controllers.dialogControllers.CreateOrderDialogControllerV2;
import com.sunmyoung.task_tracker.controllers.dialogControllers.InspectionDialogController;
import com.sunmyoung.task_tracker.pojos.CompletedTask;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ArchiveController {
    @FXML
    private TableColumn<CompletedTask, Integer> orderCol;

    @FXML
    private TableColumn<CompletedTask, String>
            clientCol,
            frameSizeCol,
            meshCol,
            combiCol,
            countCol,
            filmTypeCol,
            frameNewOldCol,
            shippingMethodCol;

    @FXML
    private TableColumn<CompletedTask, LocalDate>
            dateInCol,
            dateOutCol;

    @FXML
    private TableView<CompletedTask> tasksTableView;

    @FXML
    private DatePicker startPicker;

    @FXML
    private DatePicker endPicker;

    @FXML
    private TextField clientTF;

    private StringBuilder queryBuilder;
    private boolean isFirstParameter = true;

    @FXML
    void getResults(ActionEvent event) {
        loadInfo();
    }

    @FXML
    void viewDetails(ActionEvent event) {
        viewDetails();
    }

    @FXML
    void viewInspectionReport(ActionEvent event) {
        viewInspectionReport();
    }

    @FXML
    @SneakyThrows
    void returnToMainMenu(ActionEvent event) {
        MainV2Controller.showMainScreen(event);
    }

    @SneakyThrows
    private void viewDetails() {
        CompletedTask selectedTask = tasksTableView.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/createOrderDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(false);
        dialog.setDialogPane(fxmlLoader.load());

        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();
        controller.enableElements(false);

        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            selectedTask = session.createQuery("from CompletedTask t where t.id = :id", CompletedTask.class)
                            .setParameter("id", selectedTask.getId()).uniqueResult();
            controller.populateWindow(selectedTask);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        dialog.showAndWait();
    }

    @SneakyThrows
    private void viewInspectionReport() {
        CompletedTask selectedTask = tasksTableView.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/inspectionDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(false);
        dialog.setDialogPane(fxmlLoader.load());

        InspectionDialogController controller = fxmlLoader.getController();
        controller.enableEditing(false);

        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            selectedTask = session.createQuery("from CompletedTask t where t.id = :id", CompletedTask.class)
                    .setParameter("id", selectedTask.getId()).uniqueResult();
            controller.getInspectionReportObservableList().addAll(selectedTask.getInspectionReports());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        dialog.showAndWait();
    }

    private ObservableList<CompletedTask> tasksObservableList = FXCollections.observableArrayList();

    public void initialize() {
        //setting start date picker initial value
        LocalDate date = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        date = date.withDayOfMonth(1);
        startPicker.setValue(date);

        initTableView();
        loadInfo();
    }

    private void initTableView() {
        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(tasksObservableList.indexOf(order.getValue()) + 1));

        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        frameSizeCol.setCellValueFactory(new PropertyValueFactory<>("frameSize"));
        meshCol.setCellValueFactory(new PropertyValueFactory<>("mesh"));
        combiCol.setCellValueFactory(new PropertyValueFactory<>("combi"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        filmTypeCol.setCellValueFactory(new PropertyValueFactory<>("film"));
        frameNewOldCol.setCellValueFactory(new PropertyValueFactory<>("frameNewOld"));
        shippingMethodCol.setCellValueFactory(new PropertyValueFactory<>("shippingMethod"));

        dateInCol.setCellValueFactory(new PropertyValueFactory<>("dateIn"));
        dateOutCol.setCellValueFactory(new PropertyValueFactory<>("dateOut"));

        tasksTableView.setItems(tasksObservableList);
    }

    private void loadInfo() {
        queryBuilder = new StringBuilder("From CompletedTask t where");

        Session session = DatabaseConnection.getSessionFactory().openSession();

        if (startPicker.getValue() != null) {
            builderAddStartDate();
            System.out.println(startPicker.getValue());
        }
        if (endPicker.getValue() != null) {
            builderAddEndDate();
        }
        if (!clientTF.getText().equals("")) {
            builderAddClient();
        }
        //add new parameter here 1/4

        isFirstParameter = true;

        System.out.println(queryBuilder.toString());

        String query = queryBuilder.toString();
        TypedQuery<CompletedTask> typedQuery = session.createQuery(query, CompletedTask.class);


        if (startPicker.getValue() != null) {
            addParameterStartDate(typedQuery);
        }
        if (endPicker.getValue() != null) {
            addParameterEndDate(typedQuery);
        }
        if (!clientTF.getText().equals("")) {
            addParameterClient(typedQuery);
        }
        //add new parameter here 2/4

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<CompletedTask> tasks = typedQuery.getResultList();
            tasksObservableList.clear();
            tasksObservableList.addAll(tasks);
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

    private TypedQuery<CompletedTask> addParameterStartDate(TypedQuery<CompletedTask> query) {
        return query.setParameter("start", startPicker.getValue());
    }

    private void builderAddStartDate() {
        if (isFirstParameter) {
            queryBuilder.append(" t.dateIn >= :start");
            isFirstParameter = false;
        } else {
            queryBuilder.append(" and t.dateIn >= :start");
        }
    }

    private TypedQuery<CompletedTask> addParameterEndDate(TypedQuery<CompletedTask> query) {
        return query.setParameter("end", endPicker.getValue());
    }

    private void builderAddEndDate() {
        if (isFirstParameter) {
            queryBuilder.append(" t.dateIn <= :end");
            isFirstParameter = false;
        } else {
            queryBuilder.append(" and t.dateIn <= :end");
        }
    }

    private TypedQuery<CompletedTask> addParameterClient(TypedQuery<CompletedTask> query) {
        return query.setParameter("client", clientTF.getText());
    }

    private void builderAddClient() {
        if (isFirstParameter) {
            queryBuilder.append(" t.client = :client");
            isFirstParameter = false;
        } else {
            queryBuilder.append(" and t.client = :client");
        }
    }
    //add new parameter here 3/4 and 4/4
}

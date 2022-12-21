package com.sunmyoung.task_tracker.controllers;


import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.controllers.dialogControllers.code.CodeSearchDialogController;
import com.sunmyoung.task_tracker.controllers.dialogControllers.order.CreateOrderDialogControllerV2;
import com.sunmyoung.task_tracker.controllers.dialogControllers.order.InspectionDialogController;
import com.sunmyoung.task_tracker.pojos.CompletedTask;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import lombok.SneakyThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    @FXML
    private TextField codeTF;

    @FXML
    private Button openListBtn;

    private StringBuilder queryBuilder;
    private boolean isFirstParameter = true;
    private ObservableList<CompletedTask> tasksObservableList = FXCollections.observableArrayList();

    @FXML
    void openList(ActionEvent event) {
        openList();
    }

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

    public void initialize() {
        DateTimeFormatter koreanFormat = CreateOrderDialogControllerV2.getKoreanFormat();
        StringConverter<LocalDate> stringConverter = new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate == null)
                    return "";
                return koreanFormat.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if(dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, koreanFormat);
            }
        };

        startPicker.setConverter(stringConverter);
        endPicker.setConverter(stringConverter);

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

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();

        if (startPicker.getValue() != null) {
            builderAddStartDate();
            System.out.println(startPicker.getValue());
        }
        if (endPicker.getValue() != null) {
            builderAddEndDate();
        }
        if (!clientTF.getText().equals("") && clientTF.getText() != null) {
            builderAddClient();
        }
        if (!codeTF.getText().equals("") && codeTF.getText() != null) {
            builderAddCode();
        }
        //add new parameter here 1/4

        isFirstParameter = true;

        System.out.println(queryBuilder.toString());

        String query = queryBuilder.toString();
        TypedQuery<CompletedTask> typedQuery = entityManager.createQuery(query, CompletedTask.class);


        if (startPicker.getValue() != null) {
            addParameterStartDate(typedQuery);
        }
        if (endPicker.getValue() != null) {
            addParameterEndDate(typedQuery);
        }
        if (!clientTF.getText().equals("") && clientTF.getText() != null) {
            addParameterClient(typedQuery);
        }
        if (!codeTF.getText().equals("") && codeTF.getText() != null) {
            addParameterCode(typedQuery);
        }
        //add new parameter here 2/4

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
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
            entityManager.close();
        }
    }


    @SneakyThrows
    private void openList() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/codeSearchDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CodeSearchDialogController controller = fxmlLoader.getController();
        controller.getAddBtn().setVisible(false);
        controller.getRemoveBtn().setVisible(false);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            codeTF.setText(controller.getTextField().getText());
        }
    }

    @SneakyThrows
    private void viewDetails() {
        CompletedTask selectedTask = tasksTableView.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/createOrderDialog - management.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(false);
        dialog.setDialogPane(fxmlLoader.load());

        CreateOrderDialogControllerV2 controller = fxmlLoader.getController();
        controller.enableElements(false);

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            selectedTask = entityManager.createQuery("from CompletedTask t where t.id = :id", CompletedTask.class)
                    .setParameter("id", selectedTask.getId()).getSingleResult();
            controller.populateWindow(selectedTask);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
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

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            selectedTask = entityManager.createQuery("from CompletedTask t where t.id = :id", CompletedTask.class)
                    .setParameter("id", selectedTask.getId()).getSingleResult();
            controller.getInspectionReportObservableList().addAll(selectedTask.getInspectionReports());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }

        dialog.showAndWait();
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

    private TypedQuery<CompletedTask> addParameterCode(TypedQuery<CompletedTask> query) {
        return query.setParameter("code", codeTF.getText());
    }

    private void builderAddCode() {
        if (isFirstParameter) {
            queryBuilder.append(" t.code = :code");
            isFirstParameter = false;
        } else {
            queryBuilder.append(" and t.code = :code");
        }
    }
    //add new parameter here 3/4 and 4/4
}

package com.sunmyoung.tasktracker.controllers;


import com.sunmyoung.tasktracker.pojos.Task;
import com.sunmyoung.tasktracker.repositories.Database;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ArchiveController {
    @FXML
    private TableColumn<Task, Integer> orderCol;

    @FXML
    private TableColumn<Task, String>
            clientCol,
            frameSizeCol,
            meshCol,
            combiCol,
            countCol,
            filmTypeCol,
            frameNewOldCol,
            shippingMethodCol;

    @FXML
    private TableColumn<Task, LocalDate>
            dateInCol,
            dateOutCol;

    @FXML
    private TableView<Task> tasksTableView;

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

    private ObservableList<Task> tasksObservableList = FXCollections.observableArrayList();

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
        queryBuilder = new StringBuilder("From Task t where");

        Session session = Database.getSessionFactory().openSession();

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
        TypedQuery<Task> typedQuery = session.createQuery(query, Task.class);


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
            List<Task> tasks = typedQuery.getResultList();
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

    private TypedQuery<Task> addParameterStartDate(TypedQuery<Task> query) {
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

    private TypedQuery<Task> addParameterEndDate(TypedQuery<Task> query) {
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

    private TypedQuery<Task> addParameterClient(TypedQuery<Task> query) {
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

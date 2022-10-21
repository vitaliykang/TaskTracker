package com.sunmyoung.tasktracker.controllers;


import com.sunmyoung.tasktracker.pojos.Task;
import com.sunmyoung.tasktracker.repositories.Database;
import com.sunmyoung.tasktracker.repositories.TaskRepository;
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
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private StringBuilder queryBuilder = new StringBuilder("From Task t where");
    private boolean firstParameter = true;

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
//        loadInfo();
        Session session = Database.getSessionFactory().openSession();
        String str = "from Task t where t.client = :client";
        TypedQuery<Task> query = session.createQuery(str, Task.class);
        query.setParameter("client", "RN2");
        tasksObservableList.clear();
        tasksObservableList.addAll(query.getResultList());
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
        Session session = Database.getSessionFactory().openSession();

        if (startPicker.getValue() != null) {
            builderAddStartDate();
        }
        if (endPicker.getValue() != null) {
            builderAddEndDate();
        }
        if (clientTF.getText() != null) {
            builderAddClient();
        }

        TypedQuery<Task> typedQuery = session.createQuery(queryBuilder.toString(), Task.class);


        if (startPicker.getValue() != null) {
            addParameterStartDate(typedQuery);
        }
        if (endPicker.getValue() != null) {
            addParameterEndDate(typedQuery);
        }
        if (clientTF.getText() != null) {
            addParameterClient(typedQuery);
        }

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
        if (firstParameter) {
            queryBuilder.append(" t.dateIn >= :start");
        } else {
            queryBuilder.append(" and t.dateIn >= :start");
        }
    }

    private TypedQuery<Task> addParameterEndDate(TypedQuery<Task> query) {
        return query.setParameter("end", endPicker.getValue());
    }

    private void builderAddEndDate() {
        if (firstParameter) {
            queryBuilder.append(" t.dateIn <= :end");
        } else {
            queryBuilder.append(" and t.dateIn <= :end");
        }
    }

    private TypedQuery<Task> addParameterClient(TypedQuery<Task> query) {
        return query.setParameter("client", clientTF.getText());
    }

    private void builderAddClient() {
        if (firstParameter) {
            queryBuilder.append(" t.client >= :client");
        } else {
            queryBuilder.append(" and t.client >= :client");
        }
    }
}

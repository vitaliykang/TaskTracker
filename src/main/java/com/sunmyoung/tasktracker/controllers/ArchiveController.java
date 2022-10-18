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
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
    void getResults(ActionEvent event) {
        if (startPicker.getValue() != null && endPicker != null) {
            loadInfo(startPicker.getValue(), endPicker.getValue());
        } else {
            loadInfo();
        }
    }

    private ObservableList<Task> tasksObservableList = FXCollections.observableArrayList();

    public void initialize() {
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

    //default loader. Loads info for the current month.
    private void loadInfo() {
        LocalDate date = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        date = date.withDayOfMonth(1);
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Task> tasks = session.createQuery("from Task t where t.dateIn >= :date", Task.class).setParameter("date", date).getResultList();
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

    private void loadInfo(LocalDate start, LocalDate end) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Task> tasks = session.createQuery("from Task t where t.dateIn >= :start and t.dateIn <= :end", Task.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
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
}

//    Session session = Database.getSessionFactory().openSession();
//    Transaction transaction = null;
//        try {
//                transaction = session.beginTransaction();
//                transaction.commit();
//                } catch (Exception e) {
//                e.printStackTrace();
//                if (transaction != null) {
//                transaction.rollback();
//                }
//                } finally {
//                session.close();
//                }
//          }

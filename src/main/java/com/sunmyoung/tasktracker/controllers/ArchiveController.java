package com.sunmyoung.tasktracker.controllers;


import com.sunmyoung.tasktracker.pojos.Task;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

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

    private ObservableList<Task> tasksObservableList = FXCollections.observableArrayList();

    public void initialize() {

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
    }
}

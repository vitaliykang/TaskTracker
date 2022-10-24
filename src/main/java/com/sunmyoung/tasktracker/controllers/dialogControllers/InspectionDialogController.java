package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.pojos.InspectionReport;
import com.sunmyoung.tasktracker.pojos.Task;
import com.sunmyoung.tasktracker.repositories.Database;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InspectionDialogController {

    @FXML
    private TableColumn<InspectionReport, String>
            orderCol,
            lotCol,
            serialNumberCol,
            oneDayAgingCol,
            dateCol,
            meshThicknessCol,
            coatingsCol,
            totalThicknessCol,
            exposureCheckCol,
            finalCheckCol;

    @FXML
    private TableView<InspectionReport> inspectionTableView;

    @Getter
    private ObservableList<InspectionReport> inspectionReportObservableList = FXCollections.observableArrayList();


    @FXML
    void addInspectionReport(ActionEvent event) {
        inspectionReportObservableList.add(new InspectionReport());
    }

    @FXML
    void removeInspectionReport(ActionEvent event) {
        inspectionReportObservableList.remove(inspectionTableView.getSelectionModel().getSelectedItem());
    }

    public void initialize() {
        initTableView();
        activateCells();
    }

    private void initTableView() {
        //enable single cell select
        inspectionTableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        inspectionTableView.setEditable(true);

        inspectionTableView.setItems(inspectionReportObservableList);

        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(inspectionReportObservableList.indexOf(order.getValue()) + 1)));
        lotCol.setCellValueFactory(new PropertyValueFactory<>("lot"));
        serialNumberCol.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        oneDayAgingCol.setCellValueFactory(new PropertyValueFactory<>("oneDayAging"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        meshThicknessCol.setCellValueFactory(new PropertyValueFactory<>("meshThickness"));
        coatingsCol.setCellValueFactory(new PropertyValueFactory<>("coatings"));
        totalThicknessCol.setCellValueFactory(new PropertyValueFactory<>("totalThickness"));
        exposureCheckCol.setCellValueFactory(new PropertyValueFactory<>("exposureInspection"));
        finalCheckCol.setCellValueFactory(new PropertyValueFactory<>("finalInspection"));
    }

    private void activateCells() {
        activateLotCol();
        activateSerialNumberCol();
        activateOneDayAgingCol();
        activateDateCol();
        activateMeshThicknessCol();
        activateCoatingsCol();
        activateTotalThicknessCol();
        activateExposureCheckCol();
        activateFinalCheckCol();
    }

    private void activateLotCol() {
        lotCol.setEditable(true);
        lotCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lotCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setLot(newValue);
        });
    }

    private void activateSerialNumberCol() {
        serialNumberCol.setEditable(true);
        serialNumberCol.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumberCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setSerialNumber(newValue);
        });
    }

    private void activateOneDayAgingCol() {
        oneDayAgingCol.setEditable(true);
        oneDayAgingCol.setCellFactory(TextFieldTableCell.forTableColumn());
        oneDayAgingCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setOneDayAging(newValue);
        });
    }

    private void activateDateCol() {
        dateCol.setEditable(true);
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setDate(newValue);
        });
    }

    private void activateMeshThicknessCol() {
        meshThicknessCol.setEditable(true);
        meshThicknessCol.setCellFactory(TextFieldTableCell.forTableColumn());
        meshThicknessCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setMeshThickness(newValue);
        });
    }

    private void activateCoatingsCol() {
        coatingsCol.setEditable(true);
        coatingsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        coatingsCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setCoatings(newValue);
        });
    }

    private void activateTotalThicknessCol() {
        totalThicknessCol.setEditable(true);
        totalThicknessCol.setCellFactory(TextFieldTableCell.forTableColumn());
        totalThicknessCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setTotalThickness(newValue);
        });
    }

    private void activateExposureCheckCol() {
        exposureCheckCol.setEditable(true);
        exposureCheckCol.setCellFactory(TextFieldTableCell.forTableColumn());
        exposureCheckCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setExposureInspection(newValue);
        });
    }

    private void activateFinalCheckCol() {
        finalCheckCol.setEditable(true);
        finalCheckCol.setCellFactory(TextFieldTableCell.forTableColumn());
        finalCheckCol.setOnEditCommit(event -> {
            InspectionReport selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setFinalInspection(newValue);
        });
    }
}


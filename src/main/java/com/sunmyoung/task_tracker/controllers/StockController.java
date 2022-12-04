package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.pojos.Code;
import com.sunmyoung.task_tracker.repositories.CodeRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class StockController {

    @FXML
    private TextField
            codeTF,
            clientTF,
            frameSizeTF,
            meshTF;

    @FXML
    private TableColumn<Code, String>
            orderCol,
            codeCol,
            clientCol,
            frameSizeCol,
            meshCol,
            combiCol,
            meshSizeCol,
            countCol;
    @FXML
    private TableView<Code> stockTableView;

    private ObservableList<Code> observableList = FXCollections.observableArrayList();
    private FilteredList<Code> filteredList = new FilteredList<>(observableList, predicate -> true);

    private boolean codeFlag, clientFlag, frameSizeFlag, meshFlag;

    @FXML
    void add(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {
        System.out.println(filteredList.getPredicate());
    }

    public void initialize() {
        initTableView();
    }

    private void initTableView() {
        observableList.addAll(CodeRepository.findAll());

        stockTableView.setItems(filteredList);

        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(filteredList.indexOf(order.getValue()) + 1)));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        frameSizeCol.setCellValueFactory(new PropertyValueFactory<>("frameSize"));
        meshCol.setCellValueFactory(new PropertyValueFactory<>("mesh"));
        combiCol.setCellValueFactory(new PropertyValueFactory<>("combi"));
        meshSizeCol.setCellValueFactory(new PropertyValueFactory<>("meshSize"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    @FXML
    private void codeFilter() {
        String input = codeTF.getText();
        if (input == null || input.length() == 0) {
            filteredList.setPredicate(predicate -> true);
        } else {
            filteredList.setPredicate(predicate -> {
                if (predicate.getCode() != null) {
                    return predicate.getCode().contains(input);
                }
                return false;
            });
        }
    }

    @FXML
    private void clientFilter() {
        String input = clientTF.getText();
        if (input == null || input.length() == 0) {
            filteredList.setPredicate(predicate -> true);
        } else {
            filteredList.setPredicate(predicate -> {
                if (predicate.getCode() != null) {
                    return predicate.getClient().contains(input);
                }
                return false;
            });
        }
    }

    @FXML
    private void frameSizeFilter() {
        String input = frameSizeTF.getText();
        if (input == null || input.length() == 0) {
            filteredList.setPredicate(predicate -> true);
        } else {
            filteredList.setPredicate(predicate -> {
                if (predicate.getCode() != null) {
                    return predicate.getFrameSize().contains(input);
                }
                return false;
            });
        }
    }

    @FXML
    private void meshFilter() {
        String input = meshTF.getText();
        if (input == null || input.length() == 0) {
            filteredList.setPredicate(predicate -> true);
        } else {
            filteredList.setPredicate(predicate -> {
                if (predicate.getCode() != null) {
                    return predicate.getMesh().contains(input);
                }
                return false;
            });
        }
    }

    private void filter() {

    }
}

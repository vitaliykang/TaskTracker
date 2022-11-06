package com.sunmyoung.task_tracker.controllers.dialogControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ListDialogController {
    private String fileName;
    private ObservableList<String> content = FXCollections.observableArrayList();
    private FilteredList<String> filteredContent = new FilteredList<>(content, predicate -> true);

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField textField;

    @FXML
    void filterList(KeyEvent event) {
        String input = textField.getText();
        if (input == null || input.length() == 0) {
            filteredContent.setPredicate(predicate -> true);
        } else {
            filteredContent.setPredicate(predicate -> predicate.contains(input));
        }
    }

    @FXML
    void selectEntry(MouseEvent event) {
        String selectedEntry = listView.getSelectionModel().getSelectedItem();
        textField.setText(selectedEntry);
    }

    public ListDialogController() {

    }

    public ListDialogController(String fileName) {
        this.fileName = fileName;
    }

    public void initialize() {
//        content.addAll(Utilities.readFromFile(fileName));
        try {
           Path path = Paths.get("src/main/resources/com/sunmyoung/task_tracker/test.txt");
           content.addAll(Files.readAllLines(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setItems(filteredContent);

    }
}

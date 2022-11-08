package com.sunmyoung.task_tracker.controllers.dialogControllers;

import com.sunmyoung.task_tracker.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

public class ListDialogController {
    @Getter @Setter
    private String fileName;
    private ObservableList<String> content = FXCollections.observableArrayList();
    private FilteredList<String> filteredContent = new FilteredList<>(content, predicate -> true);

    @FXML
    private ListView<String> listView;

    @FXML
    @Getter
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

    public void init() {
        System.out.println(fileName);
        content.addAll(Utilities.readFromFile(fileName));
        listView.setItems(filteredContent);
    }
}

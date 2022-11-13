package com.sunmyoung.task_tracker.controllers.dialogControllers;

import com.sunmyoung.task_tracker.pojos.Code;
import com.sunmyoung.task_tracker.repositories.CodeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeSearchDialogController {

    @FXML
    private Rectangle chickenHighlight;

    @FXML
    private Label clientLabel;

    @FXML
    private Rectangle combiHighlight;

    @FXML
    private Label frameSizeLabel;

    @FXML
    private Label meshLabel;

    @FXML
    private Label tensionLabel;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField textField;

    private final Map<String, Code> codeMap = new HashMap<>();
    private final ObservableList<String> content = FXCollections.observableArrayList();
    private final FilteredList<String> filteredContent = new FilteredList<>(content, predicate -> true);

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
        Code code = codeMap.get(selectedEntry);
        textField.setText(selectedEntry);

        clientLabel.setText(code.getClient());
        frameSizeLabel.setText(code.getFrameSize());
        meshLabel.setText(code.getMesh());
        tensionLabel.setText(code.getTension());

        if (code.getCombi().equals("직견장")) {
            chickenHighlight.setVisible(true);
            combiHighlight.setVisible(false);
        } else {
            chickenHighlight.setVisible(false);
            combiHighlight.setVisible(true);
        }
    }

    @FXML
    void add(ActionEvent event) {

    }

    @FXML
    void remove(ActionEvent event) {

    }

    public void initialize() {
        combiHighlight.setVisible(false);
        chickenHighlight.setVisible(false);

        List<Code> codeList = CodeRepository.findAll();
        codeList.forEach(code -> {
            codeMap.put(code.getCode(), code);
        });
        content.addAll(codeMap.keySet());

        listView.setItems(filteredContent);
    }
}


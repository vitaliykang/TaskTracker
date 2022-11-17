package com.sunmyoung.task_tracker.controllers.dialogControllers;

import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.pojos.Code;
import com.sunmyoung.task_tracker.repositories.CodeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

public class CodeSearchDialogController {

    @FXML
    private Rectangle chickenHighlight;

    @FXML
    @Getter
    private Label
            clientLabel,
            frameSizeLabel,
            meshLabel,
            tensionLabel,
            biasLabel;

    @FXML
    @Getter
    private Rectangle combiHighlight;

    @FXML
    private ListView<String> listView;

    @FXML
    @Getter
    private TextField textField;

    @FXML
    @Getter
    private Button addBtn, removeBtn;

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
        if (selectedEntry != null) {
            Code code = codeMap.get(selectedEntry);
            textField.setText(selectedEntry);

            clientLabel.setText(code.getClient());
            frameSizeLabel.setText(code.getFrameSize());
            meshLabel.setText(code.getMesh());
            tensionLabel.setText(code.getTension());
            biasLabel.setText(code.getBias());

            if (code.getCombi().equals("직견장")) {
                chickenHighlight.setVisible(true);
                combiHighlight.setVisible(false);
            } else {
                chickenHighlight.setVisible(false);
                combiHighlight.setVisible(true);
            }
        }
    }

    @FXML
    void add(ActionEvent event) {
        createNewCodeDialog();
    }

    @SneakyThrows
    private void createNewCodeDialog() {
        FXMLLoader mainWindowLoader = new FXMLLoader(Main.class.getResource("dialogs/createOrderDialog.fxml"));
        mainWindowLoader.load();
        CreateOrderDialogControllerV2 taskController = mainWindowLoader.getController();

        FXMLLoader dialogLoader = new FXMLLoader(Main.class.getResource("dialogs/createNewCodeDialog.fxml"));
        DialogPane dialogPane = dialogLoader.load();
        CreateNewCodeDialogController codeController = dialogLoader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Code code = new Code();
            code.setCode(codeController.getCodeTF().getText());
            code.setClient(codeController.getClientTF().getText());
            code.setFrameSize(codeController.getFrameSizeTF().getText());
            code.setMesh(codeController.getMeshTF().getText());
            code.setBias(codeController.getBiasTF().getText());
            code.setTension(codeController.getTensionTF().getText());
            code.setCombi(codeController.getCombiCB().isSelected() ? "COMBI" : "직견장");

            CodeRepository.save(code);

            loadInfo();
        }
    }

    @FXML
    @SneakyThrows
    void remove(ActionEvent event) {
        String selectedEntry = listView.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            Code code = codeMap.get(selectedEntry);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dialogs/confirmDeleteDialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);

            ConfirmDeleteDialogController controller = fxmlLoader.getController();
            controller.getTaskInfoLabel().setText(code.toString());

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                CodeRepository.delete(code.getCode());
                loadInfo();
            }
        }
    }

    public void initialize() {
        combiHighlight.setVisible(false);
        chickenHighlight.setVisible(false);
        loadInfo();
        listView.setItems(filteredContent);
    }

    private void loadInfo() {
        content.clear();
        codeMap.clear();
        List<Code> codeList = CodeRepository.findAll();
        codeList.forEach(code -> {
            codeMap.put(code.getCode(), code);
        });
        content.addAll(codeMap.keySet());
        Collections.sort(content);
    }
}


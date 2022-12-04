package com.sunmyoung.task_tracker.controllers.dialogControllers.code;

import com.sunmyoung.task_tracker.Dialogs;
import com.sunmyoung.task_tracker.ErrorMessage;
import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.controllers.dialogControllers.utility.ConfirmationDialogController;
import com.sunmyoung.task_tracker.pojos.Code;
import com.sunmyoung.task_tracker.repositories.CodeRepository;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import jakarta.persistence.EntityManager;
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
            meshSizeLabel,
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

    private String selectedEntry;

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
        selectedEntry = listView.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            Code code = codeMap.get(selectedEntry);
            textField.setText(selectedEntry);

            clientLabel.setText(code.getClient());
            frameSizeLabel.setText(code.getFrameSize());
            meshSizeLabel.setText(code.getMeshSize());
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

    @FXML
    void edit(ActionEvent event) {
        if (selectedEntry != null) {
            editCode();
        }
    }

    private void editCode() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_CODE));
        Dialog<ButtonType> dialog = new Dialog<>();
        try {
            dialog.setDialogPane(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show(e);
        }
        CreateNewCodeDialogController controller = fxmlLoader.getController();

        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Code selectedCode = entityManager.createQuery("from Code c where c.code = :selectedEntry", Code.class).setParameter("selectedEntry", selectedEntry)
                            .getSingleResult();

            controller.populateWindow(selectedCode);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.readFields(selectedCode);
                entityManager.getTransaction().commit();
                loadInfo();
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            ErrorMessage.show(e);
        } finally {
            entityManager.close();
        }
    }

    @SneakyThrows
    private void createNewCodeDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_CODE));
        DialogPane dialogPane = fxmlLoader.load();
        CreateNewCodeDialogController controller = fxmlLoader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Code code = new Code();
            controller.readFields(code);

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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CONFIRMATION));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);

            ConfirmationDialogController controller = fxmlLoader.getController();
            controller.getMessageLabel().setText("코드를 삭제하시겠습니까?");
            controller.getInfoLabel().setText(code.toString());

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


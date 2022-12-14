package com.sunmyoung.task_tracker.controllers.dialogControllers.order;

import com.sunmyoung.task_tracker.DialogUtilities;
import com.sunmyoung.task_tracker.Dialogs;
import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.controllers.dialogControllers.client.ClientListDialogController;
import com.sunmyoung.task_tracker.controllers.dialogControllers.code.CodeSearchDialogController;
import com.sunmyoung.task_tracker.pojos.*;
import com.sunmyoung.task_tracker.repositories.CodeRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.kordamp.ikonli.bootstrapicons.BootstrapIconsIkonProvider;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CreateOrderDialogControllerV2 {
    @Getter
    private static final DateTimeFormatter koreanFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @FXML
    private CheckBox editCB;

    @FXML
    @Getter
    private TextField serialNumberTF;

    @FXML
    private RadioButton
            shipmentChepanRB,
            shipmentPSRB,
            shipmentSamegiRB;

    @FXML
    @Getter
    private TextField
            clientTF,
            typeTF,
            personTF;

    @FXML
    private RadioButton
            filmExistingRB,
            filmNewRB,
            filmProvidedRB;

    @FXML
    @Getter
    private TextField
            codeTF,
            frameSizeTF,
            meshTF,
            biasTF,
            tensionTF,
            emulsionTF;

    @FXML
    @Getter
    private RadioButton
            typeCombiRB,
            typeDirectRB;

    @FXML
    private RadioButton
            frameExistingRB,
            frameNewRB;

    @FXML
    private TextField frameDirectInputTF;


    @FXML
    private DatePicker
            dateInPicker,
            dateOutPicker;

    @FXML
    private RadioButton
            methodPesongRB,
            methodPedalRB;

    @FXML
    private RadioButton
            amRB,
            pmRB,
            deliveryRB;

    @FXML
    private RadioButton
            positionCenterRB,
            positionCustomRB;

    @FXML
    private TextArea orderNoteTA;

    @FXML
    private ToggleButton frameOnlyToggle;

    @FXML
    private TextField countTF;

    @FXML
    private TableView<Model> subtasksTableView;

    @FXML
    private TableColumn<Model, String>
            printCol,
            thicknessCol,
            countCol,
            orderCol;

    @FXML
    private Button
            addSubtaskButton,
            removeSubtaskButton,
            editSubtaskButton,
            searchByCodeButton,
            searchClientButton;

    @FXML
    @Getter
    private Rectangle
            serialNumberHighlight,
            clientHighlight,
            personHighlight,
            frameSizeHighlight,
            meshHighlight,
            modelHighlight,
            dateOutHighlight;

    @FXML
    @Getter
    private Rectangle editRectangle;

    @FXML
    @Getter
    private FontIcon editIcon;

    private final String DEFAULT_SN = " / ??????????????? ()";

    @Getter
    private final ObservableList<Model> subtaskObservableList = FXCollections.observableArrayList();

    @Getter
    private boolean subtasksChanged;

    private final Map<TextField, Rectangle> textFieldHighlightMap = new HashMap<>();

    @FXML
    void enableEdit() {
        enableElements(editCB.isSelected());
        makeTableViewEditable(editCB.isSelected());
    }

    @FXML
    void addSubtask(ActionEvent event) {
        subtasksChanged = true;
        modelHighlight.setVisible(false);
        createSubtaskDialogWindow();
    }

    @FXML
    @SneakyThrows
    void editSubtask(ActionEvent event) {
        Model subtask = subtasksTableView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_SUBTASK));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Main.getLogo());
        CreateSubtaskDialogController controller = fxmlLoader.getController();

        controller.getPrintTF().setText(subtask.getPrint());
        controller.getThicknessTF().setText(subtask.getThickness());
        controller.getCountTF().setText(subtask.getCount());

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            subtasksChanged = true;
            subtaskObservableList.remove(subtask);
            subtask = controller.getSubtask();
            subtaskObservableList.add(subtask);
            Collections.sort(subtaskObservableList);
        }
    }

    @FXML
    void removeSubtask(ActionEvent event) {
        subtasksChanged = true;
        Model subtask = subtasksTableView.getSelectionModel().getSelectedItem();
        subtaskObservableList.remove(subtask);
    }

    @FXML
    void frameOnlyToggleAction(ActionEvent event) {
        modelHighlight.setVisible(false);
        setFrameOnly(frameOnlyToggle.isSelected());
    }

    @FXML
    void searchClient(ActionEvent event) {
        openClientListDialog();
    }

    @FXML
    void searchByCode(ActionEvent event) {
        if (codeTF.getText().equals("") || codeTF.getText() == null) {
            openCodeSearchDialog();
        } else {
            searchCode();
        }
    }

    private void searchCode() {
        Code code = CodeRepository.findByCode(codeTF.getText());
        if (code != null) {
            frameSizeTF.setText(code.getFrameSize());
            meshTF.setText(code.getMesh());
            tensionTF.setText(code.getTension());
            biasTF.setText(code.getBias());

            if (code.getCombi().equals("COMBI")) {
                typeCombiRB.setSelected(true);
                typeDirectRB.setSelected(false);
            } else {
                typeCombiRB.setSelected(false);
                typeDirectRB.setSelected(true);
            }
        } else {
            openCodeSearchDialog();
        }
    }

    @SneakyThrows
    private void openCodeSearchDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CODE_LIST));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Main.getLogo());

        CodeSearchDialogController controller = fxmlLoader.getController();

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            codeTF.setText(controller.getTextField().getText());
            if (clientTF.getText().equals("") || clientTF.getText() == null) {
                clientTF.setText(controller.getClientLabel().getText());
            }
            frameSizeTF.setText(controller.getFrameSizeLabel().getText());
            meshTF.setText(controller.getMeshLabel().getText());
            tensionTF.setText(controller.getTensionLabel().getText());
            biasTF.setText(controller.getBiasLabel().getText());
            if (controller.getCombiHighlight().isVisible()) {
                typeCombiRB.setSelected(true);
                typeDirectRB.setSelected(false);
            } else {
                typeCombiRB.setSelected(false);
                typeDirectRB.setSelected(true);
            }
        }
    }

    public void initialize() {
        initRadioButtons();
        initDatePickers();
        initTableView();
        initHighlight();
        serialNumberTF.setText(DEFAULT_SN);
        countTF.setDisable(true);
        enableEditCheckBox(false);
    }

    /**
     * Reads info from the form and writes it into the provided Task object.
     * @param task - Task object, to which the information, gathered from the form, will be saved.
     */
    public void readFields(ActiveTask task) {
        task.setCode(codeTF.getText());
        task.setSerialNumber(serialNumberTF.getText());
        task.setShipmentFrom(getShipmentFrom());
        task.setClient(clientTF.getText());
        task.setType(typeTF.getText());
        task.setPersonInCharge(personTF.getText());
        task.setFilm(getFilm());
        task.setFrameSize(frameSizeTF.getText());
        task.setMesh(meshTF.getText());
        task.setCombi(getCombi());
        task.setIsNewFrame(frameNewRB.isSelected());
        task.setFrameCondition(getFrameCondition());
        task.setBias(biasTF.getText());
        task.setTension(tensionTF.getText());
        task.setEmulsion(emulsionTF.getText());
        task.setDateIn(dateInPicker.getValue());
        task.setShippingMethod(getShippingMethod());
        if (dateOutPicker.getValue() != null) {
            task.setDateOut(dateOutPicker.getValue());
        } else {
            task.setDateOut(dateInPicker.getValue());
        }
        task.setDeadlineNote(getDeadlineNote());
        task.setPrintPosition(getPrintPosition());
        task.setOrderNote(orderNoteTA.getText());

        //if it is 'frame only' order, then create a dummy subtask and add it to the subtasks set
        if (frameOnlyToggle.isSelected()) {
            int count = 0;
            try {
                count = Integer.parseInt(countTF.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            Model subtask = new Model();
            subtask.setPrint("Frame Only");
            subtask.setTask(task);
            task.setSubtasks(Set.of(subtask));
            task.setCount(count);
        } else {
            int count = 0;
            for (Model subtask : subtaskObservableList) {
                subtask.setTask(task);
                count += Integer.parseInt(subtask.getCount());
            }
            task.setSubtasks(new HashSet<>(subtaskObservableList));
            task.setCount(count);
        }
    }

    /**
     * Reads info from the provided Task object and populates the form with it.
     * @param task - Task object, from which the information will be read.
     */
    public void populateWindow(TaskInterface task) {
        if (task.getCode() != null) {
            codeTF.setText(task.getCode());
        }
        serialNumberTF.setText(task.getSerialNumber());
        if (task.getShipmentFrom() != null) {
            switch (task.getShipmentFrom()) {
                case "?????????" -> shipmentSamegiRB.setSelected(true);
                case "PS ???" -> shipmentPSRB.setSelected(true);
                case "??????" -> shipmentChepanRB.setSelected(true);
            }
        }
        clientTF.setText(task.getClient());
        typeTF.setText(task.getType() != null ? task.getType() : "");
        personTF.setText(task.getPersonInCharge());
        if (task.getFilm() != null) {
            switch (task.getFilm()) {
                case "????????????" -> filmExistingRB.setSelected(true);
                case "????????????" -> filmProvidedRB.setSelected(true);
                case "????????????" -> filmNewRB.setSelected(true);
            }
        }
        frameSizeTF.setText(task.getFrameSize());
        meshTF.setText(task.getMesh());
        if (task.getCombi() != null) {
            switch (task.getCombi()) {
                case "COMBI" -> typeCombiRB.setSelected(true);
                case "?????????" -> typeDirectRB.setSelected(true);
            }
        }
        if (task.getFrameCondition() != null) {
            switch (task.getFrameCondition()) {
                case "???????????????" -> frameNewRB.setSelected(true);
                case "???????????????" -> frameExistingRB.setSelected(true);
                default -> frameDirectInputTF.setText(task.getFrameCondition());
            }
        }

        biasTF.setText(task.getBias());
        tensionTF.setText(task.getTension());
        emulsionTF.setText(task.getEmulsion());
        dateInPicker.setValue(task.getDateIn());
        if (task.getShippingMethod() != null) {
            switch (task.getShippingMethod()) {
                case "??????" -> methodPesongRB.setSelected(true);
                case "??????" -> methodPedalRB.setSelected(true);
            }
        }
        dateOutPicker.setValue(task.getDateOut());
        if (task.getDeadlineNote() != null) {
            switch (task.getDeadlineNote()) {
                case "????????????" -> amRB.setSelected(true);
                case "????????????" -> pmRB.setSelected(true);
                case "????????????" -> deliveryRB.setSelected(true);
            }
        }
        if (task.getPrintPosition() != null) {
            switch (task.getPrintPosition()) {
                case "????????????" -> positionCustomRB.setSelected(true);
                case "???????????????" -> positionCenterRB.setSelected(true);
            }
        }
        orderNoteTA.setText(task.getOrderNote());

        //check if it is a 'frame only' order
        List<Model> subtaskList = new ArrayList<>(task.getSubtasks());
        if (subtaskList.size() == 1 & subtaskList.get(0).getPrint().equals("Frame Only")) {
            setFrameOnly(true);
            frameOnlyToggle.setSelected(true);
            countTF.setText(task.getCount().toString());
        } else {
            subtaskObservableList.addAll(subtaskList);
            Collections.sort(subtaskObservableList);
        }
    }

    public void enableElements(boolean bool) {
        codeTF.setEditable(bool);
        serialNumberTF.setEditable(bool);
        shipmentChepanRB.setDisable(!bool);
        shipmentPSRB.setDisable(!bool);
        shipmentSamegiRB.setDisable(!bool);
        dateInPicker.setEditable(bool);
        dateInPicker.setDisable(!bool);
        dateOutPicker.setEditable(bool);
        dateOutPicker.setDisable(!bool);
        amRB.setDisable(!bool);
        pmRB.setDisable(!bool);
        deliveryRB.setDisable(!bool);
        methodPesongRB.setDisable(!bool);
        methodPedalRB.setDisable(!bool);
        positionCenterRB.setDisable(!bool);
        positionCustomRB.setDisable(!bool);
        orderNoteTA.setEditable(bool);
        frameOnlyToggle.setDisable(!bool);
        countTF.setEditable(bool);
        clientTF.setEditable(bool);
        typeTF.setEditable(bool);
        personTF.setEditable(bool);
        filmExistingRB.setDisable(!bool);
        filmNewRB.setDisable(!bool);
        filmProvidedRB.setDisable(!bool);
        frameSizeTF.setEditable(bool);
        meshTF.setEditable(bool);
        biasTF.setEditable(bool);
        tensionTF.setEditable(bool);
        emulsionTF.setEditable(bool);
        typeCombiRB.setDisable(!bool);
        typeDirectRB.setDisable(!bool);
        frameExistingRB.setDisable(!bool);
        frameNewRB.setDisable(!bool);
        frameDirectInputTF.setEditable(bool);
        searchClientButton.setDisable(!bool);
        searchByCodeButton.setDisable(!bool);
        editSubtaskButton.setDisable(!bool);

        if (frameOnlyToggle.isSelected()) {
            frameOnlyToggle.setDisable(!bool);
            countTF.setEditable(bool);
        } else {
//            subtasksTableView.setEditable(bool);
//            subtasksTableView.setDisable(!bool);
            addSubtaskButton.setDisable(!bool);
            removeSubtaskButton.setDisable(!bool);
        }
    }

    public void enableEditCheckBox(boolean bool) {
        editCB.setVisible(bool);
    }

    /**
     * Checks fields that must not contain empty data and highlights those fields that have null data.
     * @return - true, if no null data fields are present, false otherwise.
     */
    public boolean fieldsOK() {
        boolean flag = DialogUtilities.checkNonNullFields(textFieldHighlightMap);

        if (serialNumberTF.getText() == null || serialNumberTF.getText().equals("") || serialNumberTF.getText().equals(DEFAULT_SN)) {
            serialNumberHighlight.setVisible(true);
            flag = false;
        }

        if (dateOutPicker.getValue() == null) {
            dateOutHighlight.setVisible(true);
            flag = false;
        }

        if (subtaskObservableList.size() == 0 && (countTF.getText().equals("") || countTF.getText() == null)) {
            modelHighlight.setVisible(true);
            flag = false;
        }

        return flag;
    }

    @SneakyThrows
    private void createSubtaskDialogWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CREATE_SUBTASK));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Main.getLogo());

        CreateSubtaskDialogController controller = fxmlLoader.getController();

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.fieldsOK()) {
                event.consume();
            }
        });

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            Model subtask = controller.getSubtask();
            subtaskObservableList.add(subtask);
        }
    }

    //change the display time format to yyyy.MM.dd
    private void initDatePickers(){
        StringConverter<LocalDate> stringConverter = new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate == null)
                    return "";
                return koreanFormat.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if(dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, koreanFormat);
            }
        };

        dateInPicker.setConverter(stringConverter);
        dateOutPicker.setConverter(stringConverter);

        dateInPicker.setValue(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()));
    }

    private void makeTableViewEditable(boolean bool) {
        subtasksTableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        subtasksTableView.setEditable(bool);

        printCol.setEditable(true);
        printCol.setCellFactory(TextFieldTableCell.forTableColumn());
        printCol.setOnEditCommit(event -> {
            Model selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setPrint(newValue);
        });

        thicknessCol.setEditable(true);
        thicknessCol.setCellFactory(TextFieldTableCell.forTableColumn());
        thicknessCol.setOnEditCommit(event -> {
            Model selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setThickness(newValue);
        });

        countCol.setEditable(true);
        countCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countCol.setOnEditCommit(event -> {
            Model selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setCount(newValue);
        });
    }

    //returns result based on selected RB. Returns null if none selected.
    private String getShipmentFrom() {
        if (shipmentSamegiRB.isSelected()) {
            return "?????????";
        }
        if (shipmentPSRB.isSelected()) {
            return "PS ???";
        }
        if (shipmentChepanRB.isSelected()) {
            return "??????";
        }
        return "";
    }

    private String getFilm() {
        if (filmNewRB.isSelected()) {
            return "????????????";
        }
        if (filmExistingRB.isSelected()) {
            return "????????????";
        }
        if (filmProvidedRB.isSelected()) {
            return "????????????";
        }
        return "";
    }

    private String getCombi() {
        if (typeCombiRB.isSelected()) {
            return "COMBI";
        }
        if (typeDirectRB.isSelected()) {
            return "?????????";
        }
        return "";
    }

    private String getShippingMethod() {
        if (methodPesongRB.isSelected()) {
            return "??????";
        }
        if (methodPedalRB.isSelected()) {
            return "??????";
        }
        return "";
    }

    private String getFrameCondition() {
        if (frameNewRB.isSelected()) {
            return "???????????????";
        }
        if (frameExistingRB.isSelected()) {
            return "???????????????";
        }
        if (!frameDirectInputTF.getText().equals("") || frameDirectInputTF.getText() != null) {
            return frameDirectInputTF.getText();
        }
        return "";
    }

    private String getPrintPosition() {
        if (positionCenterRB.isSelected()) {
            return "???????????????";
        }
        if (positionCustomRB.isSelected()) {
            return "????????????";
        }
        return "";
    }

    private String getDeadlineNote() {
        if (amRB.isSelected()) {
            return "????????????";
        }
        else if (pmRB.isSelected()) {
            return "????????????";
        }
        else if (deliveryRB.isSelected()) {
            return "????????????";
        }
        return "";
    }

    private void initTableView() {
        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(subtaskObservableList.indexOf(order.getValue()) + 1)));
        printCol.setCellValueFactory(new PropertyValueFactory<>("print"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        thicknessCol.setCellValueFactory(new PropertyValueFactory<>("thickness"));

        subtasksTableView.setItems(subtaskObservableList);
    }

    private void initRadioButtons() {
        //shipment
        ToggleGroup shipment = new ToggleGroup();
        shipmentPSRB    .setToggleGroup(shipment);
        shipmentChepanRB.setToggleGroup(shipment);
        shipmentSamegiRB.setToggleGroup(shipment);

        //film
        ToggleGroup film = new ToggleGroup();
        filmNewRB       .setToggleGroup(film);
        filmProvidedRB  .setToggleGroup(film);
        filmExistingRB  .setToggleGroup(film);

        //combi
        ToggleGroup type = new ToggleGroup();
        typeDirectRB    .setToggleGroup(type);
        typeCombiRB     .setToggleGroup(type);

        //frame
        ToggleGroup frame = new ToggleGroup();
        frameExistingRB .setToggleGroup(frame);
        frameNewRB      .setToggleGroup(frame);

        //shipping method
        ToggleGroup method = new ToggleGroup();
        methodPesongRB  .setToggleGroup(method);
        methodPedalRB   .setToggleGroup(method);

        //deadline note
        ToggleGroup deadlineNote = new ToggleGroup();
        amRB            .setToggleGroup(deadlineNote);
        pmRB            .setToggleGroup(deadlineNote);
        deliveryRB      .setToggleGroup(deadlineNote);

        //printPosition
        ToggleGroup position = new ToggleGroup();
        positionCustomRB.setToggleGroup(position);
        positionCenterRB.setToggleGroup(position);
    }

    //activates appropriate fields based on boolean parameter.
    private void setFrameOnly(boolean bool) {
        subtasksTableView.setDisable(bool);
        addSubtaskButton.setDisable(bool);
        removeSubtaskButton.setDisable(bool);

        countTF.setDisable(!bool);
    }

    @SneakyThrows
    private void openClientListDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.CLIENT_LIST));
        Dialog<ButtonType> dialog = new Dialog<>();
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                e.consume();
            }
        });
        dialog.setDialogPane(dialogPane);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Main.getLogo());

        ClientListDialogController controller = fxmlLoader.getController();

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            Client selectedClient = controller.getSelectedClient();
            if (selectedClient != null) {
                clientTF.setText(selectedClient.getClient());
                typeTF.setText(selectedClient.getType());
                personTF.setText(selectedClient.getManager());
            }
        }

        if (controller.listChanged()) {
            List<String> clientsData = new ArrayList<>();
            controller.getContent().forEach(client -> {
                clientsData.add(client.convertToString());
            });
            Utilities.writeToFile("data/clients.txt", clientsData);
        }
    }

    private void initHighlight() {
        textFieldHighlightMap.put(clientTF, clientHighlight);
        textFieldHighlightMap.put(personTF, personHighlight);
        textFieldHighlightMap.put(frameSizeTF, frameSizeHighlight);
        textFieldHighlightMap.put(meshTF, meshHighlight);

        serialNumberHighlight.setVisible(false);
        clientHighlight.setVisible(false);
        personHighlight.setVisible(false);
        frameSizeHighlight.setVisible(false);
        meshHighlight.setVisible(false);
        dateOutHighlight.setVisible(false);
        modelHighlight.setVisible(false);

        serialNumberTF.addEventHandler(KeyEvent.KEY_TYPED, event -> serialNumberHighlight.setVisible(false));
        clientTF.addEventHandler(KeyEvent.KEY_TYPED, event -> clientHighlight.setVisible(false));
        personTF.addEventHandler(KeyEvent.KEY_TYPED, event -> personHighlight.setVisible(false));
        frameSizeTF.addEventHandler(KeyEvent.KEY_TYPED, event -> frameSizeHighlight.setVisible(false));
        meshTF.addEventHandler(KeyEvent.KEY_TYPED, event -> meshHighlight.setVisible(false));
        dateOutPicker.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dateOutHighlight.setVisible(false));
    }
}

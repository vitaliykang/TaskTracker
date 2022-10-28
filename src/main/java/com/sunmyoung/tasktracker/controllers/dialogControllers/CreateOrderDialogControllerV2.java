package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.pojos.Subtask;
import com.sunmyoung.tasktracker.pojos.ActiveTask;
import com.sunmyoung.tasktracker.pojos.Task;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.SneakyThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CreateOrderDialogControllerV2 {
    @Getter
    private static DateTimeFormatter koreanFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @FXML
    private CheckBox editCB;

    @FXML
    private TextField serialNumberTF;

    @FXML
    private RadioButton
            shipmentChepanRB,
            shipmentPSRB,
            shipmentSamegiRB;

    @FXML
    private TextField
            companyTF,
            personTF;

    @FXML
    private RadioButton
            filmExistingRB,
            filmNewRB,
            filmProvidedRB;

    @FXML
    private TextField
            frameSizeTF,
            meshTF,
            biasTF,
            tensionTF,
            emulsionTF;

    @FXML
    private RadioButton
            typeCombiRB,
            typeDirectRB;

    @FXML
    private RadioButton
            frameExistingRB,
            frameNewRB;


    @FXML
    private DatePicker
            dateInPicker,
            dateOutPicker;

    @FXML
    private TextField dateOutNoteTF;

    @FXML
    private RadioButton
            methodPesongRB,
            methodPedalRB;

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
    private TableView<Subtask> subtasksTableView;

    @FXML
    private TableColumn<Subtask, String>
            printCol,
            thicknessCol,
            countCol,
            orderCol;

    @FXML
    private Button
            addSubtaskButton,
            removeSubtaskButton,
            editSubtaskButton;

    @Getter
    private ObservableList<Subtask> subtaskObservableList = FXCollections.observableArrayList();

    @Getter
    private boolean subtasksChanged;

    @FXML
    void enableEdit() {
        enableElements(editCB.isSelected());
        makeTableViewEditable(editCB.isSelected());
    }

    @FXML
    void addSubtask(ActionEvent event) {
        subtasksChanged = true;
        createSubtaskDialogWindow();
    }

    @FXML
    @SneakyThrows
    void editSubtask(ActionEvent event) {
        Subtask subtask = subtasksTableView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createSubtaskDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
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
        Subtask subtask = subtasksTableView.getSelectionModel().getSelectedItem();
        subtaskObservableList.remove(subtask);
    }

    @FXML
    void frameOnlyToggleAction(ActionEvent event) {
        setFrameOnly(frameOnlyToggle.isSelected());
    }

    public void initialize() {
        initRadioButtons();
        initDatePickers();
        initTableView();
        countTF.setDisable(true);
        enableEditCheckBox(false);
    }

    private void makeTableViewEditable(boolean bool) {
        subtasksTableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        subtasksTableView.setEditable(bool);

        printCol.setEditable(true);
        printCol.setCellFactory(TextFieldTableCell.forTableColumn());
        printCol.setOnEditCommit(event -> {
            Subtask selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setPrint(newValue);
        });

        thicknessCol.setEditable(true);
        thicknessCol.setCellFactory(TextFieldTableCell.forTableColumn());
        thicknessCol.setOnEditCommit(event -> {
            Subtask selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setThickness(newValue);
        });

        countCol.setEditable(true);
        countCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countCol.setOnEditCommit(event -> {
            Subtask selectedReport = event.getTableView().getItems().get(event.getTablePosition().getRow());
            String newValue = event.getNewValue();
            selectedReport.setCount(newValue);
        });
    }

    /**
     * Reads info from the from and writes it into the provided Task object.
     * @param task - Task object, to which the information, gathered from the form, will be saved.
     */
    public void readFields(ActiveTask task) {
        task.setSerialNumber(serialNumberTF.getText());
        task.setShipmentFrom(getShipmentFrom());
        task.setClient(companyTF.getText());
        task.setPersonInCharge(personTF.getText());
        task.setFilm(getFilm());
        task.setFrameSize(frameSizeTF.getText());
        task.setMesh(meshTF.getText());
        task.setCombi(getCombi());
        task.setIsNewFrame(frameNewRB.isSelected());
        task.setBias(biasTF.getText());
        task.setTension(tensionTF.getText());
        task.setEmulsion(emulsionTF.getText());
        task.setDateIn(dateInPicker.getValue());
        task.setShippingMethod(getShippingMethod());
        task.setDateOut(dateOutPicker.getValue());
        task.setDeadlineNote(dateOutNoteTF.getText());
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
            Subtask subtask = new Subtask();
            subtask.setPrint("Frame Only");
            subtask.setTask(task);
            task.setSubtasks(Set.of(subtask));
            task.setCount(count);
        } else {
            int count = 0;
            for (Subtask subtask : subtaskObservableList) {
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
    public void populateWindow(Task task) {
        serialNumberTF.setText(task.getSerialNumber());
        if (task.getShipmentFrom() != null) {
            switch (task.getShipmentFrom()) {
                case "사메기" -> shipmentSamegiRB.setSelected(true);
                case "PS 판" -> shipmentPSRB.setSelected(true);
                case "제판" -> shipmentChepanRB.setSelected(true);
            }
        }
        companyTF.setText(task.getClient());
        personTF.setText(task.getPersonInCharge());
        if (task.getFilm() != null) {
            switch (task.getFilm()) {
                case "기존필림" -> filmExistingRB.setSelected(true);
                case "제공필림" -> filmProvidedRB.setSelected(true);
                case "신규필림" -> filmNewRB.setSelected(true);
            }
        }
        frameSizeTF.setText(task.getFrameSize());
        meshTF.setText(task.getMesh());
        if (task.getCombi() != null) {
            switch (task.getCombi()) {
                case "COMBI" -> typeCombiRB.setSelected(true);
                case "직견장" -> typeDirectRB.setSelected(true);
            }
        }

        if (task.getIsNewFrame()) {
            frameNewRB.setSelected(true);
        } else {
            frameExistingRB.setSelected(true);
        }

        biasTF.setText(task.getBias());
        tensionTF.setText(task.getTension());
        emulsionTF.setText(task.getEmulsion());
        dateInPicker.setValue(task.getDateIn());
        if (task.getShippingMethod() != null) {
            switch (task.getShippingMethod()) {
                case "배송" -> methodPesongRB.setSelected(true);
                case "배달" -> methodPedalRB.setSelected(true);
            }
        }
        dateOutPicker.setValue(task.getDateOut());
        dateOutNoteTF.setText(task.getDeadlineNote());
        if (task.getPrintPosition() != null) {
            switch (task.getPrintPosition()) {
                case "지정위치" -> positionCustomRB.setSelected(true);
                case "프레임중심" -> positionCenterRB.setSelected(true);
            }
        }
        orderNoteTA.setText(task.getOrderNote());

        //check if it is a 'frame only' order
        List<Subtask> subtaskList = new ArrayList<>(task.getSubtasks());
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
        serialNumberTF.setEditable(bool);
        shipmentChepanRB.setDisable(!bool);
        shipmentPSRB.setDisable(!bool);
        shipmentSamegiRB.setDisable(!bool);
        dateInPicker.setEditable(bool);
        dateInPicker.setDisable(!bool);
        dateOutPicker.setEditable(bool);
        dateOutPicker.setDisable(!bool);
        dateOutNoteTF.setEditable(bool);
        methodPesongRB.setDisable(!bool);
        methodPedalRB.setDisable(!bool);
        positionCenterRB.setDisable(!bool);
        positionCustomRB.setDisable(!bool);
        orderNoteTA.setEditable(bool);
        frameOnlyToggle.setDisable(!bool);
        countTF.setEditable(bool);
        companyTF.setEditable(bool);
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

        if (frameOnlyToggle.isSelected()) {
            frameOnlyToggle.setDisable(!bool);
            countTF.setEditable(bool);
        } else {
//            subtasksTableView.setEditable(bool);
//            subtasksTableView.setDisable(!bool);
            addSubtaskButton.setDisable(!bool);
            removeSubtaskButton.setDisable(!bool);
            editSubtaskButton.setDisable(!bool);
        }
    }

    public void enableEditCheckBox(boolean bool) {
        editCB.setVisible(bool);
    }

    @SneakyThrows
    private void createSubtaskDialogWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("dialogs/createSubtaskDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        CreateSubtaskDialogController controller = fxmlLoader.getController();
        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            Subtask subtask = controller.getSubtask();
            subtaskObservableList.add(subtask);
        }
    }

    //change the display time format to yyyy.MM.dd
    private void initDatePickers(){
        StringConverter<LocalDate> stringConverter = new StringConverter<LocalDate>() {
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

    //returns result based on selected RB. Returns null if none selected.
    private String getShipmentFrom() {
        if (shipmentSamegiRB.isSelected()) {
            return "사메기";
        }
        if (shipmentPSRB.isSelected()) {
            return "PS 판";
        }
        if (shipmentChepanRB.isSelected()) {
            return "제판";
        }
        return "null";
    }

    private String getFilm() {
        if (filmNewRB.isSelected()) {
            return "신규필림";
        }
        if (filmExistingRB.isSelected()) {
            return "기존필림";
        }
        if (filmProvidedRB.isSelected()) {
            return "제공필림";
        }
        return "null";
    }

    private String getCombi() {
        if (typeCombiRB.isSelected()) {
            return "COMBI";
        }
        if (typeDirectRB.isSelected()) {
            return "직견장";
        }
        return "null";
    }

    private String getShippingMethod() {
        if (methodPesongRB.isSelected()) {
            return "배송";
        }
        if (methodPedalRB.isSelected()) {
            return "배달";
        }
        return "null";
    }

    private String getPrintPosition() {
        if (positionCenterRB.isSelected()) {
            return "프레임중심";
        }
        if (positionCustomRB.isSelected()) {
            return "지정위치";
        }
        return "null";
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
        editSubtaskButton.setDisable(bool);

        countTF.setDisable(!bool);
    }
}

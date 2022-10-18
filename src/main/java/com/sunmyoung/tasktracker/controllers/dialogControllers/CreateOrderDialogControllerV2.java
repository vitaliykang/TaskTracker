package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.pojos.Subtask;
import com.sunmyoung.tasktracker.pojos.Task;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.SneakyThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

public class CreateOrderDialogControllerV2 {
    @Getter
    private static DateTimeFormatter koreanFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

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
    private TableView<Subtask> subtasksTableView;

    @FXML
    private TableColumn<Subtask, String>
            printCol,
            thicknessCol,
            countCol,
            orderCol;

    @Getter
    private ObservableList<Subtask> subtasks = FXCollections.observableArrayList();

    @Getter
    private boolean subtasksChanged;

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
            subtasks.remove(subtask);
            subtask = controller.getSubtask();
            subtasks.add(subtask);
            Collections.sort(subtasks);
        }
    }

    @FXML
    void removeSubtask(ActionEvent event) {
        subtasksChanged = true;
        Subtask subtask = subtasksTableView.getSelectionModel().getSelectedItem();
        subtasks.remove(subtask);
    }

    @FXML
    void frameOnlyToggleAction(ActionEvent event) {
        if (frameOnlyToggle.isSelected()) {
            subtasksTableView.setDisable(true);
        } else {
            subtasksTableView.setDisable(false);
        }
    }

    public void initialize() {
        initRadioButtons();
        initDatePickers();
        initTableView();
    }

    /**
     * Reads info from the from and writes it into the provided Task object.
     * @param task - Task object, to which the information, gathered from the form, will be saved.
     */
    public void readFields(Task task) {
        task.setSerialNumber(serialNumberTF.getText());
        task.setShipmentFrom(getShipmentFrom());
        task.setClient(companyTF.getText());
        task.setPersonInCharge(personTF.getText());
        task.setFilm(getFilm());
        task.setFrameSize(frameSizeTF.getText());
        task.setMesh(meshTF.getText());
        task.setCombi(typeCombiRB.isSelected() ? "COMBI" : "직견장");
        task.setIsNewFrame(frameNewRB.isSelected());
        task.setBias(biasTF.getText());
        task.setTension(tensionTF.getText());
        task.setEmulsion(emulsionTF.getText());
        task.setDateIn(dateInPicker.getValue());
        task.setShippingMethod(methodPesongRB.isSelected() ? "배송" : "배달");
        task.setDateOut(dateOutPicker.getValue());
        task.setDeadlineNote(dateOutNoteTF.getText());
        task.setPrintPosition(positionCenterRB.isSelected() ? "프레임중심" : "지정위치");
        task.setOrderNote(orderNoteTA.getText());

        int count = 0;
        for (Subtask subtask : subtasks) {
            subtask.setTask(task);
            count += Integer.parseInt(subtask.getCount());
        }
        task.setSubtasks(new HashSet<>(subtasks));
        task.setCount(count);
    }

    /**
     * Reads info from the provided Task object and populates the form with it.
     * @param task - Task object, from which the information will be read.
     */
    public void populateWindow(Task task) {
        serialNumberTF.setText(task.getSerialNumber());
        switch (task.getShipmentFrom()) {
            case "사메기" -> shipmentSamegiRB.setSelected(true);
            case "PS 판" -> shipmentPSRB.setSelected(true);
            case "제판" -> shipmentChepanRB.setSelected(true);
        }
        companyTF.setText(task.getClient());
        personTF.setText(task.getPersonInCharge());
        switch (task.getFilm()) {
            case "기존필림" -> filmExistingRB.setSelected(true);
            case "제공필림" -> filmProvidedRB.setSelected(true);
            case "신규필림" -> filmNewRB.setSelected(true);
        }
        frameSizeTF.setText(task.getFrameSize());
        meshTF.setText(task.getMesh());
        switch (task.getCombi()) {
            case "COMBI" -> typeCombiRB.setSelected(true);
            case "직견장" -> typeDirectRB.setSelected(true);
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
        switch (task.getShippingMethod()) {
            case "배송" -> methodPesongRB.setSelected(true);
            case "배달" -> methodPedalRB.setSelected(true);
        }
        dateOutPicker.setValue(task.getDateOut());
        dateOutNoteTF.setText(task.getDeadlineNote());
        switch (task.getPrintPosition()) {
            case "프레임중심" -> positionCenterRB.setSelected(true);
            case "지정위치" -> positionCustomRB.setSelected(true);
        }
        orderNoteTA.setText(task.getOrderNote());
        subtasks.addAll(task.getSubtasks());
        Collections.sort(subtasks);
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
            subtasks.add(subtask);
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
        return null;
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
        return null;
    }

    private void initTableView() {
        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(subtasksTableView.getItems().indexOf(order.getValue()) + 1)));
        printCol.setCellValueFactory(new PropertyValueFactory<>("print"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        thicknessCol.setCellValueFactory(new PropertyValueFactory<>("thickness"));

        subtasksTableView.setItems(subtasks);
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
}

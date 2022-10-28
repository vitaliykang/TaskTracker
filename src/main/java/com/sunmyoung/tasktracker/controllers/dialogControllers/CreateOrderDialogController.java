package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.pojos.Subtask;
import com.sunmyoung.tasktracker.pojos.ActiveTask;
import com.sunmyoung.tasktracker.repositories.TaskRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;

public class CreateOrderDialogController {
    private ObservableList<Subtask> subtasks = FXCollections.observableArrayList();

    @FXML
    private DatePicker datepicker;

    @FXML
    private TextArea noteTF;

    @FXML
    private CheckBox combiBox;

    @FXML
    private RadioButton aluminumRB,
            solidRB;

    @FXML
    private TextField biasTF,
            deadlineNoteTF,
            emulsionTF,
            frameSizeTF,
            meshTF,
            clientTF,
            meshSizeTF;

    @FXML
    private TableView<Subtask> tableView;

    @FXML
    private TableColumn<Subtask, String> printCol,
            orderCol,
            thicknessCol,
            countCol;

    @FXML
    void addPrint(ActionEvent event) {
//        Subtask subtask = new Subtask();
//        subtask.setPrint("New Subtask");
//        subtasks.add(subtask);

        createSubtaskDialogWindow();
    }

    @FXML
    void removePrint(ActionEvent event) {
        Subtask subtask = tableView.getSelectionModel().getSelectedItem();
        subtasks.remove(subtask);
    }

    @FXML
    void combiCheck(ActionEvent event) {
        if (combiBox.isSelected()) {
            meshSizeTF.setDisable(false);
            meshSizeTF.setEditable(true);
        } else {
            meshSizeTF.setEditable(false);
            meshSizeTF.setDisable(true);
        }
    }

    @FXML
    public void clientClicked(MouseEvent event){
        System.out.println("Client Changed");
    }

    public void initialize() {
        //combi
        meshSizeTF.setEditable(false);
        meshSizeTF.setDisable(true);

        initToggleButtons();
        initDatePicker();
        initTableView();
        activateCells();

        tableView.setItems(subtasks);
    }

    private void initTableView() {
//        tableView.setEditable(true);
//        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);

        orderCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<>(Integer.toString(tableView.getItems().indexOf(order.getValue()) + 1)));
        printCol.setCellValueFactory(new PropertyValueFactory<>("print"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        thicknessCol.setCellValueFactory(new PropertyValueFactory<>("thickness"));
    }

    public void createTask() {
        ActiveTask task = new ActiveTask();
        readFields(task);

        TaskRepository.save(task);
    }

    public void populateWindow(ActiveTask task){
        clientTF.setText(task.getClient());
        datepicker.setValue(task.getDateOut());
        deadlineNoteTF.setText(task.getDeadlineNote());
        frameSizeTF.setText(task.getFrameSize());
        if (task.getIsAluminum()) {
            aluminumRB.setSelected(true);
        } else {
            solidRB.setSelected(true);
        }
        if (task.getCombi().equals("직견장")) {
            combiBox.setSelected(false);
            meshSizeTF.setDisable(true);
            meshSizeTF.setEditable(false);
        } else {
            combiBox.setSelected(true);
            meshSizeTF.setDisable(false);
            meshSizeTF.setEditable(true);
            meshSizeTF.setText(task.getCombi());
        }
        biasTF.setText(task.getBias().toString());
        meshTF.setText(task.getMesh());
        emulsionTF.setText(task.getEmulsion());
        noteTF.setText(task.getDeadlineNote());

        subtasks.addAll(task.getSubtasks());
    }

    public void readFields(ActiveTask task) {
        task.setClient(clientTF.getText());
        task.setDateOut(datepicker.getValue());
        task.setDeadlineNote(deadlineNoteTF.getText());
        task.setFrameSize(frameSizeTF.getText());
        task.setIsAluminum(aluminumRB.isSelected());
        task.setBias(biasTF.getText());
        task.setMesh(meshTF.getText());
        task.setEmulsion(emulsionTF.getText());
        task.setDeadlineNote(noteTF.getText());

        task.getSubtasks().forEach(subtask -> {
            subtask.setTask(null);
        });

        //CHANGED HERE
//        List<Subtask> subtaskList = tableView.getItems();
//        subtaskList.forEach(subtask -> subtask.setTask(task));
        subtasks.forEach(subtask -> subtask.setTask(task));
        task.setSubtasks(new HashSet<>(subtasks));

        if (combiBox.isSelected()) {
            task.setCombi(meshSizeTF.getText());
        } else {
            task.setCombi("직견장");
        }

        task.setCount(getFrameCount());
    }

    private void initToggleButtons() {
        ToggleGroup aluminum = new ToggleGroup();
        aluminumRB.setToggleGroup(aluminum);
        solidRB.setToggleGroup(aluminum);
        solidRB.setSelected(true);
    }

    private void initDatePicker(){
        datepicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            @Override
            public String toString(LocalDate localDate) {
                if(localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if(dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
    }

    private void activateCells() {
        activatePrintCol();
        activateThicknessCol();
        activateCountCol();
    }

    private void activatePrintCol() {
        printCol.setEditable(true);
        printCol.setCellFactory(TextFieldTableCell.forTableColumn());
        printCol.setOnEditCommit(event -> {
            Subtask selectedSubtask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            selectedSubtask.setPrint(event.getNewValue());
        });
    }

    private void activateThicknessCol() {
        thicknessCol.setEditable(true);
        thicknessCol.setCellFactory(TextFieldTableCell.forTableColumn());
        thicknessCol.setOnEditCommit(event -> {
            Subtask selectedSubtask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            selectedSubtask.setPrint(event.getNewValue());
        });
    }

    private void activateCountCol() {
        countCol.setEditable(true);
        countCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countCol.setOnEditCommit(event -> {
            Subtask selectedSubtask = event.getTableView().getItems().get(event.getTablePosition().getRow());
            selectedSubtask.setPrint(event.getNewValue());
        });
    }

    private Integer getFrameCount() {
        int total = 0;
        for (Subtask subtask : tableView.getItems()) {
            try {
                int count = Integer.parseInt(subtask.getCount());
                total += count;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return total;
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
}


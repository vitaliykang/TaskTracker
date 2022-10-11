package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.pojos.Subtask;
import com.sunmyoung.tasktracker.pojos.Task;
import com.sunmyoung.tasktracker.repositories.Database;
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
import javafx.util.StringConverter;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public void editTask(long id) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Task task = session.createQuery("FROM Task t where t.id = :id", Task.class).setParameter("id", id).uniqueResult();
        task = readFields(task);
        session.persist(task);

        transaction.commit();
        session.close();
    }

    public void createTask() {
        Task task = new Task();
        task = readFields(task);

        TaskRepository.save(task);
    }

    public void populateWindow(Long id){
        Task task = TaskRepository.get(id);

        clientTF.setText(task.getClient());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        datepicker.setValue(LocalDate.parse(task.getDeadlineDate(), formatter));
        deadlineNoteTF.setText(task.getDeadlineNote());
        frameSizeTF.setText(task.getSize());
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
        noteTF.setText(task.getNote());

        ObservableList<Subtask> subtasks = FXCollections.observableList(task.getSubtasks());
        tableView.setItems(subtasks);
    }

    private Task readFields(Task task) {
        task.setClient(clientTF.getText());
        task.setDeadlineDate(datepicker.getValue().toString());
        task.setDeadlineNote(deadlineNoteTF.getText());
        task.setSize(frameSizeTF.getText());
        task.setIsAluminum(aluminumRB.isSelected());
        task.setBias(Double.parseDouble(biasTF.getText()));
        task.setMesh(meshTF.getText());
        task.setEmulsion(emulsionTF.getText());
        task.setNote(noteTF.getText());
        subtasks.forEach(subtask -> subtask.setTask(task));
        task.setSubtasks(subtasks);

        if (combiBox.isSelected()) {
            task.setCombi(meshSizeTF.getText());
        } else {
            task.setCombi("직견장");
        }

        return task;
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


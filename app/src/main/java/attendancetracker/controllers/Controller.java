package attendancetracker.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Controller {
  @FXML
  private Button addPerson;
  @FXML
  private ListView<Button> people_list;
  @FXML
  private Button attendanceButton;
  @FXML
  private Button usersButton;
  @FXML
  private BorderPane usersBorderPane;
  @FXML
  private BorderPane attendanceBorderPane;
  @FXML
  private ListView<Button> attendance_list;
  @FXML private ComboBox<String> comboBox;
  @FXML private ListView<Label> attendance_status_list;
  private List<Button> presentList = new ArrayList<Button>();
  private List<Button> tardyList = new ArrayList<Button>();
  private List<Button> absentList = new ArrayList<Button>();

  @FXML
  private void addPerson(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Student name");
    dialog.setHeaderText("Please enter your student name");
    String name = null;
    boolean valid = false;

    while (!valid) {
      Optional<String> resukt = dialog.showAndWait();
      if (resukt.isPresent()) {
        name = resukt.get().trim();
        if (name.isEmpty()) {
          dialog.setHeaderText("Student name cannot be empty!");
        } else {
          valid = true;
        }
      } else {
        return;
      }
    }
    System.out.println(name);
    Button student = new Button(name);
    ContextMenu ctxMenu = new ContextMenu();
    MenuItem remove = new MenuItem("Remove student");
    remove.setOnAction(e -> {
      Alert confirmation = new Alert(AlertType.CONFIRMATION, "Do you want to remove this student?", ButtonType.YES,
          ButtonType.NO);
      confirmation.setTitle("Confirmation");
      confirmation.setHeaderText("Do you really want to remove this student?");
      Optional<ButtonType> res = confirmation.showAndWait();
      if (res.isPresent() && res.get() == ButtonType.YES) {
        people_list.getItems().remove(student);
      }
    });
    MenuItem rename = new MenuItem("Rename student");
    rename.setOnAction(e -> {
      TextInputDialog renameDialog = new TextInputDialog(student.getText());
      renameDialog.setTitle("Rename");
      renameDialog.setHeaderText("Rename student");
      renameDialog.showAndWait().ifPresent(newName -> student.setText(newName));
    });
    ctxMenu.getItems().addAll(rename, remove);
    student.setContextMenu(ctxMenu);

    student.setMaxWidth(200);
    people_list.getItems().add(student);
  };

  @FXML
  private void usersPage(ActionEvent event) {
    usersBorderPane.setVisible(true);
    attendanceBorderPane.setVisible(false);
  }

  @FXML
  private void attendancePage(ActionEvent event) {
    usersBorderPane.setVisible(false);
    attendanceBorderPane.setVisible(true);
    attendance_list.getItems().clear();
    for (Button button : people_list.getItems()) {
      Button appended = new Button(button.getText());
      attendance_list.getItems().addAll(appended);
      ContextMenu ctxMenu = new ContextMenu();
      MenuItem absent = new MenuItem("Mark Absent");
      MenuItem present = new MenuItem("Mark Present");
      MenuItem tardy = new MenuItem("Mark Tardy");
      ctxMenu.getItems().addAll(absent, present, tardy);
      appended.setContextMenu(ctxMenu);
      absent.setOnAction(e -> {
        absentList.add(appended);
        attendance_list.getItems().remove(appended);
      });
      tardy.setOnAction(e -> {
        tardyList.add(appended);
        attendance_list.getItems().remove(appended);
      });
    }
  }

  @FXML
  public void initialize() {
    ObservableList<String> items = FXCollections.observableArrayList("Absences", "Tardies", "Present");
    comboBox.setItems(items);
    comboBox.setButtonCell(new ListCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item);
        }
      }
    });
    comboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
      @Override
      public ListCell<String> call(ListView<String> param) {
        return new ListCell<String>() {
          @Override
          protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText(null);
              setGraphic(null);
            } else {
              HBox hbox = new HBox();
              Button button = new Button(item);
              button.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, e -> {
                comboBox.setValue(item);
                comboBox.hide();
                e.consume();
                if (item.equals("Tardies")) {
                  attendance_status_list.getItems().clear();
                  for (Button student : tardyList) {
                    attendance_status_list.getItems().add(new Label(student.getText()));
                  }
                }
              });
              HBox.setHgrow(button, Priority.ALWAYS);
              button.setMaxWidth(135);
              HBox.setMargin(param, new Insets(0, 0, 0, 10));
              hbox.setMaxWidth(Double.MAX_VALUE);
              hbox.getChildren().add(button);
              setGraphic(hbox);
              setText(null);
            }
          }
        };
      }
    });

    attendance_list.setCellFactory(lv -> new ListCell<Button>() {
      @Override
      protected void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
        } else {
          item.setMaxWidth(Double.MAX_VALUE);
          item.prefWidthProperty().bind(widthProperty().subtract(15));
          setGraphic(item);
        }
      }
    });
  }

}
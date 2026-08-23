package attendancetracker.controllers;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class Controller {
  @FXML private Button addPerson;
  @FXML private ListView<Button> people_list;

  @FXML private void addPerson(ActionEvent event) {
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
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "Do you want to remove this student?", ButtonType.YES, ButtonType.NO);
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
}
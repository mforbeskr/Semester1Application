package kloeverly.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kloeverly.domain.Resident;
import kloeverly.domain.Task;

import java.time.LocalDate;

public class NewTaskController
{

  @FXML private TextArea descriptionField;
  @FXML private TextField pointsField;
  @FXML private DatePicker datePicker;
  @FXML public Label titleLabel;

  private Resident resident;
  private Runnable saveCallback;
  private Task taskToEdit;

  public void setResidentAndCallback(Resident resident, Runnable saveCallback)
  {
    this.resident = resident;
    this.saveCallback = saveCallback;
  }

  public void setEditingTask(Task task)
  {
    this.taskToEdit = task;
    if (titleLabel != null)
      titleLabel.setText("Rediger opgave");

    descriptionField.setText(task.getDescription());
    pointsField.setText(String.valueOf(task.getPointsForTask()));
    datePicker.setValue(task.getDate());
  }

  public void setSaveCallback(Runnable saveCallback)
  {
    this.saveCallback = saveCallback;
  }

  public void setResident(Resident resident)
  {
    this.resident = resident;
  }

  @FXML private void onSave()
  {
    String desc = descriptionField.getText().trim();
    LocalDate date = datePicker.getValue();

    int points;
    try
    {
      points = Integer.parseInt(pointsField.getText().trim());
    }
    catch (NumberFormatException e)
    {
      showError("Fejl", "Point skal være et tal");
      return;
    }

    if (desc.isEmpty() || date == null)
    {
      showError("Fejl", "Alle felter skal udfyldes");
      return;
    }

    int currentSum = resident.getTasks().stream()
        .mapToInt(Task::getPointsForTask).sum();

    int futureSum = currentSum;

    if (taskToEdit != null)
      futureSum = currentSum - taskToEdit.getPointsForTask() + points;
    else
      futureSum = currentSum + points;

    if (futureSum < 0)
    {
      showError("Ugyldig handling",
          "Dette vil give negativ pointbalance for " + resident.getName());
      return;
    }

    if (taskToEdit != null)
    {
      taskToEdit.setDescription(desc);
      taskToEdit.setDate(date);
      taskToEdit.setPointsForTask(points);
    }
    else
    {
      resident.addTask(new Task(desc, date, points));
    }

    if (saveCallback != null)
      saveCallback.run();

    closeWindow();
  }

  @FXML private void onCancel()
  {
    closeWindow();
  }

  private void closeWindow()
  {
    Stage stage = (Stage) descriptionField.getScene().getWindow();
    stage.close();
  }

  private void showError(String header, String content)
  {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fejl");
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.initOwner(descriptionField.getScene().getWindow());
    alert.showAndWait();
  }
}

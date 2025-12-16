package kloeverly.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import kloeverly.domain.Resident;
import kloeverly.domain.Task;
import kloeverly.persistence.DataContainer;
import kloeverly.persistence.DataManager;

import java.io.IOException;
import java.util.List;

public class TaskMainViewController extends BaseController
{

  @FXML private ListView<Resident> residentListView;
  @FXML private ListView<Task> taskListView;
  @FXML private Label residentPointsLabel;

  private DataContainer dataContainer;
  private DataManager dataManager;

  public void setData(DataContainer container, DataManager manager)
  {
    this.dataContainer = container;
    this.dataManager = manager;
  }

  @FXML private void initialize()
  {
    residentListView.setCellFactory(_ -> new ListCell<>()
    {
      @Override protected void updateItem(Resident item, boolean empty)
      {
        super.updateItem(item, empty);
        if (empty || item == null)
          setText(null);
        else
          setText(" Husnr. " + item.getHouseNumber() + "\t\t" + item.getName());
      }

    });

    taskListView.setCellFactory(_ -> new ListCell<>()
    {
      @Override protected void updateItem(Task task, boolean empty)
      {
        super.updateItem(task, empty);
        if (empty || task == null)
        {
          setText(null);
        }
        else
        {
          setText(
              task.getDate() + "\t" + "Beskrivelse: " + task.getDescription()
                  + "\t" + "Point: " + task.getPointsForTask());
        }
      }
    });
  }

  public void initAfterInjection()
  {
    try
    {
      List<Resident> loaded = dataManager.loadResidents();
      dataContainer.setAll(loaded);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    dataContainer.ensureFaellesExists();
    residentListView.setItems(dataContainer.getResidents());
    dataContainer.sort();

    // Update task list when resident changes
    residentListView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldResident, newResident) -> {
          if (newResident != null)
          {
            taskListView.setItems(newResident.getTasks());
          }
          else
          {
            taskListView.getItems().clear();
          }
          updateResidentPoints(newResident);
        });
  }

  //for Live showing Points
  private void updateResidentPoints(Resident resident)
  {
    if (resident == null)
    {
      residentPointsLabel.setText("Point: –");
      return;
    }

    int total = resident.getTasks().stream().mapToInt(Task::getPointsForTask)
        .sum();

    residentPointsLabel.setText("Point: " + Math.max(0, total));
  }

  @FXML protected void onCreateTask()
  {
    Resident selected = residentListView.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      showAlert("Vælg en beboer først");
      return;
    }

    Stage owner = (Stage) residentListView.getScene().getWindow();
    TaskModal.openCreate(selected, owner, () -> {
      taskListView.refresh();
      updateResidentPoints(selected);
      persistAll();
    });
  }

  @FXML protected void onEditTask()
  {
    Resident selected = residentListView.getSelectionModel().getSelectedItem();
    Task task = taskListView.getSelectionModel().getSelectedItem();
    if (selected == null || task == null)
      return;

    Stage owner = (Stage) residentListView.getScene().getWindow();
    TaskModal.openEdit(selected, task, owner, () -> {
      taskListView.refresh();
      updateResidentPoints(selected);
      persistAll();
    });
  }

  @FXML protected void onDeleteTask()
  {
    Resident resident = residentListView.getSelectionModel().getSelectedItem();
    Task task = taskListView.getSelectionModel().getSelectedItem();

    if (resident == null || task == null)
    {
      showAlert("Vælg en opgave");
      return;
    }

    boolean ok = confirm("Slet opgave",
        "Er du sikker på, at du vil slette denne opgave?\n\n" + "Dato: "
            + task.getDate() + "\nBeskrivelse: " + task.getDescription()
            + "\nPoint: " + task.getPointsForTask());

    if (!ok)
      return;

    resident.removeTask(task);
    taskListView.refresh();
    updateResidentPoints(resident);
    persistAll();
  }

  @FXML protected void onSeeTask()
  {
    Task task = taskListView.getSelectionModel().getSelectedItem();
    if (task == null)
      return;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Opgave detaljer");
    alert.setHeaderText("Opgave");
    alert.setContentText(task.toString());
    alert.showAndWait();
  }

  private void showAlert(String message)
  {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Advarsel");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void persistAll()
  {
    try
    {
      dataManager.saveResidents(dataContainer.getResidents());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @FXML protected void onMainMenuClick()
  {
    goToMainMenu();
  }
}

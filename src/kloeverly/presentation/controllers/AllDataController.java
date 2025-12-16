package kloeverly.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import kloeverly.domain.Resident;
import kloeverly.domain.Task;
import kloeverly.persistence.DataContainer;
import kloeverly.persistence.DataManager;

public class AllDataController extends BaseController
{

  @FXML private ListView<Resident> residentListView;
  @FXML private ListView<Task> taskListView;
  @FXML private Label summaryLabel;

  private DataContainer dataContainer;

  public void setData(DataContainer container, DataManager manager)
  {
    this.dataContainer = container;
  }

  public void initAfterInjection()
  {
    residentListView.setItems(dataContainer.getResidents());

    residentListView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldR, newR) -> {
          if (newR != null)
          {
            taskListView.setItems(newR.getTasks());
          }
          else
          {
            taskListView.getItems().clear();
          }
        });

    configureCells();
    updateSummary();
  }

  private void configureCells()
  {

    residentListView.setCellFactory(_ -> new ListCell<>()
    {
      @Override protected void updateItem(Resident r, boolean empty)
      {
        super.updateItem(r, empty);
        if (empty || r == null)
        {
          setText(null);
        }
        else
        {
          setText(r.getName() + " (Hus " + r.getHouseNumber() + ") – "
              + r.getPoints() + " point");
        }
      }
    });

    taskListView.setCellFactory(_ -> new ListCell<>()
    {
      @Override protected void updateItem(Task t, boolean empty)
      {
        super.updateItem(t, empty);
        if (empty || t == null)
        {
          setText(null);
        }
        else
        {
          setText(t.getDate() + "  " + t.getDescription() + " ("
              + t.getPointsForTask() + " point)");
        }
      }
    });
  }

  private void updateSummary()
  {
    int residents = dataContainer.getResidents().size();

    int tasks = dataContainer.getResidents().stream()
        .mapToInt(r -> r.getTasks().size()).sum();

    int points = dataContainer.getResidents().stream()
        .mapToInt(Resident::getPoints).sum();

    summaryLabel.setText(
        "Beboere: " + residents + " | Opgaver: " + tasks + " | Samlede point: "
            + points);
  }

  @FXML protected void onMainMenuClick()
  {
    goToMainMenu();
  }
}


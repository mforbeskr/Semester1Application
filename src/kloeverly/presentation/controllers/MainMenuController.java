package kloeverly.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import kloeverly.domain.Task;
import kloeverly.presentation.core.ViewManager;
import kloeverly.persistence.DataContainer;
import kloeverly.persistence.DataManager;
import kloeverly.domain.Resident;

public class MainMenuController
{

  @FXML private Label faellesPointsLabel;

  private ViewManager vm;
  private DataContainer dataContainer;
  private DataManager dataManager;

  public void setViewManager(ViewManager vm)
  {
    this.vm = vm;
  }

  public void setData(DataContainer container, DataManager manager)
  {
    this.dataContainer = container;
    this.dataManager = manager;
  }

  public void initAfterInjection()
  {
    updateFaellesPoints();
  }

  // Fælles-point label

  private void updateFaellesPoints()
  {
    if (dataContainer == null)
      return;

    Resident faelles = dataContainer.getResidents().stream()
        .filter(r -> "Fælles".equalsIgnoreCase(r.getName())).findFirst()
        .orElse(null);

    int points = 0;
    if (faelles != null)
    {
      points = faelles.getTasks().stream().mapToInt(Task::getPointsForTask)
          .sum();
    }

    faellesPointsLabel.setText(String.valueOf(points));
  }

  @FXML private void onOpenResidents()
  {
    vm.show("Resident");
  }

  @FXML private void onOpenTasks()
  {
    vm.show("Task");
  }

  @FXML private void onOpenAllData()
  {
    vm.show("Data");
  }

}

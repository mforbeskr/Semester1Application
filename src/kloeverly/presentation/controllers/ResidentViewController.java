package kloeverly.presentation.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kloeverly.persistence.DataContainer;
import kloeverly.persistence.DataManager;
import kloeverly.domain.Resident;

import java.io.IOException;
import java.util.List;

public class ResidentViewController extends BaseController
{

  @FXML private ListView<Resident> residentListView;
  private DataContainer dataContainer;
  private DataManager dataManager;

  public ResidentViewController()
  {
  }

  public void setData(DataContainer container, DataManager manager)
  {
    this.dataContainer = container;
    this.dataManager = manager;
    initAfterInjection();
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
      e.printStackTrace();
    }

    dataContainer.ensureFaellesExists();
    residentListView.setItems(dataContainer.getResidents());
    dataContainer.sort();
  }

  @FXML private void onCreateResident()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fxml/NewResidentView.fxml"));
      Parent root = loader.load();
      NewResidentController controller = loader.getController();
      controller.setDataContainer(dataContainer, this::persistAll);

      Stage owner = (Stage) residentListView.getScene().getWindow();
      Stage dialog = new Stage();
      dialog.setTitle("Opret ny beboer");
      dialog.initOwner(owner);
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.setResizable(false);
      dialog.setScene(new Scene(root));
      dialog.showAndWait();

    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
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

  public Resident getSelectedResident()
  {
    return residentListView.getSelectionModel().getSelectedItem();
  }

  @FXML private void onSeeDetailsResident()
  {
    Resident selected = getSelectedResident();
    if (selected != null)
    {
      Stage owner = (Stage) residentListView.getScene().getWindow();
      ResidentDetailsModal.showDetails(selected, owner);
    }
  }

  @FXML private void onEditResident()
  {
    Resident selected = getSelectedResident();
    if (selected == null)
      return;

    try
    {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fxml/NewResidentView.fxml"));
      Parent root = loader.load();
      NewResidentController controller = loader.getController();

      controller.setDataContainer(dataContainer, this::persistAll);
      controller.setEditingResident(selected);

      Stage owner = (Stage) residentListView.getScene().getWindow();
      Stage dialog = new Stage();
      dialog.setTitle("Rediger beboer");
      dialog.initOwner(owner);
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.setResizable(false);
      dialog.setScene(new Scene(root));
      dialog.showAndWait();

    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }

  @FXML protected void onDeleteResident()
  {
    Resident resident = residentListView.getSelectionModel().getSelectedItem();
    if (resident == null)
      return;

    if (isFaelles(resident))
    {
      showWarning();
      return;
    }

    boolean ok = confirm("Slet beboer",
        "Er du sikker på, at du vil slette beboeren:\n\n" + resident.getName()
            + " (Husnr. " + resident.getHouseNumber() + ")\n\n"
            + "Alle opgaver vil også blive slettet!");

    if (!ok)
      return;

    dataContainer.removeResident(resident);
    persistAll();
  }

  private boolean isFaelles(Resident resident)
  {
    return resident.getName().equalsIgnoreCase("fælles")
        || resident.getHouseNumber() == 0;
  }

  private void showWarning()
  {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Kan ikke slettes");
    alert.setHeaderText(null);
    alert.setContentText(
        "Fælleskontoen kan ikke slettes.\n\nDen bruges til fælles opgaver og point.");
    alert.showAndWait();
  }

  @FXML protected void onMainMenuClick()
  {
    goToMainMenu();
  }
}

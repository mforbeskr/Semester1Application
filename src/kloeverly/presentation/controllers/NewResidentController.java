package kloeverly.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kloeverly.domain.Resident;
import kloeverly.persistence.DataContainer;

import java.time.LocalDate;

public class NewResidentController
{

  @FXML private TextField nameField;
  @FXML private DatePicker birthDatePicker;
  @FXML private TextField houseNumberField;
  @FXML public Label titleLabel;

  private DataContainer dataContainer;
  private Runnable saveCallback;

  private Resident residentToEdit;

  public void setDataContainer(DataContainer container, Runnable saveCallback)
  {
    this.dataContainer = container;
    this.saveCallback = saveCallback;
  }

  public void setEditingResident(Resident resident)
  {
    if (resident == null)
      return;

    this.residentToEdit = resident;

    if (titleLabel != null)
      titleLabel.setText("Rediger beboer");

    nameField.setText(resident.getName());
    birthDatePicker.setValue(resident.getDateOfBirth());
    houseNumberField.setText(String.valueOf(resident.getHouseNumber()));
  }

  @FXML private void onSave()
  {
    String name = nameField.getText().trim();
    String houseStr = houseNumberField.getText().trim();
    LocalDate dob = birthDatePicker.getValue();

    if (name.isEmpty() || houseStr.isEmpty() || dob == null)
    {
      showError("Udfyld venligst alle felter",
          "Navn, husnummer og fødselsdato kræves.");
      return;
    }

    int houseNumber;
    try
    {
      houseNumber = Integer.parseInt(houseStr);
    }
    catch (NumberFormatException e)
    {
      showError("Forkert format", "Husnummer skal være et tal.");
      return;
    }

    if (residentToEdit != null)
    {

      residentToEdit.setName(name);
      residentToEdit.setHouseNumber(houseNumber);

    }
    else
    {

      Resident resident = Resident.createNew(name, houseNumber, dob);
      if (dataContainer != null)
      {
        dataContainer.addResident(resident);
      }
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
    Stage stage = (Stage) nameField.getScene().getWindow();
    stage.close();
  }

  private void showError(String header, String content)
  {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fejl");
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.initOwner(nameField.getScene().getWindow());
    alert.showAndWait();
  }
}

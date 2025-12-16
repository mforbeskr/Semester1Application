package kloeverly.presentation.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kloeverly.domain.Resident;

public class ResidentDetailsModal
{

  public static void showDetails(Resident resident, Stage owner)
  {
    if (resident == null)
      return;

    Label detailsLabel = new Label(resident.toString());
    detailsLabel.setWrapText(true);

    Button closeBtn = new Button("Luk");
    closeBtn.setOnAction(
        _ -> ((Stage) closeBtn.getScene().getWindow()).close());

    VBox layout = new VBox(14, detailsLabel, closeBtn);
    layout.setPadding(new Insets(20));
    layout.setStyle("-fx-font-size: 14px;");

    Stage dialog = new Stage();
    dialog.initOwner(owner);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Beboer detaljer");
    dialog.setScene(new Scene(layout));
    dialog.setResizable(false);
    dialog.showAndWait(); // blocks until closed
  }
}

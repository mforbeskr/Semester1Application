package kloeverly.presentation.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import kloeverly.presentation.core.ViewManager;

public class BaseController
{

  protected ViewManager viewManager;

  public void setViewManager(ViewManager viewManager)
  {
    this.viewManager = viewManager;
  }

  protected void goToMainMenu()
  {
    if (viewManager != null)
    {
      viewManager.show("MainMenu");
    }
  }

  protected boolean confirm(String title, String message)
  {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    return alert.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
  }

}

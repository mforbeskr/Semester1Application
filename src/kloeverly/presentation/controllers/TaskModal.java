package kloeverly.presentation.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kloeverly.domain.Resident;
import kloeverly.domain.Task;

import java.io.IOException;

public class TaskModal
{

  public static void openCreate(Resident resident, Stage owner,
      Runnable saveCallback)
  {
    openModal(resident, null, owner, saveCallback);
  }

  public static void openEdit(Resident resident, Task task, Stage owner,
      Runnable saveCallback)
  {
    openModal(resident, task, owner, saveCallback);
  }

  private static void openModal(Resident resident, Task taskToEdit, Stage owner,
      Runnable saveCallback)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader(
          TaskModal.class.getResource("/fxml/NewTaskView.fxml"));
      Parent root = loader.load();
      NewTaskController controller = loader.getController();
      controller.setResidentAndCallback(resident, saveCallback);
      if (taskToEdit != null)
        controller.setEditingTask(taskToEdit);

      Stage dialog = new Stage();
      dialog.initOwner(owner);
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.setResizable(false);
      dialog.setTitle(taskToEdit == null ? "Opret opgave" : "Rediger opgave");
      dialog.setScene(new Scene(root));
      dialog.showAndWait();

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

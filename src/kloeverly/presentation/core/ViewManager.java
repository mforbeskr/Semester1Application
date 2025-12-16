package kloeverly.presentation.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kloeverly.persistence.DataContainer;
import kloeverly.persistence.DataManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager
{

  private final Stage stage;
  private final Map<String, String> views = new HashMap<>();

  private final DataContainer dataContainer;
  private final DataManager dataManager;

  public ViewManager(Stage stage, DataContainer container, DataManager manager)
  {
    this.stage = stage;
    this.dataContainer = container;
    this.dataManager = manager;

    views.put("MainMenu", "/fxml/MainMenuView.fxml");
    views.put("Resident", "/fxml/ResidentView.fxml");
    views.put("Task", "/fxml/TaskMainView.fxml");
    views.put("NewResident", "/fxml/NewResidentView.fxml");
    views.put("Data", "/fxml/AllDataView.fxml");
  }

  public void show(String viewName)
  {
    String fxml = views.get(viewName);
    if (fxml == null)
    {
      throw new IllegalArgumentException(
          "No view registered with name: " + viewName);
    }

    try
    {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
      Parent root = loader.load();

      Object controller = loader.getController();

      if (controller != null)
      {

        // Inject ViewManager if present
        try
        {
          controller.getClass().getMethod("setViewManager", ViewManager.class)
              .invoke(controller, this);
        }
        catch (NoSuchMethodException ignored)
        {
        }

        // Inject DataContainer + DataManager if present
        try
        {
          controller.getClass()
              .getMethod("setData", DataContainer.class, DataManager.class)
              .invoke(controller, dataContainer, dataManager);
        }
        catch (NoSuchMethodException ignored)
        {
        }

        // If controller has initAfterInjection(), run it
        try
        {
          controller.getClass().getMethod("initAfterInjection")
              .invoke(controller);
        }
        catch (NoSuchMethodException ignored)
        {
        }
      }

      stage.setScene(new Scene(root));
      stage.show();

    }
    catch (IOException | ReflectiveOperationException e)
    {
      throw new RuntimeException("Failed to load view: " + fxml, e);
    }
  }
}

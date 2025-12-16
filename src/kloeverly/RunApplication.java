package kloeverly;

import javafx.application.Application;
import javafx.stage.Stage;
import kloeverly.persistence.DataContainer;
import kloeverly.persistence.DataManager;
import kloeverly.persistence.FileDataManager;
import kloeverly.presentation.core.ViewManager;

import java.io.IOException;

public class RunApplication extends Application {

  @Override
  public void start(Stage primaryStage) {

    DataManager dataManager = new FileDataManager("residents.txt");
    DataContainer dataContainer = new DataContainer();

    try { // LOAD RESIDENTS ON STArTUP
      dataContainer.setAll(dataManager.loadResidents());
      dataContainer.ensureFaellesExists();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    ViewManager vm = new ViewManager(primaryStage, dataContainer, dataManager);
    vm.show("MainMenu");
  }

  public static void main(String[] args) {
    launch(args);
  }
}

# Semester 1 Java Application

## Overview

This project is a **first-year Software Engineering Java application** developed as part of the Semester 1 coursework. The goal of the application is to manage residents and their associated tasks using a graphical user interface built with **JavaFX** and persistent storage via a local text file.

The project focuses on applying fundamental software engineering principles such as **object-oriented design**, **separation of concerns**, and **basic persistence**, following the **Waterfall development model**.

---

## Features

* JavaFX-based graphical user interface (GUI)
* Create, view, and manage residents
* Assign and manage tasks related to residents
* Persistent data storage using a `.txt` file
* Automatic creation of the data file if it does not already exist

---

## Project Structure

The project is organised to separate responsibilities clearly:

```
resources/
 └── fxml/                              # JavaFX FXML layout files
      ├── AllDataView.fxml
      ├── MainMenuView.fxml
      ├── NewResidentView.fxml
      ├── NewTaskView.fxml
      ├── ResidentView.fxml
      └── TaskMainView.fxml
src/
 └── kloeverly/
      ├── RunApplication.java           # Application entry point
      ├── domain/                       # Core logic models
      │    ├── Resident.java
      │    └── Task.java
      ├── persistence/                  # Data handling and persistance
      │    ├── DataContainer.java
      │    ├── DataManager.java
      │    └── FileDataManager.java
      └── presentation/                 # UI logic and JavaFX controllers
           ├── controllers/
           │    ├── AllDataController.java
           │    ├── BaseController.java
           │    ├── MainMenuController.java
           │    ├── NewResidentController.java
           │    ├── NewTaskController.java
           │    ├── ResidentDetailsModal.java
           │    ├── ResidentViewController.java
           │    ├── TaskMainViewController.java
           │    └── TaskModal.java
           └── core/
                └── ViewManager.java    # View and navigation management
                       
```

This structure helps maintain a clear separation between **UI**, **domain logic**, and **data persistence**.

---
## Class Responsibilities Summary

* **Resident.java:** Represents a resident with properties like name and ID.
* **Task.java:** Represents a task assigned to a resident.
* **DataContainer.java:** Stores all residents and tasks in memory.
* **DataManager.java:** Defines operations for adding, retrieving, and saving data.
* **FileDataManager.java:** Implements persistence using a text file.
* **ViewManager.java:** Handles navigation between different JavaFX views.
* **Controllers:** Each controller manages user interaction for a specific view or modal.

---
## Technologies Used

* **Java**
* **JavaFX** (FXML, Controllers)
* **SceneBuilder**
* **File I/O** for persistence
* **Git & GitHub** for version control

---
## How to Run the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/mforbeskr/Semester1Application.git
   ```
2. Open the project in an IDE that supports JavaFX (e.g., IntelliJ IDEA).
3. Ensure JavaFX is properly configured.
4. Mark resources as “Resources Root” in IntelliJ. → 
Right click on resources → Mark Directory as → Resources Root
5. Run the `RunApplication` class.

---

## Data Storage

* The application uses a local text file for persistent storage.
* If the file does not exist when the application starts, it is **automatically created** in the project root directory.
* The generated data file is **not tracked by Git** and is created at runtime.

---

## Development Methodology

The project follows the **Waterfall Model**, progressing through:

1. Requirements Analysis
2. System Design
3. Implementation
4. Testing
5. Documentation

Some design documentation (e.g., class diagrams) is being refined after implementation to better reflect the final system.

---

## Known Limitations & Future Improvements

* Limited input validation in some UI forms
* Controllers contain some business logic that could be further abstracted
* No automated unit testing

**Planned improvements:** 
* Enhanced validation
* Clearer separation of logic layers
* Additional documentation and diagrams.

---

## Author

Developed by Group 7, a team of first-year Software Engineering trainee-students at VIA University College as part of our Semester 1 coursework.

---

## License

This project is for educational purposes only.

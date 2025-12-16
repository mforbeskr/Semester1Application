package kloeverly.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Resident
{

  private String name;
  private int houseNumber;
  private final LocalDate dateOfBirth;
  private final ObservableList<Task> tasks = FXCollections.observableArrayList();

  public Resident(String name, int houseNumber, LocalDate dateOfBirth)
  {
    this.name = name;
    this.houseNumber = houseNumber;
    this.dateOfBirth = dateOfBirth;
  }

  public static Resident createNew(String name, int houseNumber, LocalDate dob)
  {
    return new Resident(name, houseNumber, dob);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getHouseNumber()
  {
    return houseNumber;
  }

  public void setHouseNumber(int houseNumber)
  {
    this.houseNumber = houseNumber;
  }

  public LocalDate getDateOfBirth()
  {
    return dateOfBirth;
  }

  public ObservableList<Task> getTasks()
  {
    return tasks;
  }

  public int getPoints()
  {
    return tasks.stream().mapToInt(Task::getPointsForTask).sum();
  }

  public void addTask(Task task)
  {
    if (task == null)
      return;
    tasks.add(task);
  }

  public void removeTask(Task task)
  {
    if (task == null)
      return;
    tasks.remove(task);
  }

  public void addTaskFromFile(Task task)
  {
    if (task == null)
      return;
    tasks.add(task);
  }

  public int getAge()
  {
    return (dateOfBirth == null) ?
        0 :
        Period.between(dateOfBirth, LocalDate.now()).getYears();
  }

  @Override public String toString()
  {
    return "Navn: " + name + "\nHusnummer: " + houseNumber + "\nAlder: "
        + getAge() + "\nPoint: " + getPoints();
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof Resident resident))
      return false;
    return houseNumber == resident.houseNumber && Objects.equals(name,
        resident.name) && Objects.equals(dateOfBirth, resident.dateOfBirth);
  }

  @Override public int hashCode()
  {
    return Objects.hash(name, houseNumber, dateOfBirth);
  }
}

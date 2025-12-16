package kloeverly.domain;

import java.time.LocalDate;

public class Task
{
  private String description;
  private LocalDate date;
  private int pointsForTask;

  public Task(String description, LocalDate date, int pointsForTask)
  {
    this.description = description;
    this.date = date;
    this.pointsForTask = pointsForTask;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public int getPointsForTask()
  {
    return pointsForTask;
  }

  public void setPointsForTask(int pointsForTask)
  {
    this.pointsForTask = pointsForTask;
  }

  public LocalDate getDate()
  {
    return date;
  }

  public void setDate(LocalDate date)
  {
    this.date = date;
  }

  public String toCsvString()
  {
    return date + ";" + pointsForTask + ";" + description.replace(";", ",");
  }

  public static Task fromCsvString(String csvLine)
  {
    String[] parts = csvLine.split(";", 3);
    if (parts.length < 3)
      return null;

    LocalDate date = LocalDate.parse(parts[0]);
    int points = Integer.parseInt(parts[1]);
    String description = parts[2];

    return new Task(description, date, points);
  }

  @Override public String toString()
  {
    return "Dato: " + date + "\nBeskrivelse: " + description + "\nPoint: "
        + pointsForTask;
  }
}

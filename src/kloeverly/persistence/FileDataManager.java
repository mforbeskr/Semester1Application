package kloeverly.persistence;

import kloeverly.domain.Resident;
import kloeverly.domain.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileDataManager implements DataManager
{

  private final File file;

  public FileDataManager(File file)
  {
    this.file = file;
  }

  public FileDataManager(String path)
  {
    this(new File(path));
  }

  @Override
  public List<Resident> loadResidents() throws IOException {
    List<Resident> list = new ArrayList<>();
    if (!file.exists()) return list;

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

      String line;
      Resident currentResident = null;

      while ((line = reader.readLine()) != null) {
        if (line.isBlank()) continue;

        if (!line.startsWith("\t")) { // Resident line
          String[] parts = line.split(",", -1);
          if (parts.length < 4) continue;

          String name = parts[0];
          int houseNumber = parseIntSafe(parts[1], 0);
          LocalDate dob = parseDateSafe(parts[3]);

          currentResident = new Resident(name, houseNumber, dob);
          list.add(currentResident);
        } else if (currentResident != null) { // Task line starts with tab
          String taskLine = line.substring(1); // remove leading tab
          Task task = Task.fromCsvString(taskLine);
          if (task != null)
            currentResident.addTaskFromFile(task);
        }
      }
    }

    return list;
  }

  @Override
  public void saveResidents(List<Resident> residents) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {

      for (Resident r : residents) {
        String dob = r.getDateOfBirth() == null ? "" : r.getDateOfBirth().toString();
        writer.write(String.join(",", safe(r.getName()), String.valueOf(r.getHouseNumber()),
            String.valueOf(r.getPoints()), dob));
        writer.newLine();


        for (Task t : r.getTasks()) {
          writer.write("\t" + t.toCsvString());
          writer.newLine();
        }
      }
    }
  }


  private static int parseIntSafe(String s, int fallback)
  {
    try
    {
      return Integer.parseInt(s.trim());
    }
    catch (Exception ex)
    {
      return fallback;
    }
  }

  private static LocalDate parseDateSafe(String s)
  {
    try
    {
      if (s == null || s.trim().isEmpty())
        return null;
      return LocalDate.parse(s.trim());
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  private static String safe(String s)
  {
    return (s == null) ? "" : s.replace("\n", " ").replace("\r", " ");
  }
}

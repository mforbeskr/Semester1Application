package kloeverly.persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kloeverly.domain.Resident;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DataContainer
{

  private final ObservableList<Resident> residents = FXCollections.observableArrayList();

  public ObservableList<Resident> getResidents()
  {
    return residents;
  }

  public void setAll(List<Resident> list)
  {
    residents.clear();
    residents.addAll(list);
    sort();
  }

  public void addResident(Resident r)
  {
    residents.add(r);
    sort();
  }

  public void removeResident(Resident r)
  {
    residents.remove(r);
  }

  public Optional<Resident> findByHouseNumber(int houseNumber)
  {
    return residents.stream().filter(r -> r.getHouseNumber() == houseNumber)
        .findFirst();
  }

  public void sort()
  {
    residents.sort(
        Comparator.comparing(Resident::getName, String.CASE_INSENSITIVE_ORDER));
  }

  public void ensureFaellesExists()
  {
    boolean exists = residents.stream().anyMatch(r -> r.getHouseNumber() == 0);
    if (!exists)
    {
      residents.add(
          new Resident("Fælles", 0, java.time.LocalDate.of(1991, 1, 1)));
      sort();
    }
  }
}

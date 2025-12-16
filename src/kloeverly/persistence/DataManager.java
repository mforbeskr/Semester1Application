package kloeverly.persistence;

import kloeverly.domain.Resident;

import java.io.IOException;
import java.util.List;

public interface DataManager
{
  /**
   * Load all residents from storage
   */
  List<Resident> loadResidents() throws IOException;

  /**
   * Save the provided list (rewrite entire storage).
   */
  void saveResidents(List<Resident> residents) throws IOException;
}

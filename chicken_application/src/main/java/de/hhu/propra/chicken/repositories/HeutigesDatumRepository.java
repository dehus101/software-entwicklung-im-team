package de.hhu.propra.chicken.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface HeutigesDatumRepository {

  LocalDate getDatum();

  LocalDateTime getDatumUndZeit();
}

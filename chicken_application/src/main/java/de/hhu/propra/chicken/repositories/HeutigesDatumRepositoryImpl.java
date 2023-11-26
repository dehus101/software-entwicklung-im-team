package de.hhu.propra.chicken.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class HeutigesDatumRepositoryImpl implements HeutigesDatumRepository {
  @Override
  public LocalDate getDatum() {
    return LocalDate.now();
  }

  @Override
  public LocalDateTime getDatumUndZeit() {
    return LocalDateTime.now();
  }
}

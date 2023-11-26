package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.services.logging.LogOperation;
import de.hhu.propra.chicken.services.logging.LogTyp;
import java.time.LocalDateTime;

public interface LoggingRepository {

  void logEntry(LocalDateTime dateTime, LogOperation action, LogTyp typ, String gitHubHandle,
                ZeitraumDto vonZustand, ZeitraumDto nachZustand);
}

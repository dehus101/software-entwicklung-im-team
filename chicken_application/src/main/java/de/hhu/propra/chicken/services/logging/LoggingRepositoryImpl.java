package de.hhu.propra.chicken.services.logging;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

@Component
public class LoggingRepositoryImpl implements LoggingRepository {

  private static final String logFilePath = "urlaub-logs.csv";
  private final String[] headers = {"Zeitpunkt", "Aktion", "Typ", "GitHubHandle",
      "Aenderung von", "Aenderung zu"};

  @Override
  public void logEntry(LocalDateTime dateTime, LogOperation action, LogTyp typ, String gitHubHandle,
                       ZeitraumDto vonZustand, ZeitraumDto nachZustand) {
    try {
      CSVPrinter printer = loadLogFile();
      printer.printRecord(dateTime, action, typ, gitHubHandle, vonZustand, nachZustand);
      printer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private CSVPrinter createLogFile() throws IOException {
    BufferedWriter writer;
    writer = Files.newBufferedWriter(Paths.get(logFilePath), StandardOpenOption.APPEND,
        StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    return new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
        .setHeader(headers)
        .build());
  }

  private CSVPrinter loadLogFile() throws IOException {
    File file = new File(logFilePath);
    BufferedWriter writer;
    CSVPrinter csvPrinter;
    if (!file.exists()) {
      csvPrinter = createLogFile();
    } else {
      writer = Files.newBufferedWriter(Paths.get(logFilePath), StandardOpenOption.APPEND,
          StandardOpenOption.CREATE);
      csvPrinter = new CSVPrinter(writer,
          CSVFormat.DEFAULT);
    }
    return csvPrinter;
  }


}
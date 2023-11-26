package de.hhu.propra.chicken.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hhu.propra.chicken.dao.KlausurDao;
import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.aggregates.klausur.KlausurRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:db/migration/V1__tabelle_erstellen.sql",
    "classpath:db/migration/V2__testDaten.sql"})
@DataJdbcTest
@ActiveProfiles("test")
public class KlausurRepositoryImplTest {


  @Autowired
  KlausurDao klausurDao;

  @Test
  @DisplayName("Klausur wird mit richtiger VeranstaltungsId aus der Datenbank geladen")
  void test_1() {
    KlausurRepositoryImpl klausurRepository = new KlausurRepositoryImpl(klausurDao, "2022-03-07",
        "2022-03-25");
    Klausur klausur = klausurRepository.findeKlausurMitVeranstaltungsId("215783");
    assertThat(klausur).isNotNull();
    assertThat(klausur.getVeranstaltungsId()).isEqualTo("215783");
  }

  @Test
  @DisplayName("Eine nicht vorhandene Klausur wird nicht aus der Datenbank geladen.")
  void test_2() {
    KlausurRepositoryImpl klausurRepository = new KlausurRepositoryImpl(klausurDao, "2022-03-07",
        "2022-03-25");
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
        klausurRepository.findeKlausurMitVeranstaltungsId("456654")
    );
  }

  @Test
  @DisplayName("Eine Klausur wird richtig in der Datenbank gespeichert.")
  void test_3() {
    KlausurRepositoryImpl klausurRepository = new KlausurRepositoryImpl(klausurDao, "2022-03-07",
        "2022-03-25");
    Klausur klausur = new Klausur(null, "224568", "RDB",
        ZeitraumDto.erstelleZeitraum(
            LocalDate.of(2022, 3, 9),
            LocalTime.of(10, 30),
            LocalTime.of(11, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)),
        ZeitraumDto.erstelleZeitraum(
            LocalDate.of(2022, 3, 9),
            LocalTime.of(9, 30),
            LocalTime.of(13, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)), true);
    klausurRepository.speicherKlausur(klausur);
    Klausur geladeneKlausur = klausurRepository.findeKlausurMitVeranstaltungsId("224568");
    assertThat(geladeneKlausur).isNotNull();
    assertThat(geladeneKlausur).isEqualTo(klausur);
  }

  @Test
  @DisplayName("Es kann nicht 2 mal die gleiche Klausur hinzugefügt werden")
  void test_4() {
    KlausurRepositoryImpl klausurRepository = new KlausurRepositoryImpl(klausurDao, "2022-03-07",
        "2022-03-25");
    Klausur klausur = new Klausur(null, "215783", "RDB",
        ZeitraumDto.erstelleZeitraum(
            LocalDate.of(2022, 3, 9),
            LocalTime.of(10, 30),
            LocalTime.of(11, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)),
        ZeitraumDto.erstelleZeitraum(
            LocalDate.of(2022, 3, 9),
            LocalTime.of(9, 30),
            LocalTime.of(13, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)), true);
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> klausurRepository.speicherKlausur(klausur));
  }

}

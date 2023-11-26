package de.hhu.propra.chicken.aggregates.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.fehler.ZeitraumDtoException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ZeitraumDtoTest {

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum innerhalb des "
      + "Praktikumszeitraums liegt.")
  void test_1() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 9),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30),
        LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25));
    assertThat(zeitraumDto).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum dem Startdatum des "
      + "Praktikums entspricht.")
  void test_1a() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30),
        LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25));
    assertThat(zeitraumDto).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum dem Enddatum des "
      + "Praktikums entspricht.")
  void test_1b() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 25),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30),
        LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25));
    assertThat(zeitraumDto).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegeben Datum vor dem "
      + "Praktikumszeitraum liegt.")
  void test_2a() throws Exception {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() ->
            ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 1),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                LocalDate.of(2022, 3, 7),
                LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Praktikumszeitraum");
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegeben Datum nach dem "
      + "Praktikumszeitraum liegt.")
  void test_2b() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() ->
            ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 30),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                LocalDate.of(2022, 3, 7),
                LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Praktikumszeitraum");
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn die angegebenen Zeiten nicht in 15er "
      + "Blöcken angegeben sind.")
  void test_3() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() ->
            ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 9),
                LocalTime.of(9, 32),
                LocalTime.of(10, 30),
                LocalDate.of(2022, 3, 7),
                LocalDate.of(2022, 3, 25)))
        .withMessageContaining("15-Minuten Blockform");
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn die Startzeit nach der Endzeit liegt.")
  void test_4() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() -> ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 9),
            LocalTime.of(14, 30),
            LocalTime.of(10, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Startuhrzeit liegt nicht vor der Enduhrzeit");
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegebene Datum am Wochenende "
      + "liegt.")
  void test_5() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() ->
            ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 12),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                LocalDate.of(2022, 3, 7),
                LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Wochenende");
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegebene Datum am Wochenende "
      + "liegt.")
  void test_6() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() ->
            ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 13),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                LocalDate.of(2022, 3, 7),
                LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Wochenende");
  }

  @Test
  @DisplayName("Die Dauer eines Zeitraums wird korrekt berechnet.")
  void test_7() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30),
        LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25));
    long dauer = zeitraumDto.dauerInMinuten();
    assertThat(dauer).isEqualTo(60);
  }

  @Test
  @DisplayName("Wenn die Uhrzeit vor Praktikumsbeginn liegt, erstelle kein Zeitraum.")
  void test_8() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() -> ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7),
            LocalTime.of(9, 15),
            LocalTime.of(10, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Arbeitszeitraum");
  }

  @Test
  @DisplayName("Wenn die Uhrzeit nach Praktikumsende liegt, erstelle kein Zeitraum.")
  void test_9() {
    assertThatExceptionOfType(ZeitraumDtoException.class)
        .isThrownBy(() -> ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7),
            LocalTime.of(9, 30),
            LocalTime.of(14, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)))
        .withMessageContaining("Arbeitszeitraum");
  }

}

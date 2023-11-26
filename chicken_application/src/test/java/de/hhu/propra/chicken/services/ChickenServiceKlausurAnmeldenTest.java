package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_0930_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_0930_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1030_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1030_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1100_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1230_1330;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.services.fehler.VeranstaltungsIdException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

public class ChickenServiceKlausurAnmeldenTest {
  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;

  @Mock
  HeutigesDatumRepository heutigesDatumRepository;

  @Mock
  VeranstaltungsIdRepository veranstaltungsIdRepository;

  @Mock
  LoggingRepository logging;

  @BeforeEach
  void setup() {
    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));
    when(heutigesDatumRepository.getDatumUndZeit()).thenReturn(
        LocalDateTime.of(LocalDate.of(2022, 3, 15),
            LocalTime.of(10, 15)));
    logging = mock(LoggingRepository.class);
  }

  @Test
  @DisplayName("Eine Klausur, die in Präsenz geschrieben wird, erhält 2 Stunden vorher und "
      + "nachher frei.")
  void test_1() throws StudentNichtGefundenException {
    /*
     * 11:30->12:30 zu 09:30->13:30
     */
    veranstaltungsIdRepository = mock(VeranstaltungsIdRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(veranstaltungsIdRepository.webCheck("224568")).thenReturn(true);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    ArgumentCaptor<Klausur> klausurArgumentCaptor = ArgumentCaptor.forClass(Klausur.class);

    appService.klausurAnmelden("224568", "ProPra II",
        ZEITRAUM_03_14_1130_1230, true);

    verify(klausurRepository).speicherKlausur(klausurArgumentCaptor.capture());
    assertThat(klausurArgumentCaptor.getValue().klausurZeitraum()).isEqualTo(
        ZEITRAUM_03_14_1130_1230);
    assertThat(klausurArgumentCaptor.getValue().freistellungsZeitraum()).isEqualTo(
        ZEITRAUM_03_14_0930_1330);
  }

  @Test
  @DisplayName("Eine Klausur, die in Präsenz geschrieben wird, erhält 2 Stunden vorher und "
      + "nachher frei.")
  void test_11() throws StudentNichtGefundenException {
    /*
     * 10:30->11:30 zu 09:30->13:30
     */
    veranstaltungsIdRepository = mock(VeranstaltungsIdRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(veranstaltungsIdRepository.webCheck("224568")).thenReturn(true);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    ArgumentCaptor<Klausur> klausurArgumentCaptor = ArgumentCaptor.forClass(Klausur.class);

    appService.klausurAnmelden("224568", "ProPra II",
        ZEITRAUM_03_14_1030_1130, true);

    verify(klausurRepository).speicherKlausur(klausurArgumentCaptor.capture());
    assertThat(klausurArgumentCaptor.getValue().klausurZeitraum()).isEqualTo(
        ZEITRAUM_03_14_1030_1130);
    assertThat(klausurArgumentCaptor.getValue().freistellungsZeitraum()).isEqualTo(
        ZEITRAUM_03_14_0930_1330);
  }

  @Test
  @DisplayName("Eine Klausur, die in Präsenz geschrieben wird, erhält 2 Stunden vorher und "
      + "nachher frei.")
  void test_12() throws StudentNichtGefundenException {
    /*
     * 12:30->13:30 zu 10:30->13:30
     */
    veranstaltungsIdRepository = mock(VeranstaltungsIdRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(veranstaltungsIdRepository.webCheck("224568")).thenReturn(true);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    ArgumentCaptor<Klausur> klausurArgumentCaptor = ArgumentCaptor.forClass(Klausur.class);
    appService.klausurAnmelden("224568", "ProPra II",
        ZEITRAUM_03_14_1230_1330, true);

    verify(klausurRepository).speicherKlausur(klausurArgumentCaptor.capture());

    assertThat(klausurArgumentCaptor.getValue().klausurZeitraum()).isEqualTo(
        ZEITRAUM_03_14_1230_1330);
    assertThat(klausurArgumentCaptor.getValue().freistellungsZeitraum()).isEqualTo(
        ZEITRAUM_03_14_1030_1330);
  }

  @Test
  @DisplayName("Eine Klausur, die Online geschrieben wird, erhält 30 Minuten vorher frei.")
  void test_2() throws StudentNichtGefundenException {
    /*
     * 11:30->12:30 zu 11:00->12:30
     */
    veranstaltungsIdRepository = mock(VeranstaltungsIdRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(veranstaltungsIdRepository.webCheck("224568")).thenReturn(true);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    ArgumentCaptor<Klausur> klausurArgumentCaptor = ArgumentCaptor.forClass(Klausur.class);

    appService.klausurAnmelden("224568", "ProPra II",
        ZEITRAUM_03_14_1130_1230, false);

    verify(klausurRepository).speicherKlausur(klausurArgumentCaptor.capture());

    assertThat(klausurArgumentCaptor.getValue().klausurZeitraum()).isEqualTo(
        ZEITRAUM_03_14_1130_1230);
    assertThat(klausurArgumentCaptor.getValue().freistellungsZeitraum()).isEqualTo(
        ZEITRAUM_03_14_1100_1230);
  }

  @Test
  @DisplayName("Eine Klausur, die Online geschrieben wird, erhält 30 Minuten vorher frei.")
  void test_22() throws StudentNichtGefundenException {
    /*
     * 11:30->12:30 zu 11:00->12:30
     */
    veranstaltungsIdRepository = mock(VeranstaltungsIdRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(veranstaltungsIdRepository.webCheck("224568")).thenReturn(true);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    ArgumentCaptor<Klausur> klausurArgumentCaptor = ArgumentCaptor.forClass(Klausur.class);

    appService.klausurAnmelden("224568", "ProPra II",
        ZEITRAUM_03_14_0930_1130, false);

    verify(klausurRepository).speicherKlausur(klausurArgumentCaptor.capture());

    assertThat(klausurArgumentCaptor.getValue().klausurZeitraum()).isEqualTo(
        ZEITRAUM_03_14_0930_1130);
    assertThat(klausurArgumentCaptor.getValue().freistellungsZeitraum()).isEqualTo(
        ZEITRAUM_03_14_0930_1130);
  }

  @Test
  @DisplayName("Eine Klausur, mit falscher VeranstaltungsId, kann nicht angemeldet werden.")
  void test_3() throws StudentNichtGefundenException {
    veranstaltungsIdRepository = mock(VeranstaltungsIdRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(veranstaltungsIdRepository.webCheck("224568")).thenReturn(false);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(VeranstaltungsIdException.class)
        .isThrownBy(() ->
            appService.klausurAnmelden("224568", "ProPra II",
                ZEITRAUM_03_14_1130_1230, false))
        .withMessageContaining("Veranstaltungs ID nicht gültig");
  }
}

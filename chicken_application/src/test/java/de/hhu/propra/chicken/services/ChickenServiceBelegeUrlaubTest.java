package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_03_09_1000_1100_O;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_03_09_1000_1145;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230_O;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230_O2;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_1230_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_0945;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1215;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1030_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1030_1145;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1100_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1145_1215;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1145_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1230_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1300_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1130_1230;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceBelegeUrlaubTest {

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

  Student dennis = new Student(1L, "dehus101");

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
  @DisplayName("Urlaub endet vor Klausuranfang. Keine bereits belegten Urlaube. Buchung möglich.")
  void test_belegeUrlaub_fall_1_1() throws StudentNichtGefundenException {
    /*
     * Fall 1 (Klausur)
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 09:30->09:45
     * Erwartung gebuchter Urlaub
     * 1. Keine Buchung des Urlaubs
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_0945);

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_0930_0945);
  }

  @Test
  @DisplayName("Ende des Urlaubs befindet sich exakt am Start der Klausur.")
  void test_belegeUrlaub_fall_1_2() throws StudentNichtGefundenException {
    /*
     * Fall 1 (Ende des Urlaubs gleich Start Klausur)
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 09:30->11:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Buchung des Urlaubs
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1130);

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_0930_1130);
  }

  @Test
  @DisplayName("Urlaub endet innerhalb des Klausurzeitraums. Urlaub wird geschnitten. Buchung "
      + "möglich.")
  void test_test_belegeUrlaub_fall_2_1() throws StudentNichtGefundenException {
    /*
     * Fall 2
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 10:30->11:45
     * Erwartung gebuchter Urlaub 10:30->11:30 (Kürzung des Urlaubs Ende)
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1030_1145);

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1030_1130);
  }

  @Test
  @DisplayName("Urlaub startet innerhalb des Klausurzeitraums. Urlaub wird geschnitten. Urlaub "
      + "hört innerhalb des bereits vorhandenem Urlaub auf. Urlaub wird geschnitten. Buchung "
      + "möglich.")
  void test_4() throws StudentNichtGefundenException {
    /*
     * Fall 3 (Klausur) und Fall 2 (Vorhandener Urlaub)
     * Klausur 10:00->11:00
     * Bereits vorhandener Urlaub 11:30->12:30
     * Zu buchender Urlaub 10:30->11:45
     * Erwartung gebuchter Urlaub:
     * 1. Kürzung von 10:30->11:45 zu 11:00->11:45
     * 2. Kürzung von 11:00->11:45 zu 11:00->11:30 (Kürzung Urlaub)
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_03_09_1000_1100_O);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(
            KL_03_09_1000_1100_O.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1100_O);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1030_1145);

    assertThat(dennis.getUrlaube()).contains(ZEITRAUM_03_09_1100_1130);
  }

  @Test
  @DisplayName("Urlaub befindet sich im Klausurzeitraum. Keine Buchung möglich.")
  void test_900() throws StudentNichtGefundenException {
    /*
     * Fall 4 (Klausur)
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 11:45->12:15
     * Erwartung gebuchter Urlaub
     * 1. Keine Buchung des Urlaubs
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(
        () -> appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1145_1215)
    ).withMessageContaining("Urlaubzeitraum wird komplett von Klausur ueberdeckt");
    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Urlaub startet nach Klausurende. Keine Überschneidung. Buchung ist möglich.")
  void test_2() throws StudentNichtGefundenException {
    /*
     * Fall 5
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 13:00->13:30
     * Erwartung gebuchter Urlaub 13:00->13:30 (keine Veränderung)
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1300_1330);

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1300_1330);
    //verify(studentRepository).speicherStudent(dennis);
    //assertThat(dennis.berechneBeantragtenUrlaub()).isEqualTo(30);
  }

  @Test
  @DisplayName("Klausur endet vor Urlaubsanfang. Urlaub schneidet sich mit bereits belegten "
      + "Urlaub. Urlaub wird geschnitten. Buchung möglich.")
  void test_5() throws StudentNichtGefundenException {
    /*
     * Fall 5 (Klausur) und Fall 3 (Urlaub)
     * Klausur 10:00->11:00
     * Bereits vorhandener Urlaub 11:30->12:30
     * Zu buchender Urlaub 11:45->13:30
     * Erwartung gebuchter Urlaub
     * 1. keine Veränderung, 11:30->12:30 zu 11:30->12:30
     * 2. Kürzung von 11:45->13:30 zu 12:30->13:30
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_03_09_1000_1100_O);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(
            KL_03_09_1000_1100_O.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1100_O);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1145_1330);

    assertThat(dennis.getUrlaube()).containsExactlyInAnyOrder(ZEITRAUM_03_09_1130_1230,
        ZEITRAUM_03_09_1230_1330);
  }

  @Test
  @DisplayName("Start des Urlaubs befindet sich exakt am Ende der Klausur.")
  void test_903() throws StudentNichtGefundenException {
    /*
     * Fall 5 (Start des Urlaubs gleich Ende Klausur)
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 12:30->13:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Buchung des Urlaubs
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1230_1330);

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1230_1330);
  }

  @Test
  @DisplayName("Klausur endet vor Urlaubsanfang. Urlaub schneidet sich mit bereits belegten "
      + "Urlaub. Urlaub wird geschnitten. Buchung möglich.")
  void test_6() throws StudentNichtGefundenException {
    /*
     * Fall 5 (Klausur) und Fall 3 (Urlaub)
     * Klausur 10:00->11:00
     * Bereits vorhandener Urlaub 09:30->09:45
     * Bereits vorhandener Urlaub 11:30->12:30
     * Zu buchender Urlaub 11:45->13:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Veränderung 11:45->13:30 zu 11:45->13:30
     * 2. Kürzung 11:45->13:30 zu 12:30->13:30
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_03_09_1000_1100_O);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_0945);

    klausurRepository = mock(KlausurRepository.class);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(
            KL_03_09_1000_1100_O.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1100_O);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1145_1330);

    assertThat(dennis.getUrlaube()).containsExactlyInAnyOrder(
        ZEITRAUM_03_09_0930_0945,
        ZEITRAUM_03_09_1130_1230,
        ZEITRAUM_03_09_1230_1330
    );
  }

  @Test
  @DisplayName("Urlaub erstreckt sich über den gesamten Klausurzeitraum. Urlaub wird in zwei "
      + "Teile aufgeteilt. Buchung möglich.")
  void test_3() throws StudentNichtGefundenException {
    /*
     * Fall 6
     * Klausur 11:30->12:30
     * Zu buchender Urlaub 09:30->13:30
     * Erwartung gebuchter Urlaub 09:30->11:30 und 12:30->13:30 (Aufteilung Urlaub)
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1330);

    assertThat(dennis.getUrlaube()).contains(ZEITRAUM_03_09_0930_1130, ZEITRAUM_03_09_1230_1330);
    assertThat(dennis.berechneBeantragtenUrlaub()).isEqualTo(180);
  }

  /*
   ==============================================================================================
   * Ohne Klausur an dem Tag
   */

  @Test
  @DisplayName("Urlaub endet um 13:30. Bereits vorhandener Urlaub fängt um 9:30 an. Es ist genug "
      + "Zeit zwischen den beiden Urlauben. Urlaub kann gebucht werden.")
  void test_7() throws StudentNichtGefundenException {
    /*
     * Fall 5
     * Bereits vorhandener Urlaub 09:30->09:45
     * Zu buchender Urlaub 11:45->13:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Veränderung 09:30->09:45 und 11:45->13:30
     */
    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_0945);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1145_1330);

    assertThat(dennis.getUrlaube()).containsExactlyInAnyOrder(
        ZEITRAUM_03_09_0930_0945, ZEITRAUM_03_09_1145_1330
    );
  }

  @Test
  @DisplayName("Keine bereits vorhandenen Urlaube. Urlaub kann frei gebucht werden.")
  void test_8() throws StudentNichtGefundenException {
    /*
     * Fall 5
     * Zu buchender Urlaub 11:45->13:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Veränderung 09:30->09:45 und 11:45->13:30
     */
    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1145_1330);

    assertThat(dennis.getUrlaube()).containsExactly(
        ZEITRAUM_03_09_1145_1330
    );
  }

  @Test
  @DisplayName("Keine bereits vorhandenen Urlaube. Urlaub ist 4 Stunden lang. Der Student hat "
      + "genug Resturlaub. Buchung möglich.")
  void test_9() throws StudentNichtGefundenException {
    /*
     * Zu buchender Urlaub 09:30->13:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Veränderung 09:30->13:30 und 09:30->13:30
     */
    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1330);

    assertThat(dennis.getUrlaube()).containsExactly(
        ZEITRAUM_03_09_0930_1330
    );
    assertThat(dennis.berechneRestUrlaub()).isEqualTo(0);
  }

  @Test
  @DisplayName("Keine bereits vorhandenen Urlaube. Urlaub ist 2,5 Stunden lang. Der Student "
      + "hat genug Resturlaub. Buchung möglich.")
  void test_10() throws StudentNichtGefundenException {
    /*
     * Zu buchender Urlaub 09:30->12:00
     * Erwartung gebuchter Urlaub
     * 1. Keine Veränderung 09:30->12:00 und 09:30->12:00
     */
    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1200);

    assertThat(dennis.getUrlaube()).containsExactly(
        ZEITRAUM_03_09_0930_1200
    );
    assertThat(dennis.berechneRestUrlaub()).isEqualTo(90);
  }

  @Test
  @DisplayName("Keine bereits vorhandenen Urlaube. Urlaub ist kürzer als 2,5 Stunden. Der "
      + "Student hat genug Resturlaub. Buchung möglich.")
  void test_100() throws StudentNichtGefundenException {
    /*
     * Zu buchender Urlaub 09:30->11:30
     * Erwartung gebuchter Urlaub
     * 1. Keine Veränderung 09:30->11:30 und 09:30->11:30
     */
    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1130);

    assertThat(dennis.getUrlaube()).containsExactly(
        ZEITRAUM_03_09_0930_1130
    );
  }

  @Test
  @DisplayName("Keine bereits vorhandenen Urlaube. Urlaub ist länger als 2,5 Stunden und kürzer "
      + "als 4 Stunden. Der Student hat genug Resturlaub. Buchung nicht möglich.")
  void test_11() throws StudentNichtGefundenException {
    /*
     * Zu buchender Urlaub 09:30->12:15
     * Erwartung gebuchter Urlaub
     * 1. Keine Buchung
     */
    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1215)
    ).withMessageContaining("Urlaubszeitraum nicht korrekt");
  }

  @Test
  @DisplayName("Es müssen mindestens 90 Minuten zwischen den belegten Urlauben an einem Tag "
      + "liegen.")
  void test_12() throws StudentNichtGefundenException {

    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_1130);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(
        () -> appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1145_1330)
    ).withMessageContaining("90 Minuten");

    assertThat(dennis.getUrlaube()).containsExactly(
        ZEITRAUM_03_09_0930_1130
    );
    assertThat(dennis.berechneRestUrlaub()).isEqualTo(120);
  }

  @Test
  @DisplayName("Der Student kann nicht mehr Urlaub buchen, als er zur Verfügung hat.")
  void test_13() throws StudentNichtGefundenException {

    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_1130);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_07_1230_1330);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(
        () -> appService.belegeUrlaub("dehus101", ZEITRAUM_03_14_1130_1230)
    ).withMessageContaining("Resturlaub");
  }

  @Test
  @DisplayName("Der Student kann nicht mehr als zwei Urlaube an einem Tag buchen.")
  void test_14() throws StudentNichtGefundenException {

    studentRepository = mock(StudentRepository.class);
    klausurRepository = mock(KlausurRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(anyString())).thenReturn(null);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_0945);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1300_1330);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(
        () -> appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_1130_1230)
    ).withMessageContaining("zwei Urlaube");
  }

  @Test
  @DisplayName("Ein Klausur kann nicht am selben Tag hinzugefügt werden.")
  void test_15() {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 9));

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(KL_03_09_1000_1145.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1145);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1130)
    ).withMessageContaining("selben Tag");
  }

  @Test
  @DisplayName("Ein Klausur kann nicht in der Vergangenheit hinzugefügt werden.")
  void test_16() {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 15));

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(KL_03_09_1000_1145.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1145);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        appService.belegeUrlaub("dehus101", ZEITRAUM_03_09_0930_1130)
    ).withMessageContaining("nachhinein");
  }

}

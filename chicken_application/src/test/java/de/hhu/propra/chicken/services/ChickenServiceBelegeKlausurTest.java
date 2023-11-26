package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_03_09_1000_1100;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_03_09_1000_1145;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230_O2;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_0945;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1100_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1100_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1145_1215;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1200_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1230_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1300_1330;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.student.KlausurReferenz;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceBelegeKlausurTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;

  @Mock
  HeutigesDatumRepository heutigesDatumRepository;
  Student dennis = new Student(1L, "dehus101");

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
  @DisplayName("Kein Urlaub am Tag. Klausur hinzufügen möglich.")
  void test_1() {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07",
            "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()));
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub und Klausur ueberschneiden sich nicht. "
      + "Klausur hinzufügen möglich.")
  void test_2() {
    /*
     * Fall 2: Urlaub fängt vor der Klausur an und hört vor der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 09:30->09:45
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub bleibt unverändert
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_0945);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07",
            "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_0930_0945);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub und Klausur ueberschneiden sich nicht. "
      + "Klausur hinzufügen möglich.")
  void test_3() {
    /*
     * Fall 2: Urlaub fängt vor der Klausur an und hört exakt vor der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 09:30->11:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub bleibt unverändert
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_1130);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07",
            "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_0930_1130);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub und Klausur ueberschneiden sich nicht. "
      + "Klausur hinzufügen möglich.")
  void test_4() {
    /*
     * Fall 6: Urlaub fängt nach der Klausur an und hört nach der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 12:30->13:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub bleibt unverändert
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1230_1330);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07",
            "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1230_1330);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub und Klausur ueberschneiden sich nicht. "
      + "Klausur hinzufügen möglich.")
  void test_5() {
    /*
     * Fall 6: Urlaub fängt nach der Klausur an und hört nach der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 13:00->13:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub bleibt unverändert
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1300_1330);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1300_1330);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub und Klausur ueberschneiden sich. Urlaub wird verändert. "
      + "Klausur hinzufügen möglich.")
  void test_6() {
    /*
     * Fall 1: Urlaub fängt vor der Klausur an und hört innerhalb des Klausurzeitraums auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 11:00->12:00
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub wird von 11:00->12:00 zu 11:00->11:30 geändert
     * 3. Urlaub von 11:00->12:00 wird gelöscht
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1100_1200);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1100_1130);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub und Klausur ueberschneiden sich. Urlaub wird verändert. "
      + "Klausur hinzufügen möglich.")
  void test_7() {
    /*
     * Fall 4: Urlaub fängt innerhalb der Klausur an und hört nach der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 12:00->13:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub wird von 12:00->13:30 zu 12:30->13:30 geändert
     * 3. Urlaub von 12:00->13:30 wird gelöscht
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1200_1330);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1230_1330);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub befindet sich innerhalb der Klausur. "
      + "Urlaub wird gelöscht. Klausur hinzufügen möglich.")
  void test_8() {
    /*
     * Fall 5: Urlaub fängt innerhalb der Klausur an und hört innerhalb der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 12:00->13:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub wird von 12:00->13:30 zu 12:30->13:30 geändert
     * 3. Urlaub von 12:00->13:30 wird gelöscht
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1145_1215);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub befindet sich innerhalb der Klausur. "
      + "Urlaub wird gelöscht. Klausur hinzufügen möglich.")
  void test_9() {
    /*
     * Fall 5: Urlaub fängt innerhalb der Klausur an und hört innerhalb der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 12:00->13:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub wird von 12:00->13:30 zu 12:30->13:30 geändert
     * 3. Urlaub von 12:00->13:30 wird gelöscht
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1145_1215);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1230_1330);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_09_1230_1330);
  }

  @Test
  @DisplayName("Urlaub an dem Tag. Urlaub befindet sich innerhalb der Klausur. "
      + "Urlaub wird gelöscht. Klausur hinzufügen möglich.")
  void test_10() {
    /*
     * Fall 3: Urlaub fängt vor der Klausur an und hört nach der Klausur auf
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Urlaub 09:30->13:30
     * Erwartung
     * 1. Klausur wird hinzugefügt
     * 2. Urlaub wird von 12:00->13:30 zu 12:30->13:30 geändert
     * 3. Urlaub von 12:00->13:30 wird gelöscht
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230_O2);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230_O2.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230_O2);

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_1330);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230_O2);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()));
    assertThat(dennis.getUrlaube()).containsExactlyInAnyOrder(ZEITRAUM_03_09_0930_1130,
        ZEITRAUM_03_09_1230_1330);
  }

  @Test
  @DisplayName("Kein Urlaub an dem Tag. Klausur an dem Tag. Klausuren ueberschneiden sich nicht. "
      + " Klausur hinzufügen möglich.")
  void test_11() {
    /*
     * Fall 3: (Optional) zwei Klausuren schneiden sich Klausur belegung ablehnen.
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Klausur 10:00->11:00
     * Erwartung
     * 1. Klausur wird hinzugefügt
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(KL_03_09_1000_1100.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1100);

    dennis.fuegeKlausurHinzu(KL_03_09_1000_1100);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230);

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()),
        new KlausurReferenz(KL_03_09_1000_1100.getVeranstaltungsId())
    );
  }

  @Test
  @DisplayName("Kein Urlaub an dem Tag. Klausur an dem Tag. Klausuren ueberschneiden sich. "
      + " Klausur hinzufügen nicht möglich.")
  void test_12() {
    /*
     * Fall 3: (Optional) zwei Klausuren schneiden sich Klausur belegung ablehnen.
     * Zu buchende Klausur 11:30->12:30
     * Vorhandener Klausur 10:00->11:45
     * Erwartung
     * 1. Klausur wird hinzugefügt
     */
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(
        klausurRepository.findeKlausurMitVeranstaltungsId(KL_03_09_1000_1145.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1145);

    dennis.fuegeKlausurHinzu(KL_03_09_1000_1145);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(KlausurException.class).isThrownBy(() ->
        appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("zwei Klausuren");

    assertThat(dennis.getKlausuren()).contains(
        new KlausurReferenz(KL_03_09_1000_1100.getVeranstaltungsId())
    );
  }

  @Test
  @DisplayName("Ein Klausur kann nicht am selben Tag hinzugefügt werden.")
  void test_13() {
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

    assertThatExceptionOfType(KlausurException.class).isThrownBy(() ->
        appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("selben Tag");
  }

  @Test
  @DisplayName("Ein Klausur kann nicht in der Vergangenheit hinzugefügt werden.")
  void test_14() {
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

    assertThatExceptionOfType(KlausurException.class).isThrownBy(() ->
        appService.belegeKlausur("dehus101", KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("nachhinein");
  }

}

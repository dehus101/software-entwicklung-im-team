package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_RANDOM10_03_24_1000_1300;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_RANDOM10_03_24_1000_1300_O;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_RANDOM11_03_24_1300_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_24_0930_1000;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_24_1000_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_24_1000_1300;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_24_1200_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_24_1300_1330;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.logging.LogOperation;
import de.hhu.propra.chicken.services.logging.LogTyp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceLoggingTest {
  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;
  Student dennis = new Student(1L, "dehus101");
  LocalDateTime heutigesDatum = LocalDateTime.of(LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 15));
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
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

  }

  @Test
  @DisplayName("Beim Urlaub belegen mit Urlaub am Tag aber ohne Klausur am Tag und befolgen aller"
      + "Regeln wird ein korrekter Log erstellt")
  void test_1() {
    /*
     * Fall: Urlaub von 9:30 -> 10:00 schon gebucht
     *       keine Klausur
     *       Soll: Urlaub von 13:00 -> 13:30 buchen
     */
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_24_0930_1000);
    klausurRepository = mock(KlausurRepository.class);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeUrlaub("dehus101", ZEITRAUM_03_24_1300_1330);
    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.URLAUB, "dehus101",
        null, ZEITRAUM_03_24_1300_1330);
    verifyNoMoreInteractions(logging);
  }

  @Test
  @DisplayName("Beim Urlaub belegen ohne Urlaub am Tag aber mit Klausur am Tag und keiner "
      + "Überschneidung wird ein korrekter Log erstellt")
  void test_2() {
    /*
     * Fall: Kein Urlaub gebucht
     *       Klausur von 10_00 - 13_00 schon gebucht
     *       Soll: Urlaub von 13:00 -> 13:30 buchen
     */
    dennis.fuegeKlausurHinzu(KL_RANDOM10_03_24_1000_1300_O);
    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_RANDOM10_03_24_1000_1300_O.getVeranstaltungsId()))
        .thenReturn(KL_RANDOM10_03_24_1000_1300_O);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeUrlaub("dehus101", ZEITRAUM_03_24_1300_1330);
    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.URLAUB, "dehus101",
        null, ZEITRAUM_03_24_1300_1330);
    verifyNoMoreInteractions(logging);
  }

  @Test
  @DisplayName("Beim Urlaub belegen ohne Urlaub am Tag und ohne Klausur am Tag"
      + " wird ein korrekter Log erstellt")
  void test_21() {
    /*
     * Fall: Kein Urlaub gebucht
     *       keine Klausur gebucht
     *       Soll: Urlaub von 13:00 -> 13:30 buchen
     */
    klausurRepository = mock(KlausurRepository.class);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeUrlaub("dehus101", ZEITRAUM_03_24_1300_1330);
    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.URLAUB, "dehus101",
        null, ZEITRAUM_03_24_1300_1330);
    verifyNoMoreInteractions(logging);
  }

  @Test
  @DisplayName("Beim Urlaub stornieren wird der korrekte Log-Eintrag erstellt")
  void test_3() {
    /*
     * Fall: Urlaub von 9:30 - 10:30 schon gebucht, soll storniert werden
     */
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_08_0930_1030);

    verify(logging).logEntry(heutigesDatum, LogOperation.DELETE, LogTyp.URLAUB, "dehus101",
        ZEITRAUM_03_08_0930_1030, null);
    verifyNoMoreInteractions(logging);

  }

  @Test
  @DisplayName("Beim Klausur stornieren ohne Urlaub am Tag wird ein korrekter Log-Eintrag "
      + "erstellt")
  void test_4() {
    /*
     * Fall: Klausur von 10_00 - 13_00 schon gebucht, soll storniert werden
     */
    dennis.fuegeKlausurHinzu(KL_RANDOM10_03_24_1000_1300);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    applicationService.storniereKlausur("dehus101", KL_RANDOM10_03_24_1000_1300);

    verify(logging).logEntry(heutigesDatum, LogOperation.DELETE, LogTyp.KLAUSUR, "dehus101",
        ZEITRAUM_03_24_1000_1300, null);
    verifyNoMoreInteractions(logging);

  }

  @Test
  @DisplayName("Beim Klausur stornieren mit Urlaub am Tag werden die korrekten Log-Einträge "
      + "erstellt")
  void test_5() {
    /*
     * Fall: Klausur am 24.03.2022 um 10:00 - 13:00 soll storniert werden
     * Urlaub am selben Tagvon 13 - 13:30 schon gebucht, soll mit storniert werden
     */
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_24_1300_1330);
    dennis.fuegeKlausurHinzu(KL_RANDOM10_03_24_1000_1300);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    applicationService.storniereKlausur("dehus101", KL_RANDOM10_03_24_1000_1300);

    verify(logging).logEntry(heutigesDatum, LogOperation.DELETE, LogTyp.URLAUB, "dehus101",
        ZEITRAUM_03_24_1300_1330, null);
    verify(logging).logEntry(heutigesDatum, LogOperation.DELETE, LogTyp.KLAUSUR, "dehus101",
        ZEITRAUM_03_24_1000_1300, null);
    verifyNoMoreInteractions(logging);

  }

  @Test
  @DisplayName("Beim Klausur belegen ohne Urlaub und ohne andere Klausur am Tag wird ein "
      + "richtiger Log-Eintrag erstellt")
  void test_6() {
    /*
     * Fall: Klausur am 24.03.2022 um 10:00 - 13:00 soll gebucht werden
     *       kein Urlaub am selben Tag, keine andere Klausur
     */
    klausurRepository = mock(KlausurRepository.class);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeKlausur("dehus101", KL_RANDOM10_03_24_1000_1300_O);

    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.KLAUSUR, "dehus101",
        null, ZEITRAUM_03_24_1000_1300);
    verifyNoMoreInteractions(logging);

  }

  @Test
  @DisplayName("Beim Klausur belegen ohne Klausur am Tag aber mit Urlaub am Tag und "
      + "Überschneidenden Zeiträumen werden die richtigen Log-Einträge erstellt")
  void test_7() {
    /*
     * Fall: Klausur am 24.03.2022 um 10:00 - 13:00 soll gebucht werden
     * Urlaub am selben Tag von 10:00 - 11:00 schon gebucht, soll storniert werden
     * Keine andere Klausur
     */
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_24_1000_1100);

    klausurRepository = mock(KlausurRepository.class);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeKlausur("dehus101", KL_RANDOM10_03_24_1000_1300_O);

    verify(logging).logEntry(heutigesDatum, LogOperation.DELETE, LogTyp.URLAUB, "dehus101",
        ZEITRAUM_03_24_1000_1100, null);
    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.KLAUSUR, "dehus101",
        null, ZEITRAUM_03_24_1000_1300);
    verifyNoMoreInteractions(logging);

  }

  @Test
  @DisplayName("Beim Klausur belegen ohne Klausur am Tag mit Urlaub am Tag und nicht "
      + "überschneidenden Zeiträumen wird der richtige Log-Eintrag erstellt")
  void test_8() {
    /*
     * Fall: Klausur am 24.03.2022 um 10:00 - 13:00 soll gebucht werden
     * Urlaub am selben Tag von 13:00 - 13:30 schon gebucht, soll nichts passieren
     * Keine Klausur schon gebucht
     */
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_24_1300_1330);

    klausurRepository = mock(KlausurRepository.class);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeKlausur("dehus101", KL_RANDOM10_03_24_1000_1300_O);

    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.KLAUSUR, "dehus101",
        null, ZEITRAUM_03_24_1000_1300);
    verifyNoMoreInteractions(logging);
  }

  @Test
  @DisplayName("Beim Klausur belegen ohne Klausur am Tag aber mit Urlaub am Tag und einseitig "
      + "überschneidenden werden die richtigen Log-Einträge erstellt")
  void test_9() {
    /*
     * Fall: Urlaub von 12:00 -> 13:30
     *       Dann: Klausur belegen von 10:00 -> 13:00
     *       Soll: Urlaub von 12:00 -> 13:30 auf 13:00 -> 13:30 aktualisiert
     *       keine Klausur am Tag
     */

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_24_1200_1330);

    klausurRepository = mock(KlausurRepository.class);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeKlausur("dehus101", KL_RANDOM10_03_24_1000_1300_O);
    verify(logging).logEntry(heutigesDatum, LogOperation.UPDATE, LogTyp.URLAUB, "dehus101",
        ZEITRAUM_03_24_1200_1330, ZEITRAUM_03_24_1300_1330);
    verify(logging).logEntry(heutigesDatum, LogOperation.DELETE, LogTyp.URLAUB, "dehus101",
        ZEITRAUM_03_24_1200_1330, null);
    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.KLAUSUR, "dehus101",
        null, ZEITRAUM_03_24_1000_1300);
    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.URLAUB, "dehus101",
        null, ZEITRAUM_03_24_1300_1330);
    verifyNoMoreInteractions(logging);
  }

  @Test
  @DisplayName("Beim Klausur belegen mit Klausur am Tag aber ohne Urlaub am Tag und keine "
      + "Überschneidungen wird der richtige Log-Eintrag erstellt")
  void test_10() {
    /*
     * Fall: Klausur von 13:00 -> 13:30 schon gebucht
     *       Es soll eine weitere Klausur am selben Tag von 10:00 - 13:00 gebucht werden
     *       Soll: Ein Log Eintrag
     */
    dennis.fuegeKlausurHinzu(KL_RANDOM11_03_24_1300_1330);
    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_RANDOM11_03_24_1300_1330.getVeranstaltungsId()))
        .thenReturn(KL_RANDOM11_03_24_1300_1330);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.belegeKlausur("dehus101", KL_RANDOM10_03_24_1000_1300_O);

    verify(logging).logEntry(heutigesDatum, LogOperation.INSERT, LogTyp.KLAUSUR, "dehus101",
        null, ZEITRAUM_03_24_1000_1300);
    verifyNoMoreInteractions(logging);
  }
}

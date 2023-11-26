package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_RECHNERNETZTE_03_07_0930_1030;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_STOCHASTIK_03_08_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_1230_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1000_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1000_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1000_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1000_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1015_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1030_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1045_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1100_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1130_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1145_1200;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;
  Student dennis = new Student(1L, "dehus101");

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
  @DisplayName("GetUrlaubeAmTag gibt die richtige Anzahl der belegten Urlaube an einem Tag zurück")
  void test_1() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<ZeitraumDto> urlaubeAmTag = applicationService
        .getUrlaubeAmTag(ZEITRAUM_03_08_0930_1030, dennis);

    assertThat(urlaubeAmTag).hasSize(2);
  }

  @Test
  @DisplayName("GetUrlaubeAmTag gibt leere Menge zurück bei keinem Urlaub am Tag")
  void test_2() {
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<ZeitraumDto> urlaubeAmTag = applicationService
        .getUrlaubeAmTag(ZEITRAUM_03_07_0930_1030, dennis);

    assertThat(urlaubeAmTag).hasSize(0);
  }

  @Test
  @DisplayName("GetUrlaubeAmTag gibt die richtige Anzahl der belegten Urlaube an einem Tag zurück")
  void test_3() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<ZeitraumDto> urlaubeAmTag = applicationService
        .getUrlaubeAmTag(ZEITRAUM_03_09_1130_1230, dennis);

    assertThat(urlaubeAmTag).hasSize(1);
  }

  @Test
  @DisplayName("getBelegteKlausurenAmTag gibt die richtige Anzahl der belegten Klausuren an einem"
      + " Tag zurück")
  void test_4() {

    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);
    dennis.fuegeKlausurHinzu(KL_STOCHASTIK_03_08_0930_1030);
    dennis.fuegeKlausurHinzu(KL_RECHNERNETZTE_03_07_0930_1030);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_STOCHASTIK_03_08_0930_1030.getVeranstaltungsId()))
        .thenReturn(KL_STOCHASTIK_03_08_0930_1030);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_RECHNERNETZTE_03_07_0930_1030.getVeranstaltungsId()))
        .thenReturn(KL_RECHNERNETZTE_03_07_0930_1030);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<Klausur> klausurenAmTag = applicationService
        .getBelegteKlausurenAmTag(ZEITRAUM_03_08_0930_1030, dennis);

    assertThat(klausurenAmTag).hasSize(1);
  }

  @Test
  @DisplayName("getBelegteKlausurenAmTag gibt keine Klausuren an einem Tag ohne belegte Klausuren"
      + " zurück")
  void test_5() {
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);
    dennis.fuegeKlausurHinzu(KL_STOCHASTIK_03_08_0930_1030);
    dennis.fuegeKlausurHinzu(KL_RECHNERNETZTE_03_07_0930_1030);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_STOCHASTIK_03_08_0930_1030.getVeranstaltungsId()))
        .thenReturn(KL_STOCHASTIK_03_08_0930_1030);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_RECHNERNETZTE_03_07_0930_1030.getVeranstaltungsId()))
        .thenReturn(KL_RECHNERNETZTE_03_07_0930_1030);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<Klausur> klausurenAmTag = applicationService
        .getBelegteKlausurenAmTag(ZEITRAUM_03_14_1130_1230, dennis);

    assertThat(klausurenAmTag).hasSize(0);
  }

  @Test
  @DisplayName("istUrlaubsverteilungKorrekt gibt true bei korrekter Verteilung der "
      + "Urlaubszeiten")
  void test_6() {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    boolean verteilung = applicationService.istUrlaubsverteilungKorrekt(ZEITRAUM_03_07_0930_1030,
        ZEITRAUM_03_07_1230_1330);
    assertThat(verteilung).isTrue();
  }

  @Test
  @DisplayName("istUrlaubsverteilungKorrekt gibt false bei nicht konformer Verteilung der "
      + "Urlaubszeiten")
  void test_7() {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    boolean verteilung = applicationService.istUrlaubsverteilungKorrekt(ZEITRAUM_03_07_0930_1030,
        ZEITRAUM_03_07_1130_1230);
    assertThat(verteilung).isFalse();
  }

  @Test
  @DisplayName("istGenugZeitZwischen gibt true bei genügend Abstand zwischen den beiden "
      + "Urlaubszeiten")
  void test_8() {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    boolean abstand = applicationService.istGenugZeitZwischen(ZEITRAUM_03_07_0930_1030,
        ZEITRAUM_03_07_1230_1330);
    assertThat(abstand).isTrue();
  }

  @Test
  @DisplayName("istGenugZeitZwischen gibt false bei zu wenig Abstand zwischen den beiden "
      + "Urlaubszeiten")
  void test_9() {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    boolean abstand =
        applicationService.istGenugZeitZwischen(ZEITRAUM_03_07_1130_1230, ZEITRAUM_03_07_1230_1330);
    assertThat(abstand).isFalse();
  }

  @Test
  @DisplayName("holeStudent gibt den Student mit dem jeweiligen GithubHandle zurück")
  void test_10() throws StudentNichtGefundenException {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Student geholt = applicationService.holeStudent("dehus101");

    assertThat(geholt).isEqualTo(dennis);
  }

  @Test
  @DisplayName("holeStudent wirft Exception bei keinem gefundenen Studenten")
  void test_11() throws StudentNichtGefundenException {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle(anyString())).thenReturn(null);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(StudentNichtGefundenException.class)
        .isThrownBy(() -> applicationService.holeStudent("dehus101"))
        .withMessageContaining("dehus101");
  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt true bei rechter Überschneidung zurück")
  void test_12() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean ueberschneidung = applicationService.ueberschneidenSichZeitraeume(
        ZEITRAUM_03_15_1100_1200, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isTrue();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt bei keiner Überschneidung false zurück")
  void test_13() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean ueberschneidung = applicationService.ueberschneidenSichZeitraeume(
        ZEITRAUM_03_15_1145_1200, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isFalse();

  }

  @Test
  @DisplayName("Reihenfolge ist nicht entscheidend über das Ergebnis")
  void test_14() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean ueberschneidung = applicationService.ueberschneidenSichZeitraeume(
        ZEITRAUM_03_15_1030_1130, ZEITRAUM_03_15_1145_1200);

    assertThat(ueberschneidung).isFalse();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt true bei linker Überschneidung zurück")
  void test_15() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean ueberschneidung = applicationService.ueberschneidenSichZeitraeume(
        ZEITRAUM_03_15_1015_1100, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isTrue();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt true bei kompletter Überlappung")
  void test_16() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean ueberschneidung = applicationService.ueberschneidenSichZeitraeume(
        ZEITRAUM_03_15_1000_1200, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isTrue();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt false bei identischen Zeiträumen")
  void test_17() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean ueberschneidung = applicationService.ueberschneidenSichZeitraeume(
        ZEITRAUM_03_15_1000_1200, ZEITRAUM_03_15_1000_1200);

    assertThat(ueberschneidung).isFalse();

  }

  @Test
  @DisplayName("berechneZeitraeume gibt nicht ueberlappende Zeiträume zurück. "
      + "Zeitraum1 beinhaltet Zeitraum2")
  void test_18() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<ZeitraumDto> neueZeitraeume = applicationService.berechneNichtUeberlappendeZeitraeume(
        ZEITRAUM_03_15_1000_1200, ZEITRAUM_03_15_1030_1130).collect(Collectors.toSet());

    assertThat(neueZeitraeume).containsExactlyInAnyOrder(ZEITRAUM_03_15_1000_1030,
        ZEITRAUM_03_15_1130_1200);

  }


  @Test
  @DisplayName("berechneZeitraeume gibt ueberlappenden Zeitraum rechts zurück.")
  void test_20() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<ZeitraumDto> neueZeitraeume = applicationService.berechneNichtUeberlappendeZeitraeume(
        ZEITRAUM_03_15_1045_1200, ZEITRAUM_03_15_1030_1130).collect(Collectors.toSet());

    assertThat(neueZeitraeume).containsExactlyInAnyOrder(ZEITRAUM_03_15_1130_1200);

  }

  @Test
  @DisplayName("berechneZeitraeume gibt ueberlappenden Zeitraum links zurück.")
  void test_21() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Set<ZeitraumDto> neueZeitraeume = applicationService.berechneNichtUeberlappendeZeitraeume(
        ZEITRAUM_03_15_1000_1100, ZEITRAUM_03_15_1030_1130).collect(Collectors.toSet());

    assertThat(neueZeitraeume).containsExactlyInAnyOrder(ZEITRAUM_03_15_1000_1030);

  }

  @Test
  @DisplayName("liegtUrlaubInZeitraum gibt true bei Zeitraum2 beinhaltet Zeitraum1 zurück")
  void test_19() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean b = applicationService.liegtUrlaubInZeitraum(
        ZEITRAUM_03_15_1030_1130, ZEITRAUM_03_15_1000_1200);

    assertThat(b).isTrue();

  }

  @Test
  @DisplayName("liegtUrlaubInZeitraum gibt false bei Zeitraum2 beinhaltet nicht Zeitraum1 zurück")
  void test_22() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean b = applicationService.liegtUrlaubInZeitraum(
        ZEITRAUM_03_15_1030_1130, ZEITRAUM_03_15_1130_1200);

    assertThat(b).isFalse();

  }

  @Test
  @DisplayName("berechneRestUrlaub gibt true ")
  void test_23() throws StudentNichtGefundenException {
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    boolean b = applicationService.liegtUrlaubInZeitraum(
        ZEITRAUM_03_15_1030_1130, ZEITRAUM_03_15_1130_1200);

    assertThat(b).isFalse();
  }

  @Test
  @DisplayName("istKlausurDatumKorrekt wirft eine Exception, wenn das angegebene Datum am selben "
      + "Tag liegt.")
  void test_24() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 9));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(KlausurException.class).isThrownBy(
        () -> applicationService.istKlausurDatumKorrekt(KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("selben Tag");
  }

  @Test
  @DisplayName("istKlausurDatumKorrekt wirft eine Exception, wenn das angegebene Datum früher als "
      + "der heutige Tag ist")
  void test_25() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 15));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(KlausurException.class).isThrownBy(
        () -> applicationService.istKlausurDatumKorrekt(KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("nachhinein");
  }

  @Test
  @DisplayName("istKlausurDatumKorrekt wirft keine Exception, wenn das angegebene Datum später als "
      + "der heutige Tag ist")
  void test_26() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.istKlausurDatumKorrekt(KL_PROPRA_03_09_1130_1230);
  }

  @Test
  @DisplayName("istUrlaubDatumKorrekt wirft eine Exception, wenn das angegebene Datum am selben "
      + "Tag ist")
  void test_27() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 9));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(
        () -> applicationService.istUrlaubsDatumKorrekt(ZEITRAUM_03_09_1130_1230)
    ).withMessageContaining("selben");
  }

  @Test
  @DisplayName("istUrlaubDatumKorrekt wirft eine Exception, wenn das angegebene Datum am selben "
      + "Tag ist")
  void test_28() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 9));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(UrlaubException.class).isThrownBy(
        () -> applicationService.istUrlaubsDatumKorrekt(ZEITRAUM_03_09_1130_1230)
    ).withMessageContaining("selben");
  }

  @Test
  @DisplayName("istUrlaubDatumKorrekt wirft keine Exception, wenn das angegebene Datum am selben "
      + "Tag ist")
  void test_29() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.istUrlaubsDatumKorrekt(ZEITRAUM_03_09_1130_1230);

  }

  @Test
  @DisplayName("istUrlaubDatumKorrekt wirft keine Exception, wenn das angegebene Datum am selben "
      + "Tag ist")
  void test_30() throws StudentNichtGefundenException {

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        applicationService.passeUrlaubszeitraumeAnVorhandenenUrlaubenAn(
            Set.of(ZEITRAUM_03_09_0930_1200), Set.of(ZEITRAUM_03_09_1000_1100))
    ).withMessageContaining("ueberdeckt");
  }
}

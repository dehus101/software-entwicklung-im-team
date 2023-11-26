package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
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

public class ChickenServiceHoleKlausurTest {

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
  @DisplayName("Eine existierende Klausur wird korrekt zurückgegeben.")
  void test_1() {
    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId())).thenReturn(KL_PROPRA_03_09_1130_1230);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Klausur geholteKlausur
        = appService.holeKlausur(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId());

    assertThat(geholteKlausur).isEqualTo(KL_PROPRA_03_09_1130_1230);
  }

  @Test
  @DisplayName("Nicht existierende Klausur wirft Fehler.")
  void test_2() {
    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId("20012154")).thenReturn(null);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(KlausurException.class).isThrownBy(() ->
        appService.holeKlausur("20012154")
    ).withMessageContaining("20012154");
  }
}

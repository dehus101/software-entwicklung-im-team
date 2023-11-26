package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_03_09_1000_1100;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_RECHNERNETZTE_03_07_0930_1030;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceAlleKlausurenTest {

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
  @DisplayName("Alle in der Datenbank angemeldeten Klausuren werden korrekt zurückgegeben.")
  void test_1() {
    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findAll()).thenReturn(
        Set.of(KL_PROPRA_03_09_1130_1230, KL_03_09_1000_1100, KL_RECHNERNETZTE_03_07_0930_1030));

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07",
            "2022-03-25");

    Set<Klausur> klausuren = appService.alleKlausuren();

    assertThat(klausuren).containsExactlyInAnyOrder(KL_PROPRA_03_09_1130_1230, KL_03_09_1000_1100,
        KL_RECHNERNETZTE_03_07_0930_1030);
  }

  @Test
  @DisplayName("Wenn keine Klausuren angemeldet sind, wird nichts zurückgegeben.")
  void test_2() {
    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findAll()).thenReturn(Collections.emptySet());

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07",
            "2022-03-25");

    Set<Klausur> klausuren = appService.alleKlausuren();
    assertThat(klausuren).isEmpty();
  }
}

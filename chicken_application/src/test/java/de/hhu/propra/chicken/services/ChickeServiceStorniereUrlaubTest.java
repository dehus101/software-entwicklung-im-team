package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1045_1200;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickeServiceStorniereUrlaubTest {

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
    when(heutigesDatumRepository.getDatumUndZeit()).thenReturn(LocalDateTime.of(
        LocalDate.of(2022, 3, 15),
        LocalTime.of(10, 15))
    );
    logging = mock(LoggingRepository.class);
  }

  @Test
  @DisplayName("Der Urlaub kann im vorhinein bis zum Vortag storniert werden")
  void test_1() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200);

    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Der Urlaub kann am Vortag storniert werden")
  void test_2() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 14));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200);

    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Der Urlaub kann nicht am selben Tag storniert werden")
  void test_3() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 15));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200)
    ).withMessageContaining("selben");

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_15_1045_1200);
  }

  @Test
  @DisplayName("Der Urlaub kann nicht im nachhinein storniert werden")
  void test_4() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatumRepository = mock(HeutigesDatumRepository.class);
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 17));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");
    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200)
    ).withMessageContaining("nachhinein");

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_15_1045_1200);
  }

}

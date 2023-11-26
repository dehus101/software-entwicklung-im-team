package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_03_09_1000_1145;
import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
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
import de.hhu.propra.chicken.services.dto.StudentDetailsDto;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceStudentDetailsTestDto {

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
  @DisplayName("Student hat eine Klausuren belegt. Klausuren-Set enthält diese.")
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
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    StudentDetailsDto studentDetailsDto = appService.studentDetails("dehus101");

    assertThat(studentDetailsDto.klausuren()).containsExactlyInAnyOrder(KL_PROPRA_03_09_1130_1230);
  }

  @Test
  @DisplayName("Student hat mehrere Klausuren belegt. Klausuren-Set enthält diese.")
  void test_2() {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);
    dennis.fuegeKlausurHinzu(KL_03_09_1000_1145);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()))
        .thenReturn(KL_PROPRA_03_09_1130_1230);
    when(klausurRepository.findeKlausurMitVeranstaltungsId(
        KL_03_09_1000_1145.getVeranstaltungsId()))
        .thenReturn(KL_03_09_1000_1145);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    StudentDetailsDto studentDetailsDto = appService.studentDetails("dehus101");

    assertThat(studentDetailsDto.klausuren()).containsExactlyInAnyOrder(KL_PROPRA_03_09_1130_1230,
        KL_03_09_1000_1145);
  }

  @Test
  @DisplayName("Student hat keine Klausuren belegt. Klausuren-Set ist leer.")
  void test_3() {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    klausurRepository = mock(KlausurRepository.class);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    StudentDetailsDto studentDetailsDto = appService.studentDetails("dehus101");

    assertThat(studentDetailsDto.klausuren()).isEmpty();
  }

  @Test
  @DisplayName("Student existiert nicht. Exception wird geworfen.")
  void test_4() {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);
    when(studentRepository.findeStudentMitHandle("fnellen")).thenReturn(null);

    klausurRepository = mock(KlausurRepository.class);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    assertThatExceptionOfType(StudentNichtGefundenException.class).isThrownBy(() ->
        appService.studentDetails("fnellen")).withMessageContaining("fnellen");
  }

}

package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.LoggingRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceStudentSpeichernTest {

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
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    appService.studentSpeichern(dennis);

    verify(studentRepository).speicherStudent(dennis);
  }

  @Test
  @DisplayName("Student hat eine Klausuren belegt. Klausuren-Set enthält diese.")
  void test_2() {
    studentRepository = mock(StudentRepository.class);
    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    klausurRepository = mock(KlausurRepository.class);

    ChickenService appService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatumRepository,
            veranstaltungsIdRepository, logging, "2022-03-07", "2022-03-25");

    Student student = new Student(3L, "test");

    appService.studentSpeichern(student);

    verify(studentRepository).speicherStudent(student);
  }
}

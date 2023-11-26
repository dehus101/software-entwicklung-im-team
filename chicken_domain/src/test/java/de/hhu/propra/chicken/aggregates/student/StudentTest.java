package de.hhu.propra.chicken.aggregates.student;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudentTest {

  @Test
  @DisplayName("Der Belegte Urlaub wird korrekt berechnet.")
  void test_1() {
    Student student = new Student(1L, "");
    ZeitraumDto zeitraum1 =
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 30),
            LocalTime.of(10, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25));
    ZeitraumDto zeitraum2 =
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 10), LocalTime.of(10, 30),
            LocalTime.of(11, 0),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25));
    student.fuegeUrlaubHinzu(zeitraum1);
    student.fuegeUrlaubHinzu(zeitraum2);
    long restUrlaub = student.berechneBeantragtenUrlaub();

    assertThat(restUrlaub).isEqualTo(90);
  }

  @Test
  @DisplayName("Der restliche Urlaub wird korrekt berechnet.")
  void test_2() {
    Student student = new Student(1L, "");
    ZeitraumDto zeitraum1 =
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 30),
            LocalTime.of(10, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25));
    ZeitraumDto zeitraum2 =
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 10), LocalTime.of(10, 30),
            LocalTime.of(11, 0),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25));
    student.fuegeUrlaubHinzu(zeitraum1);
    student.fuegeUrlaubHinzu(zeitraum2);
    long restUrlaub = student.berechneRestUrlaub();

    assertThat(restUrlaub).isEqualTo(150);
  }
}

package de.hhu.propra.chicken.web;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.services.dto.StudentDetailsDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

public class StudentTemplate {

  public static final ZeitraumDto ZEITRAUM_03_09_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  public static final ZeitraumDto ZEITRAUM_03_09_0930_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  public static final ZeitraumDto ZEITRAUM_03_10_1030_1300 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 10),
      LocalTime.of(10, 30),
      LocalTime.of(13, 00),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  public static final Klausur KL_PROPRA_03_09_1130_1230 =
      new Klausur(null, "215783", "Propra2", ZEITRAUM_03_09_1130_1230, ZEITRAUM_03_09_0930_1330,
          true);

  public static final Student DENNIS = new Student(1L, "dehus101");
  public static final Student FEDERICO = new Student(2L, "fnellen");

  public StudentDetailsDto getDennisDetails() {
    return new StudentDetailsDto(DENNIS, Set.of(KL_PROPRA_03_09_1130_1230));
  }

  public StudentDetailsDto getFedericoDetails() {
    return new StudentDetailsDto(FEDERICO, Collections.emptySet());
  }

}

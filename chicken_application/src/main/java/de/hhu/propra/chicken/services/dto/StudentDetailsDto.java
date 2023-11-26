package de.hhu.propra.chicken.services.dto;

import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import java.util.Set;

public record StudentDetailsDto(Student student,
                                Set<Klausur> klausuren) {

  public static StudentDetailsDto von(Student student, Set<Klausur> klausuren) {

    return new StudentDetailsDto(student, klausuren);
  }


}

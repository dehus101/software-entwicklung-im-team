package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.domain.aggregates.student.Student;

public interface StudentRepository {

  Student findeStudentMitHandle(String githubHandle);

  void speicherStudent(Student student);

}

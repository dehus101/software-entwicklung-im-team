package de.hhu.propra.chicken.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hhu.propra.chicken.dao.StudentDao;
import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.aggregates.student.KlausurReferenz;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.domain.aggregates.student.StudentRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:db/migration/V1__tabelle_erstellen.sql",
    "classpath:db/migration/V2__testDaten.sql"})
@DataJdbcTest
@ActiveProfiles("test")
public class StudentRepositoryImplTest {

  @Autowired
  StudentDao studentDao;

  @Test
  @DisplayName("Student wird mit richtigem githubHandle aus der Datenbank geladen")
  void test_1() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    Student student = studentRepository.findeStudentMitHandle("ernaz100");
    assertThat(student).isNotNull();
    assertThat(student.getGithubHandle()).isEqualTo("ernaz100");

  }

  @Test
  @DisplayName("Student wird mit falschem githubHandle nicht aus der Datenbank geladen")
  void test_2() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> studentRepository.findeStudentMitHandle("ernaz99"))
        .withMessageContaining("ernaz99");
  }

  @Test
  @DisplayName("Student wird in die Datenbank gespeichert und korrekt geladen")
  void test_3() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    Student student = new Student(null, "lol");
    studentRepository.speicherStudent(student);
    Student geladen = studentRepository.findeStudentMitHandle("lol");
    assertThat(geladen).isNotNull();
    assertThat(geladen.getGithubHandle()).isEqualTo("lol");
  }

  @Test
  @DisplayName("Student wird aus der Datenbank geladen und Urlaub hinzugefügt")
  void test_4() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    Student student = studentRepository.findeStudentMitHandle("dehus101");
    student.fuegeUrlaubHinzu(ZeitraumDto.erstelleZeitraum(
        LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30), LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25)));
    studentRepository.speicherStudent(student);
    Student geladen = studentRepository.findeStudentMitHandle("dehus101");
    assertThat(geladen.getGithubHandle()).isEqualTo("dehus101");
    assertThat(geladen.getUrlaube()).contains(ZeitraumDto.erstelleZeitraum(
        LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30), LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25)));
  }

  @Test
  @DisplayName("Student wird aus der Datenbank geladen und Urlaub gelöscht")
  void test_5() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    Student dennis = studentRepository.findeStudentMitHandle("dehus101");
    dennis.entferneUrlaub(ZeitraumDto.erstelleZeitraum(
        LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30), LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25)));
    assertThat(dennis.getGithubHandle()).isEqualTo("dehus101");
    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Student wird aus der Datenbank geladen und Urlaub gelöscht und korrekt gespeichert")
  void test_6() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    Student dennis = studentRepository.findeStudentMitHandle("dehus101");
    dennis.entferneUrlaub(ZeitraumDto.erstelleZeitraum(
        LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30), LocalDate.of(2022, 3, 7),
        LocalDate.of(2022, 3, 25)));
    studentRepository.speicherStudent(dennis);
    Student geladen = studentRepository.findeStudentMitHandle("dehus101");
    assertThat(geladen.getGithubHandle()).isEqualTo("dehus101");
    assertThat(geladen.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Eine vom Student belegte Klausur wird in der Datenbank gespeichert")
  void test_7() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao, "2022-03-07",
        "2022-03-25");
    Student dennis = studentRepository.findeStudentMitHandle("dehus101");
    Klausur klausur = new Klausur(null, "215783", "RDB",
        ZeitraumDto.erstelleZeitraum(
            LocalDate.of(2022, 3, 9),
            LocalTime.of(10, 30),
            LocalTime.of(11, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)),
        ZeitraumDto.erstelleZeitraum(
            LocalDate.of(2022, 3, 9),
            LocalTime.of(9, 30),
            LocalTime.of(13, 30),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25)),
        true);
    dennis.fuegeKlausurHinzu(klausur);
    studentRepository.speicherStudent(dennis);
    Student geladen = studentRepository.findeStudentMitHandle("dehus101");
    assertThat(geladen.getKlausuren())
        .containsExactly(new KlausurReferenz(klausur.getVeranstaltungsId()));

  }
}

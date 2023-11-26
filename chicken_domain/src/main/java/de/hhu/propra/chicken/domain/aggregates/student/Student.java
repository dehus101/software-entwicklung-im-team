package de.hhu.propra.chicken.domain.aggregates.student;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.stereotypes.AggregateRoot;
import de.hhu.propra.chicken.domain.stereotypes.EntityObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Darstellung eines Studenten, der Urlaub nehmen kann und sich für Klausuren anmelden kann.
 * Bildet den Stakeholder Student ab.
 */
@AggregateRoot
@EntityObject
public class Student {

  private static final long GESAMT_URLAUBSZEIT_IN_MINUTEN = 240L;
  private final String githubHandle;
  private Long id;
  private Set<ZeitraumDto> urlaube = new HashSet<>();
  private Set<KlausurReferenz> klausuren = new HashSet<>();

  /**
   * Konstruktor zur Erstellung eines Studenten.
   *
   * @param id           Eindeutige Identifikation des Studenten in der Datenbank.
   * @param githubHandle Eindeutige Identifikation des Studenten durch GitHub-Authentifizierung.
   */
  public Student(Long id, String githubHandle) {
    this.id = id;
    this.githubHandle = githubHandle;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGithubHandle() {
    return githubHandle;
  }

  public Set<ZeitraumDto> getUrlaube() {
    return urlaube;
  }

  /**
   * Fügt dem Studenten Urlaub hinzu.
   *
   * @param urlaubsZeitraum Ein Zeitraum, indem der Student Urlaub hat.
   */
  public void fuegeUrlaubHinzu(ZeitraumDto urlaubsZeitraum) {
    long minuten = urlaubsZeitraum.dauerInMinuten() + this.berechneBeantragtenUrlaub();
    if (minuten <= GESAMT_URLAUBSZEIT_IN_MINUTEN) {
      Set<ZeitraumDto> newUrlaube = new HashSet<>(urlaube);
      newUrlaube.add(urlaubsZeitraum);
      urlaube = Set.copyOf(newUrlaube);
    }
  }

  public void fuegeKlausurHinzu(Klausur klausur) {
    klausuren.add(new KlausurReferenz(klausur.getVeranstaltungsId()));
  }

  public long berechneBeantragtenUrlaub() {
    long urlaub = urlaube.stream().mapToLong(ZeitraumDto::dauerInMinuten).sum();
    return urlaub;
  }

  public long berechneRestUrlaub() {
    return GESAMT_URLAUBSZEIT_IN_MINUTEN - this.berechneBeantragtenUrlaub();
  }

  void setzeUrlaube(Set<ZeitraumDto> urlaube) {
    this.urlaube = urlaube;
  }

  public Set<KlausurReferenz> getKlausuren() {
    return klausuren;
  }

  void setzeKlausuren(Set<KlausurReferenz> klausurenReferenzen) {
    this.klausuren = klausurenReferenzen;
  }

  public void entferneUrlaub(ZeitraumDto zeitraumDto) {
    Set<ZeitraumDto> neueUrlaube = new HashSet<>(urlaube);
    neueUrlaube.remove(zeitraumDto);
    urlaube = Set.copyOf(neueUrlaube);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Student student = (Student) o;

    return githubHandle.equals(student.githubHandle);
  }

  @Override
  public int hashCode() {
    return githubHandle.hashCode();
  }

  public void entferneKlausur(Klausur klausur) {
    klausuren.remove(new KlausurReferenz(klausur.getVeranstaltungsId()));
  }
}

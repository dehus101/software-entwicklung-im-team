package de.hhu.propra.chicken.domain.aggregates.klausur;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.stereotypes.AggregateRoot;
import de.hhu.propra.chicken.domain.stereotypes.EntityObject;

/**
 * Darstellung einer Klausur.
 */
@AggregateRoot
@EntityObject
public record Klausur(Long id, VeranstaltungsId veranstaltungsId,
                      String veranstaltungsName,
                      ZeitraumDto klausurZeitraum,
                      ZeitraumDto freistellungsZeitraum,
                      Boolean praesenz) {


  public Klausur(Long id, String veranstaltungsId, String veranstaltungsName,
                 ZeitraumDto klausurZeitraum, ZeitraumDto freistellungsZeitraum, Boolean praesenz) {
    this(id, VeranstaltungsId.erstelle(veranstaltungsId), veranstaltungsName, klausurZeitraum,
        freistellungsZeitraum, praesenz);

  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Klausur klausur = (Klausur) o;

    return veranstaltungsId.equals(klausur.veranstaltungsId);
  }

  public String getVeranstaltungsId() {
    return veranstaltungsId.getVeranstaltungsId();
  }

  @Override
  public int hashCode() {
    return veranstaltungsId.hashCode();
  }

  @Override
  public String toString() {
    return veranstaltungsName + " " + klausurZeitraum + " " + (praesenz ? "in Präsenz" : "Online");
  }
}

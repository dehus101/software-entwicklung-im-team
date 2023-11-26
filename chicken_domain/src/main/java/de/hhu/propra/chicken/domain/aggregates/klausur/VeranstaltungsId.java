package de.hhu.propra.chicken.domain.aggregates.klausur;

import de.hhu.propra.chicken.domain.stereotypes.ValueObject;


/**
 * Darstellung einer gültigen VeranstaltungsId aus dem LSF.
 */
@ValueObject
class VeranstaltungsId {

  private final String veranstaltungsId;

  private VeranstaltungsId(String veranstaltungsId) {
    this.veranstaltungsId = veranstaltungsId;
  }


  public String getVeranstaltungsId() {
    return veranstaltungsId;
  }

  public static VeranstaltungsId erstelle(String veranstaltungsId) {
    return new VeranstaltungsId(veranstaltungsId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VeranstaltungsId that = (VeranstaltungsId) o;

    return veranstaltungsId.equals(that.veranstaltungsId);
  }

  @Override
  public int hashCode() {
    return veranstaltungsId.hashCode();
  }

}

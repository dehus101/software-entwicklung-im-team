package de.hhu.propra.chicken.domain.aggregates.klausur;

public record VeranstaltungsIdDto(String veranstaltungsId) {

  public VeranstaltungsId konvertiereZuVeranstaltungsId() {
    return VeranstaltungsId.erstelle(this.veranstaltungsId);
  }

}

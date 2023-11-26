package de.hhu.propra.chicken.domain.aggregates.student;

import de.hhu.propra.chicken.domain.aggregates.klausur.VeranstaltungsIdDto;
import org.springframework.data.relational.core.mapping.Embedded;

//@Table("klausur_referenz")
public record KlausurReferenzDto(
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) VeranstaltungsIdDto veranstaltungsId) {

  public KlausurReferenz konvertiereZuKlausurReferenz() {
    return new KlausurReferenz(veranstaltungsId.veranstaltungsId());
  }
}

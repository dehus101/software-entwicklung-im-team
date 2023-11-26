package de.hhu.propra.chicken.domain.aggregates.klausur;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

//@Table("klausur")
public record KlausurDto(@Id Long id,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         VeranstaltungsId veranstaltungsId,
                         String veranstaltungsName,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "klausur")
                         KlausurZeitraumDto klausurZeitraum,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "freistellung")
                         KlausurZeitraumDto freistellungsZeitraum,
                         boolean praesenz) {
}

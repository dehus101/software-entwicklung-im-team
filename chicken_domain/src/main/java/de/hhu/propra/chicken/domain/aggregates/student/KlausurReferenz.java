package de.hhu.propra.chicken.domain.aggregates.student;


import de.hhu.propra.chicken.domain.stereotypes.EntityObject;

/**
 * Referenziert Klausuren im Bezug zum Studenten.
 */
@EntityObject
public record KlausurReferenz(String id) {


}

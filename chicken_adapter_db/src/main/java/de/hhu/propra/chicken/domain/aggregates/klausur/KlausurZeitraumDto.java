package de.hhu.propra.chicken.domain.aggregates.klausur;

import java.time.LocalDate;
import java.time.LocalTime;

public record KlausurZeitraumDto(LocalDate datum, LocalTime startUhrzeit, LocalTime endUhrzeit) {


}

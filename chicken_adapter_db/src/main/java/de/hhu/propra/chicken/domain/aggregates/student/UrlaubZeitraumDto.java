package de.hhu.propra.chicken.domain.aggregates.student;

import java.time.LocalDate;
import java.time.LocalTime;

public record UrlaubZeitraumDto(LocalDate datum, LocalTime startUhrzeit, LocalTime endUhrzeit) {
}

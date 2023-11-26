package de.hhu.propra.chicken.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public record KlausurDto(
    @NotBlank(message = "Darf nicht leer sein")
    @NotNull(message = "Darf nicht leer sein")
    String veranstaltungsId,
    @NotBlank(message = "Darf nicht leer sein")
    @NotNull(message = "Darf nicht leer sein")
    String veranstaltungsName,
    @NotNull
    Boolean praesenz,
    @NotNull(message = "Darf nicht leer sein")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate klausurdatum,
    @NotNull(message = "Darf nicht leer sein")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime klausurstart,
    @NotNull(message = "Darf nicht leer sein")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime klausurende
) {

}

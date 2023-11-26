package de.hhu.propra.chicken.domain.aggregates.student;

import java.util.Set;
import org.springframework.data.annotation.Id;

public record StudentDto(@Id Long id,
                         String githubhandle,
                         Set<UrlaubZeitraumDto> urlaube,
                         Set<KlausurReferenzDto> klausuren) {

  public StudentDto(Long id, String githubhandle, Set<UrlaubZeitraumDto> urlaube,
                    Set<KlausurReferenzDto> klausuren) {
    this.id = id;
    this.githubhandle = githubhandle;
    this.urlaube = Set.copyOf(urlaube);
    this.klausuren = Set.copyOf(klausuren);
  }


}

package de.hhu.propra.chicken.dao;

import de.hhu.propra.chicken.domain.aggregates.klausur.KlausurDto;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface KlausurDao extends CrudRepository<KlausurDto, String> {

  @Query(
      """
          SELECT * FROM klausur_dto
          WHERE date = :datum;
          """
  )
  Set<KlausurDto> findeKlausurenAmTag(@Param("datum") LocalDate datum);

  @Query("""
      SELECT * FROM klausur_dto
      WHERE veranstaltungs_id = :veranstaltungsId
      """
  )
  Optional<KlausurDto> findeKlausurMitVeranstaltungsId(
      @Param("veranstaltungsId") String veranstaltungsId);

  Set<KlausurDto> findAll();

}

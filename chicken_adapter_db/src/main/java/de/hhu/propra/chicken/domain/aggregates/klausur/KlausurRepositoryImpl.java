package de.hhu.propra.chicken.domain.aggregates.klausur;

import de.hhu.propra.chicken.dao.KlausurDao;
import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class KlausurRepositoryImpl implements KlausurRepository {

  private final KlausurDao klausurDao;
  private final LocalDate praktikumsstart;
  private final LocalDate praktikumsende;

  public KlausurRepositoryImpl(KlausurDao klausurDao,
                               @Value("${praktikumszeitraum.praktikumsstart}")
                                   String praktikumsstart,
                               @Value("${praktikumszeitraum.praktikumsende}")
                                   String praktikumsende) {
    this.klausurDao = klausurDao;
    this.praktikumsstart = LocalDate.parse(praktikumsstart);
    this.praktikumsende = LocalDate.parse(praktikumsende);
  }

  private static KlausurDto konvertiereZuKlausurDto(Klausur klausur) {
    return new KlausurDto(klausur.id(), klausur.veranstaltungsId(), klausur.veranstaltungsName(),
        new KlausurZeitraumDto(klausur.klausurZeitraum().getDatum(),
            klausur.klausurZeitraum().getStartUhrzeit(), klausur.klausurZeitraum().getEndUhrzeit()),
        new KlausurZeitraumDto(klausur.freistellungsZeitraum().getDatum(),
            klausur.freistellungsZeitraum().getStartUhrzeit(),
            klausur.freistellungsZeitraum().getEndUhrzeit()),
        klausur.praesenz());
  }

  @Override
  public Klausur findeKlausurMitVeranstaltungsId(String id) {
    KlausurDto klausurDto = klausurDao.findeKlausurMitVeranstaltungsId(id)
        .orElseThrow(() -> new NoSuchElementException(id));
    return konvertiereZuKlausur(klausurDto);
  }

  private Klausur konvertiereZuKlausur(KlausurDto klausurDto) {
    return new Klausur(
        klausurDto.id(),
        klausurDto.veranstaltungsId(),
        klausurDto.veranstaltungsName(),
        ZeitraumDto.erstelleZeitraum(
            klausurDto.klausurZeitraum().datum(),
            klausurDto.klausurZeitraum().startUhrzeit(),
            klausurDto.klausurZeitraum().endUhrzeit(),
            praktikumsstart,
            praktikumsende),
        ZeitraumDto.erstelleZeitraum(
            klausurDto.freistellungsZeitraum().datum(),
            klausurDto.freistellungsZeitraum().startUhrzeit(),
            klausurDto.freistellungsZeitraum().endUhrzeit(),
            praktikumsstart,
            praktikumsende),
        klausurDto.praesenz());
  }

  @Override
  @Transactional
  public void speicherKlausur(Klausur klausur) {
    try {
      findeKlausurMitVeranstaltungsId(klausur.getVeranstaltungsId());
      throw new RuntimeException("Klausur wurde bereits angemeldet");
    } catch (NoSuchElementException e) {
      KlausurDto klausurDto = konvertiereZuKlausurDto(klausur);
      klausurDao.save(klausurDto);
    }
  }

  @Override
  public Set<Klausur> findAll() {
    return klausurDao.findAll()
        .stream()
        .map(this::konvertiereZuKlausur)
        .collect(Collectors.toSet());
  }


}

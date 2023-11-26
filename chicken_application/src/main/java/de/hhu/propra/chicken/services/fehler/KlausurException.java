package de.hhu.propra.chicken.services.fehler;

public class KlausurException extends RuntimeException {
  public KlausurException(String fehler) {
    super(fehler);
  }
}

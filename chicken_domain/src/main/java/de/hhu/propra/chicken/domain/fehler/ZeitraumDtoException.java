package de.hhu.propra.chicken.domain.fehler;

public class ZeitraumDtoException extends RuntimeException {
  public ZeitraumDtoException(String fehler) {
    super(fehler);
  }
}
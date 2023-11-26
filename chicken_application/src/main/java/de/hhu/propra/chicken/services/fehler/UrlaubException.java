package de.hhu.propra.chicken.services.fehler;

public class UrlaubException extends RuntimeException {
  public UrlaubException(String fehler) {
    super(fehler);
  }
}

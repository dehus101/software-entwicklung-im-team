package de.hhu.propra.chicken.services.fehler;

public class VeranstaltungsIdException extends RuntimeException {
  public VeranstaltungsIdException(String fehler) {
    super(fehler);
  }
}

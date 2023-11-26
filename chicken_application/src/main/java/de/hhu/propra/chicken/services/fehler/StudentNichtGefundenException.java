package de.hhu.propra.chicken.services.fehler;

public class StudentNichtGefundenException extends RuntimeException {
  public StudentNichtGefundenException(String githubHandle) {
    super(githubHandle);
  }
}

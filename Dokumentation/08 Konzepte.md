# 08 Konzepte

---
---

## Anmeldung / Rollenverteilung

Um das System nutzen zu können muss man bei GitHub registriert sein. Über die `application.yml` Datei bekommt das System die Information, welche Nutzer Tutorinnen und Tutoren oder Organisatorinnen und Organisatoren sind. Jeder Benutzer erhält nach der Anmeldung die passende Rolle und kann auf die Funktionen zugreifen, die für ihn vorgesehen sind.

## Submodule und ArchUnit Tests

Damit wir die Onion-Architektur einhalten, benutzen wir Submodule und ArchUnit-Tests. Außerdem prüfen wir, ob wir die Regeln des Domain-Driven-Designs in Bezug auf Aggregate und deren Berechtigungen einhalten.

## Validierung

Damit die Regeln der Belegung der Urlaube und Klausuren eingehalten werden, überprüfen wird die Anfragen, bevor diese
gespeichert werden. Sie werden einerseits auf Vollständigkeit aber auch auf die definierten Regeln überprüft.

Im Falle der Belegung eines Urlaubes werden mehrere Zusammenhänge betrachtet. Abhängig davon, ob an dem Tag des eingereichten Urlaubsantrags bereits eine Klausur geschrieben wird, greifen unterschiedliche Regelungen:
* Sollte keine Klausur belegt sein, so wird validiert, ob der Student genügend Urlaub zur Verfügung hat, an dem Tag schon Urlaub belegt hat und wenn ja wie oft, und ob die Länge des Urlaubs korrekt ist. 
* Wurde bereits eine Klausur belegt, so wird überprüft, ob sich der bereits vorhandene Klausurzeitraum mit dem eingereichten Urlaubsantrag schneidet. 
	* Sollte dies der Fall sein, so wird der Verschnitt wieder gutgeschrieben. 
	* Ansonsten wird der eingereichte Urlaubsantrag gespeichert. 
* Zusätzlich wird immer überprüft, ob der eingereichte Urlaubsantrag mit bereits vorhanden Urlauben im Konflikt steht.

Während der Anmeldung einer Klausur wird die angegebene Veranstaltungs-Id mit dem Veranstaltungsverzeichnis der HHU gegengeprüft.

Bei der Belegung oder Stornierung eines Urlaubes oder einer Klausur wird immer überprüft, ob diese in der Vergangenheit liegt. Kommt es zu einem solchen Versuch, wird die Anfrage abgelehnt.

Es wird ebenfalls immer geprüft, ob sich die Anträge innerhalb des Praktikumszeitraumes befinden.

## Fehlerhandhabung
Sollte bei einer Aktion ein Fehler auftreten, z.B. es wird verucht zu viel Urlaub zu buchen, so wird eine entsprechende Exception geworfen: 
- UrlaubsException, bei Verletzung von Urlaubsregeln
- KlausurException, bei Verletzung von Klausursregeln
- ZeitraumDtoException, bei Verletzung von Zeitraumregeln
- StudentNichtGefundenException, sollte ein Student nicht in der Datenbank gespeichert sein
- VeranstaltungsIdException, sollte eine VeranstaltungsId nicht korrekt sein
Diese Fehlermeldungen werden an den Controller weitergeleitet und im HTML angezeigt.

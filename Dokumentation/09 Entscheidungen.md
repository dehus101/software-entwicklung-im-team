# Entwurfsentscheidungen

---
---

## Onion Architektur
Wir haben uns gegen Microservices entschieden, weil wir der Meinung waren, dass sich für die doch überschaubare Projektgröße eine Onion Architektur besser eignen würde und übersichtlicher wäre.
Außerdem haben wir uns dadurch die Kommunikation zwischen verschiedenen Systemen gespart.
Durch eine Onion Architektur haben wir uns erhofft eine geringere Kopplung zu haben und das Single-Responsibility-Prinzip besser einhalten zu können.

## Domain-Driven-Design
Wir haben unser Projekt nach dem Domain-Driven-Design strukturiert. Dabei haben wir die Verschiedenen Informationen in Aggregate unterteilt:

### Student
Der Student enthält lediglich Informationen zu seinem belegten Urlaub bzw. Klausuren. Er hat nur Referenzen zu den Klausuren, um eine bessere Abgrenzung der Aggregate zu haben.

### Klausur
Die Klausur hat eine VeranstaltungsId, einen Veranstaltungsnamen, einen Klausurzeitraum, einen Freistellungszeitraum und die Information, ob die Klausur in präsenz stattfindet. Die beiden Zeiträume werden getrennt von einander gespeichert, um bei der Belegung mehrerer Klausuren an einem Tag, Überschneidungen von Klausuren zu vermeiden. 
Diese Trennung erfolgte nachdem wir festgestellt hatten, dass mit der vorherigen Implementation von nur einem Zeitraum (freigestellter Zeitraum), Klausuren sich Überschneiden würden, obwohl sich nur deren freigestellten Zeiträume überschneiden.

#### Veranstaltungs ID
Wir haben uns dazu entschieden die VeranstaltungsId separat als eigene Klasse zur definieren und die Id als String zu speichern, entgegengesetzt zur Darstellung des LSF als Zahl. Dies macht unser Programm weniger Anfällig für Fehler durch Änderungen seitens des LSF an der Darstellung.

### ZeitraumDto
Während der Erstellung eines Zeitraumes werden die vorgegebenen Regeln Überprüft (z.B 15-Minuten Blöcke, etc. ...). Somit stellen wir sicher, dass keine invaliden Zeiträume gebucht und erstellt werden können.
Anfänglich waren die Praktikumszeiträume fest definiert in der Klasse. Um die Konfigurierbarkeit der Anwendung zu ermöglichen, haben wir uns dazu entschieden, bei der Erstellung jedes Zeitraumes den Praktikumszeitraum zu übergeben. 
Das Object wird als Data-Transfer-Objekt aufgefasst, da es innerhalb der Anwendung an verschiedenen Stellen verwendet wird und nur Daten enthält, wenn sie valiede sind. Somit wird immer sichergestellt, dass nicht versucht wird einen illegalen Zeitraum zu erstellen.

## Validierung von Überschneidungen
Die Validierung von Überschneidungen haben wir im Application-Layer angesiedelt. Wir kamen zu dieser Entscheidung, da es nur an dieser Stelle möglich ist auf die Daten der Klausuren aus der Datenbank zuzugreifen. Das Student-Aggregat enthält lediglich Referenzen auf die belegten Klausuren. Es käme bei einer Ansiedelung der Validierung in der Domain zu mehrfachen Wiederholungen im Code.

## Spring Data JDBC
Da wir unser Projekt nach Domain Driven Design modelliert haben, hat es sich angeboten Spring Data JDBC für die Zugriffe unserer Datenbank zu verwenden.

## Spring Security OAuth2
Da wir ein Spring Boot System entwickelt haben, haben wir Spring Security OAuth2 benutzt, um die Sicherheit in unserem Programm zu gewährleisten.

## Gradle-Submodule
Wir haben uns dazu entschieden, entgegengesetzt zu den vorgestellten Varianten, Gralde-Submodule zu verwenden, da es uns am einfachsten ermöglichte die Regeln der Onion Architektur einzuhalten. Dadurch kann mit Paket-Sichtbarkeit gearbeitet werden und es müssen nicht viele ArchUnit-Test geschrieben werden, die Verletzungen dieser Art überprüfen.

## Maria-Datenbank
Wir haben uns dazu entschlossen eine MariaDB-Datenbank zu verwenden, 
weil wir eine relationale Datenbank brauchten, welche dazu noch eine gute Performance leisten kann.

## Checkstyle & SpotBugs
Da wir in den Wochenblättern mit CheckStyle und SpotBugs gearbeitet haben und uns somit schon damit auskennen, haben wir diese beiden statischen Checker für die Sicherstellung der Codequalität verwendet.

## Rollenverteilung über YML-Konfiguration
Um die Benutzertypen bzw. die Rollen in unserem Programm festzulegen, haben wir uns für eine YML-Config Datei entschieden, in welcher die Tutoren und Organisatoren aufgelistet werden. Desweiteren wird hier der Praktikumszeitraum angegeben.

## Accessabilty
Damit auch Menschen mit Einschränkungen unsere Website benutzten können, haben wir unsere HTML-Seiten entsprechend angepasst. Dabei wurde darauf geachtet, dass Screenreader-Programme unser Website richtig vorlesen können. Desweiteren haben wir auf die Farbwahl acht gegeben, damit immer genügend Kontrast zwischen Text und Hintergrund besteht.
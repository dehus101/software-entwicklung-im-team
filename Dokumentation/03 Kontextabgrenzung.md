# 03 Kontextabgrenzung

---
---
## Abgrenzung
- Das System verwaltet die Freistellungen der Studierenden vom Praktikum des Kurses ProPra 2. 
- Es überprüft ob die angegebenen Veranstaltungs ID existieren, aber __nicht__ ob die angegebenen Veranstaltungsnamen korrekt ist. 
- Die Authentifizierung der Studierenden bzw. der Tutoren und Organisatoren erfolgt durch den Authetification-Provider GitHub. 
- Es wird ebenfalls __nicht__ überprüft, ob die Person, die verucht sich bei unserer Website anzumelden, zur Organisation von ProPra 2 gehört.
![[Kontextabgrenzung.png]]
## Authentifizierungs Ablauf
(http://chicken.org/ Beispielhafte Url)
![[Auth-Flow.png]]
## Komponenten
![[Komponenten.png]]
### Datenbank-Adapter
Speichert Studenteninformationen:
* GitHub-Handle
* Belegte Urlaube
* Belegte Klausuren

Speichert Klausurinformationen:
* Veranstaltungs ID
* Veranstaltungs Name
* Klausurzeitraum
* Freistellungszeitraum
* Präsenz oder nicht

*Darstellung der Beziehungen innerhalb der Datenbank:*
![[klausur_dto.png]]

### Web-Adapter
Der Studentencontroller bietet folgende Schnittstellen an:
* Übersicht 
* Klausurbelegung
* Klausuranmeldung
* Urlaubsbelegung
* Urlaubsstornierung
* Klausurstronierung

Die Controller für die Organisatoren und Tutoren haben keine Funktionalität. Sie sind lediglich mit der entsprechenden Berechtigung erreichbar. Dafür exitieren die Routen:
- ``/tutor/logs``
- ``/organisator/logs``

### LSF-Adapter
Baut eine Verbindung zum LSF auf, um die angegebene Veranstaltungs-Id zu überprüfen.

### Chicken-Service
Übernimmt die Validierung der Anfragen zur Belegung oder Stornierung von Urlauben und Klausuren. Speichert bei erfolgreicher Überprüfung die Anfragen in der Datenbank.

#### Logging
Alle Änderungen an belegten Urlauben bzw. Klausuren werden mittels des Loggings in einer Log-Datei festgehalten. Diese kann nicht durch die Benutzer verändert werden, lediglich das Programm kann weitere Einträge hinzufügen.

#### Heutiges Datum
Liefert das heutige Datum und die aktuelle Zeit. Hilfreich, um das Testing zu erleichtern.

### Domain
Definiert Aggregate und spiegelt die Geschäftslogik wieder:
* Studierende
* Klausuren
* Zeiträume (Kein Aggregat, aber von beiden benötigetes Objekt)

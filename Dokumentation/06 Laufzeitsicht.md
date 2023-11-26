# 06 Laufzeitsicht

---
---

Die Anfragen, die das System während der Laufzeit über die Benutzereingaben bekommt, werden den zuständigen Controllern übergeben.
Die Controller geben die Daten an den entsprechenden Service weiter, worin sie verarbeitet werden. Neue Informationen werden sowohl an die Controller zurückgegeben, als auch in der Datenbank über die Repositories gespeichert.

Im Folgenden werden ein paar Abläufe der Funktionen erklärt:

### Urlaub belegen:
Studenten geben einen Urlaubzeitraum den sie belegen wollen ein. 
Diese Informationen werden dann vom Controller an den Service geleitet. Nach einiger Validierung wird der Urlaub im Repository gespeichert und der Student an die Übersichtsseite weitergeleitet.
Hier kann er einsehen wie viel Resturlaub der Student noch zur Verfügung und welche Urlaube er schon belegt hat.

(Beispielhafter Ablauf der Urlaubsbelegung. Komponentenaufruf der folgenden Funktionen ähnlich.)
![[Urlaub Belegen Illustration.png]]

### Klausur anmelden:
Studenten können eine Klausur anmelden, um diese in der Klausurliste zur Auswahl zu haben.
Die Klausur wird im Controller entgegengenommen und dann über den Service ans Repository zum Speichern in der Datenbank geleitet. Es wird vorher überprüft, ob die angegebene Veranstaltungs ID der Klausur valide ist und nicht bereits verwendet wurde um eine Klausur anzumelden.

### Klausur belegen:
Studenten wählen eine vorhandene Klausur von der Klausurliste aus, die vorher angemeldet wurde.
Diese Informationen wird ebenfalls dann vom Controller in den Service und ins Repository gegeben, damit die Klausur und der Klausurzeitraum gespeichert werden.

Mögliche Fälle bei der Belegung einer Klausur und darauf folgende Aktionen:

- **Fall 1**: Kein Urlaub an dem Tag  
  - Klausur hinzufügen  
- **Fall 2**: Urlaub an dem Tag  
  - Fall 1: Urlaub fängt vor der Klausur an und hört innerhalb des Klausurzeitraums auf  
      - Urlaub schneiden  
      - alten aus student löschen  
      - geschnittenen Urlaub hinzufügen  
      - Klausur hinzufügen  
  - Fall 2: Urlaub fängt vor der Klausur an und hört vor der Klausur auf  
      - Klausur hinzufügen  
  - Fall 3: Urlaub fängt vor der Klausur an und hört nach der Klausur auf  
      - Urlaub schneiden  
      - alten aus student löschen  
      - geschnittenen Urlaub hinzufügen  
      - Klausur hinzufügen  
  - Fall 4: Urlaub fängt innerhalb der Klausur an und hört nach der Klausur auf  
      - Urlaub schneiden  
      - alten aus student löschen  
      - geschnittenen Urlaub hinzufügen  
      - Klausur hinzufügen  
  - Fall 5: Urlaub fängt innerhalb der Klausur an und hört innerhalb der Klausur auf  
      - vorhandenen Urlaub löschen  
      - Klausur hinzufügen  
  - Fall 6: Urlaub fängt nach der Klausur an und hört nach der Klausur auf  
      - Klausur hinzufügen  
- **Fall 3**: Zwei Klausuren schneiden sich  
  - Klausurbelegung ablehnen.

### Urlaub- oder Klausur stornieren:
Studenten haben die Möglichkeit ihre Freistellungen zu stornieren.
Diese Informationen wird ebenfalls dann vom Controller in den Service und ins Repository gegeben, damit der Urlaub- oder Klausur zeitraum gelöscht wird.


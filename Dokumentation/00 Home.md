# Projekt mit @dehus101 @fnellen @ernaz100 @pifis102 @TeeJaey

---
---

## Starten des Systems

Das Projekt benutzt Docker sowie `docker-compose`, beides sollte installiert sein.

Es ist wichtig, dass die Umgebungsvariablen `CLIENT_ID` sowie `CLIENT_SECRET` gesetzt sind.

Zur Festlegung der systeminternen Rollen, sowie der Praktikumszeiten im Format *yyyy-mm-dd*, werden diese in der
Datei `chicken_adapater_web/src/main/resources/application.yml` definiert:

```
rollen:
    tutoren: GitHubHandleX, GitHubHandleY
    organisatoren: GitHubHandleX, GitHubHandleY

praktikumszeitraum:
    praktikumsstart: Datum (YYYY-MM-DD)
    praktikumsende: Datum (YYYY-MM-DD)
```

Wenn alle vorigen Schritte ausgeführt wurden, kann das Projekt einfach mit
**docker-compose-dev.yml** und mit dem Befehl `gradle bootRun` gestartet werden.



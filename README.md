# Urlaubs- und Klausurverwaltung für ProPra 2

## TL;DR

Unser Java Spring Boot-Projekt zur Urlaubs- und Klausurverwaltung für den ProPra 2 Kurs an der HHU zeigt umfassende Auseinandersetzung mit den Themen des Domain-Driven-Designs und verschiedenen Architekturmustern. Die Anwendung ermöglicht die flexible Planung von Urlaub und Klausuranmeldungen und gewährleistet Sicherheit, Wartbarkeit, Konfigurierbarkeit und Barrierefreiheit. Die klare Architektur folgt den Prinzipien der Onion-Architektur und des Domain-Driven-Designs, während Bootstrap-Templates für ein ansprechendes Frontend und MariaDB als Datenbank für effiziente Datenspeicherung sorgen.

## Beschreibung

Unsere Java Spring Boot-Anwendung bietet eine benutzerfreundliche Lösung für die Organisation von Urlaubs- und Klausuranmeldungen während des dreiwöchigen ProPra 2 Praktikums. Studierende können 4 Stunden Urlaub für die Praktikumsdauer nehmen und ihre Planung flexibel gestalten. Klausuranmeldungen sind ebenfalls möglich, mit der Option zur Stornierung bis zum Vortag. Ein detailliertes Log dokumentiert sämtliche Änderungen.

## Erforderliche Programmierkenntnisse

- **Java Spring Boot**: Backend-Entwicklung unter Verwendung des Spring-Frameworks für eine effiziente und skalierbare Serverarchitektur.

- **Datenbankmanagement**: Einsatz von MariaDB für die effiziente Speicherung von Nutzerinformationen.

- **Webentwicklung mit Bootstrap**: Verwendung von Bootstrap-Templates für ein ansprechendes und responsives Frontend.

- **Authentifizierung und Autorisierung**: Implementierung sicherer Zugriffsmechanismen für den geschützten Bereich der Anwendung.

- **Barrierefreie Gestaltung (a11y)**: Berücksichtigung von a11y-Prinzipien für eine zugängliche Benutzeroberfläche.

- **Architekturdesign**: Strikte Einhaltung der Onion-Architektur und des Domain-Driven-Designs für eine klare und wartbare Codebasis.

## Anforderungen

- **Wartbarkeit**: Das System ist modular aufgebaut, um Wartungsarbeiten ohne Ausfallzeiten zu ermöglichen.

- **Sicherheit**: Zugriff nur für authentifizierte Personen, um Änderungen nachvollziehbar zu machen.

- **Konfigurierbarkeit**: Benutzerfreundliche Konfigurationsschnittstelle für einfache Anpassungen durch Organisatoren.

- **Barrierefreiheit (a11y)**: Zugängliche Weboberfläche für Menschen mit Einschränkungen.

- **Architektureinhaltung**: Strikte Umsetzung der Onion-Architektur und des Domain-Driven-Designs für klare Struktur und Erweiterbarkeit.

## Benutzung

1. Studierende können ihre Urlaubs- und Klausurzeiten flexibel planen.

2. Stornierungen sind bis zum Vortag möglich.

3. Alle Änderungen werden in einem Log erfasst.

## Qualitätsziele im Detail

### Wartbarkeit

Modularer Aufbau ermöglicht einfache Wartungsarbeiten ohne Ausfallzeiten.

### Sicherheit

Authentifizierung gewährleistet nur autorisierten Zugriff, um Änderungen nachvollziehbar zu machen.

### Konfigurierbarkeit

Benutzerfreundliche Konfigurationsschnittstelle für einfache Anpassungen durch Organisatoren.

### Barrierefreiheit (a11y)

Zugängliche Weboberfläche erleichtert die Bedienung mittels Screenreaders.

### Architektureinhaltung

Strikte Umsetzung der Onion-Architektur und des Domain-Driven-Designs für klare Struktur und Erweiterbarkeit.

# MessengerX

## Opis

Aplikacja MessengerX jest platformą do komunikacji między użytkownikami poprzez chaty indywidualne oraz grupowe. Umożliwia tworzenie grup, wysyłanie powiadomień oraz organizowanie ankiet na czatach grupowych.

## Motywacja
Motywacją do podjęcia tematyki chatowania była chęć zapoznania się z systemami czasu rzeczywsitego jak WebSockets, SSE oraz poszerzenie wiedzy o Spring MVC.

## Funkcjonalności

### Must-have:

**Chatowanie indywidualne:**
- Stworzenie interfejsu użytkownika do wysyłania i odbierania wiadomości prywatnych.
- Implementacja logiki serwera obsługującej wymianę wiadomości między użytkownikami.

**Chatowanie grupowe:**
- Stworzenie interfejsu użytkownika do wysyłania i odbierania wiadomości w grupach.
- Implementacja logiki serwera obsługującej wiadomości w grupach.

**Wysyłanie powiadomień:**
- Konfiguracja mechanizmu wysyłania powiadomień na podstawie określonych zdarzeń lub aktywności użytkowników.
- Implementacja wysyłania powiadomień do odpowiednich użytkowników.

### Nice-to-have:

**Rejestracja oraz logowanie:**
- Stworzenie formularzy rejestracji i logowania dla użytkowników.
- Implementacja mechanizmów uwierzytelniania i autoryzacji użytkowników.

**Użytkownicy posiadający role na czatach:**
- Stworzenie mechanizmu nadawania i zarządzania rolami użytkowników w grupowych czatach.
- Implementacja interfejsu administracyjnego do zarządzania rolami.

**Udostępnianie postów widocznych dla wszystkich:**
- Implementacja możliwości tworzenia i udostępniania postów na czatach grupowych.
- Stworzenie interfejsu użytkownika umożliwiającego udostępnianie postów.

**Tworzenie ankiet:**
- Stworzenie interfejsu użytkownika do tworzenia ankiet na czatach grupowych.
- Implementacja mechanizmu obsługi ankiet.

### Dodatkowe:

**Wyszukiwanie w starych konwersacjach:**
- Implementacja funkcji wyszukiwania w archiwalnych rozmowach.
- Stworzenie interfejsu użytkownika umożliwiającego wyszukiwanie w starych wiadomościach.


## Technologie

- **Java**: Język programowania, w którym napisana będzie główna logika aplikacji.
- **Spring Framework**: Framework do tworzenia aplikacji Java, który zapewnia wsparcie dla budowania aplikacji webowych, obsługi żądań HTTP i zarządzania zależnościami.
- **Spring Boot**: Ułatwia szybkie tworzenie aplikacji Spring, zapewniając konfigurację domyślną i automatyczne konfigurowanie większości aspektów aplikacji.
- **Spring Data**: Umożliwia łatwe dostarczanie warstwy dostępu do danych w aplikacjach Spring.
- **Spring Security**: Zapewnia mechanizmy uwierzytelniania, autoryzacji i zarządzania sesjami dla aplikacji Spring.
- **WebSocket**: Technologia do obsługi komunikacji w czasie rzeczywistym między klientem a serwerem.
- **Server Sent Events**: Technologia do obsługi komunikacji jednostronnej, gdzie serwer wysyła powiadomienia do klienta.
- **Hibernate (opcjonalnie)**: Biblioteka ORM do mapowania obiektowo-relacyjnego, która może być użyta w połączeniu z Spring Data do zarządzania danymi w bazie danych.
- **Elastyczne wyszukiwarki (np. Elasticsearch)**: Biblioteka pomocnicza do efektywnego wyszukiwania elementów w starych konwersacjach

## Licencja

Ten projekt jest objęty licencją MIT - sprawdź plik `LICENSE` dla szczegółów.

---
Autor: Mateusz

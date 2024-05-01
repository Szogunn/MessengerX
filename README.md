# MessengerX

## Opis

Aplikacja MessengerX jest platformą do komunikacji między użytkownikami poprzez chaty indywidualne oraz grupowe.
Umożliwia tworzenie grup, wysyłanie powiadomień oraz organizowanie ankiet na czatach grupowych.

## Motywacja

Motywacją do podjęcia tematyki chatowania jest chęć zapoznania się z systemami czasu rzeczywsitego jak WebSockets, SSE
oraz poszerzenie wiedzy o Spring MVC.

## Funkcjonalności

### Must-have:

**Rejestracja oraz logowanie:**

- [X] Stworzenie formularzy rejestracji i logowania dla użytkowników.
- [X] Implementacja mechanizmów uwierzytelniania i autoryzacji użytkowników.

**Chatowanie indywidualne:**

- [X] Stworzenie interfejsu użytkownika do wysyłania i odbierania wiadomości prywatnych.
- [X] Implementacja logiki serwera obsługującej wymianę wiadomości między użytkownikami.

**Dostęp do starych wiadomości:**

- [X] Implementacja logiki serwera obsługującej dostęp do starych wiadomości bez wyszukiwania.

**Wysyłanie powiadomień:**

- [ ] Konfiguracja mechanizmu wysyłania powiadomień na podstawie określonych zdarzeń lub aktywności użytkowników.
  -  [X] Zaproszenie do znajmomych
  -  [ ] Informacje o przyjęciu lub odrzuceniu zaproszenia jako powiadomienie (przeprocesowanie jako odczytanie - kliknięcie)
  -  [ ] Dedykowane okno do wyświetlenia nie przeprocesowanych powiadomień
  -  [X] Powiadomienie o zmianie statusu użytkownika
  -  [ ] Powiadomienie o przychodzącej wiadomości od użytkownika z którym aktualnie nie chatuje 

### Nice-to-have:
**Chatowanie grupowe:**

- [ ] Stworzenie interfejsu użytkownika do wysyłania i odbierania wiadomości w grupach.
- [ ] Implementacja logiki serwera obsługującej wiadomości w grupach.

**Użytkownicy posiadający role na czatach:**

- [ ] Stworzenie mechanizmu nadawania i zarządzania rolami użytkowników w grupowych czatach.
- [ ] Implementacja interfejsu administracyjnego do zarządzania rolami.

**Udostępnianie postów widocznych dla wszystkich:**

- [ ] Implementacja możliwości tworzenia i udostępniania postów na czatach grupowych.
- [ ] Stworzenie interfejsu użytkownika umożliwiającego udostępnianie postów.

**Tworzenie ankiet:**

- [ ] Stworzenie interfejsu użytkownika do tworzenia ankiet na czatach grupowych.
- [ ] Implementacja mechanizmu obsługi ankiet.

### Dodatkowe:

**Wyszukiwanie w starych konwersacjach:**

- [ ] Implementacja funkcji wyszukiwania w archiwalnych rozmowach.
- [ ] Stworzenie interfejsu użytkownika umożliwiającego wyszukiwanie w starych wiadomościach.

**Zewnętrzny broker:**

- [ ] Zaimplementowanie zewnętrznego brokerka (np. RabbitMQ, Kafka) do przetwarzania wiadomości.

**Baza danych w pamięci:**

- [ ] Zaimplementowanie bazy danych wykorzystujący mechanizm cacheowania(np. Redis), do przetrzymywania dynamicznych danych.

## Technologie

- [Java 21](https://openjdk.org/projects/jdk/21/) : Język programowania, w którym napisana będzie główna logika aplikacji.
- [Maven](https://maven.apache.org/) : Automatyzacja budowy projektów opartych o język Java
- [Spring Framework](https://spring.io/projects/spring-framework) : Framework do tworzenia aplikacji Java, który zapewnia wsparcie dla budowania aplikacji webowych, obsługi żądań HTTP i zarządzania zależnościami.
- [Spring Boot 3](https://spring.io/projects/spring-boot) : Ułatwia szybkie tworzenie aplikacji Spring, zapewniając konfigurację domyślną i automatyczne
  konfigurowanie większości aspektów aplikacji.
- [Spring Data](https://spring.io/projects/spring-data) : Umożliwia łatwe dostarczanie warstwy dostępu do danych w aplikacjach Spring.
- [Spring Security](https://spring.io/projects/spring-security) : Zapewnia mechanizmy uwierzytelniania, autoryzacji i zarządzania sesjami dla aplikacji Spring.
- [Spring Session](https://spring.io/projects/spring-session) : Wykorzystanie Spring Session do utrzymania sesji http dla protokołu **WebSocket** wykorzystywanego do komunikacji między klientem a serwerem w czasie rzeczywistym
- [Server Sent Events](https://developer.mozilla.org/en-US/docs/Web/API/EventSource) : Technologia do obsługi komunikacji jednostronnej, gdzie serwer wysyła powiadomienia do
  klienta.
- [NoSQL database](https://www.mongodb.com): Nierelacyjna baza danych typu document store do przechowywania danych np. MongoDB.
- [Thymeleaf](https://www.thymeleaf.org/) : Prosta warstwa fronentdowa z wykorzystaniem silnika Thymeleaf
- [Wyszukiwarki (np. Elasticsearch)](https://www.elastic.co/) : Biblioteka pomocnicza do efektywnego wyszukiwania elementów w starych
  konwersacjach

## Pomocne linki

- https://medium.com/@m.romaniiuk/system-design-chat-application-1d6fbf21b372 (architektura)

### Licencja

Ten projekt jest objęty licencją MIT - sprawdź plik `LICENSE` dla szczegółów.

---
Autor: Mateusz

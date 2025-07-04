# marcin-dolecki-kodilla-java-tasks

## 📌 Opis aplikacji

### 🗄️ Baza danych

Aplikacja korzysta z bazy danych **MySQL** jako źródła trwałego przechowywania danych. Dane obiektowe w Javie są mapowane na rekordy w bazie danych przy użyciu JPA i encji `Task`. Komunikacja z bazą danych realizowana jest poprzez interfejs `TaskRepository`, który dziedziczy po `CrudRepository` dostarczonym przez **Spring Data JPA**, umożliwiającym wykonywanie podstawowych operacji **CRUD** (Create, Read, Update, Delete) na encjach bazodanowych.

---

### 📚 Użyte biblioteki i technologie

Projekt wykorzystuje następujące biblioteki i frameworki:

- **Spring Boot** - główny framework aplikacji, zapewniający m.in. automatyczną konfigurację, uruchamianie servera aplikacji oraz kontrolery REST.
- **Spring Web (Spring MVC)** - do obsługi żądań HTTP, mapowania endpointów i budowania REST API.
- **Spring Data JPA** - do komunikacji z bazą danych w sposób obiektowy (ORM), przy użyciu interfejsu `CrudRepository`.
- **Lombok** - do automatycznego generowania getterów, konstruktorów itp., za pomocą adnotacji takich jak `@Getter`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@RequiredArgsConstructor`.
- **Jakarta Persistence (JPA)** - do oznaczania klas jako encje (`@Entity`) oraz definiowania pól bazy danych (`@Column`, `@Id`).
- **MySQL** – jako baza danych (poprzez `mysql-connector-java`).

---

### 🔁 Obsługiwane metody HTTP

Aplikacja udostępnia REST API, które obsługuje podstawowe operacje CRUD na zasobie `Task`. Używane metody HTTP:

| Metoda | Endpoint             | Opis                                             |
|--------|----------------------|--------------------------------------------------|
| GET    | `/v1/tasks`          | Pobranie wszystkich zadań                        |
| GET    | `/v1/tasks/{id}`     | Pobranie zadania o wskazanym ID                  |
| POST   | `/v1/tasks`          | Utworzenie nowego zadania                        |
| PUT    | `/v1/tasks`          | Aktualizacja zadania o wskazanym ID (przez body) |
| PUT    | `/v1/tasks/{id}`     | Aktualizacja zadania o wskazanym ID (przez URL)  |
| DELETE | `/v1/tasks/{id}`     | Usunięcie zadania o danym ID                     |

Zwracane są odpowiednie kody HTTP, np. `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, zgodnie z dobrymi praktykami REST API.

---

### ✅ Dodatkowe informacje

- Obsługa błędów realizowana jest globalnie przez klasę `GlobalHttpErrorHandler`, która przechwytuje wyjątek `TaskNotFoundException`.
- Obiekty DTO (`TaskDto`) są mapowane na encje (`Task`) za pomocą klasy `TaskMapper`.
- Aplikacja została napisana w języku **Java**, w oparciu o konwencje warstw: kontroler, serwis, repozytorium.
# TennisScoreBoard

Веб-приложение для ведения счёта теннисного матча в реальном времени: создание матча между двумя игроками, начисление очков с автоматическим пересчётом гейма/сета/матча по теннисным правилам (включая тай-брейк), а также список завершённых матчей с постраничной навигацией и поиском по имени игрока.

## Стек

- **Backend:** Java 25, Spring MVC (Spring Framework 7, без Spring Boot), Hibernate 6 / JPA, HikariCP, PostgreSQL
- **Frontend:** TypeScript (компилируется в `build/*.js`), статический HTML/CSS, без фреймворков
- **Прочее:** MapStruct (мапперы DTO/сущность), Lombok, Jakarta Bean Validation, JUnit 5
- **Сборка/деплой:** Maven (`war`-пакет), запуск на сервлет-контейнере (Tomcat и т.п.)

## Архитектура

Слоистая структура на классическом Spring MVC (`web.xml`-less, через `SpringDispatcherServletInitializer`):

- `controller` — REST-контроллеры (`/api/...`)
- `application` — application-сервисы, оркестрирующие сценарии use-case
- `domain/model`, `domain/service` — доменная модель матча (сет, гейм, тай-брейк, очки) и правила подсчёта счёта
- `service` — сервисы поиска/сохранения завершённых матчей
- `dao` / `common/dao` — доступ к данным (Hibernate) поверх обобщённых DAO-интерфейсов
- `entity` — JPA-сущности (`Match`, `Player`)
- `dto`, `mapper` — контракты API и MapStruct-мапперы entity ↔ DTO
- `handler`, `exception` — глобальная обработка ошибок и доменные исключения
- `config` — конфигурация Spring, DataSource/Hibernate (`DatabaseConfig`)

Фронтенд — статичные страницы (`index.html`, `new-match.html`, `match-score.html`, `finished-match.html`), логика на TypeScript в `src/main/webapp/src`, компилируется в `src/main/webapp/build`.

## API

| Метод | Путь | Назначение |
|---|---|---|
| `POST` | `/api/matches` | Создать новый матч между двумя игроками |
| `GET` | `/api/matches/{uuid}` | Получить текущий счёт матча |
| `POST` | `/api/matches/{uuid}/point` | Начислить очко игроку и вернуть обновлённый счёт |
| `GET` | `/api/matches?page=&player_name=` | Список завершённых матчей (с пагинацией и фильтром по игроку) |

## Требования

- JDK 25
- Maven 3.9+
- PostgreSQL (локально доступный экземпляр)
- Node.js (для сборки TypeScript-фронтенда), опционально — если правите `src/main/webapp/src`

## Настройка базы данных

Параметры подключения — в [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
db.url=jdbc:postgresql://localhost:5432/postgres
db.username=postgres
db.password=12345
```

Схема создаётся автоматически при старте (`hibernate.hbm2ddl.auto=create-drop`), тестовые данные — из [`import.sql`](src/main/resources/import.sql). Перед запуском поднимите PostgreSQL и при необходимости скорректируйте креды под свою среду.

## Сборка и запуск

Backend (WAR-пакет):

```bash
mvn clean package
```

Полученный `target/TennisScoreBoard-1.0-SNAPSHOT.war` разверните на сервлет-контейнере (например, Tomcat) либо запустите проект из IDE (IntelliJ IDEA) через конфигурацию сервера приложений.

Frontend (пересборка TypeScript в `src/main/webapp/build`, если менялись `.ts`-файлы):

```bash
cd src/main/webapp
npm install
npm run build
```

## Тесты

```bash
mvn test
```

Юнит-тесты покрывают доменную логику подсчёта очков/геймов/сетов — [`TennisMatchTest`](src/test/java/org/example/tennisscoreboard/domain/model/TennisMatchTest.java).

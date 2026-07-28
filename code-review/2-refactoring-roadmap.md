# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Entity и слой доступа к данным

- `/entity/Player.java`
- `/entity/Match.java`
- `/common/dao/DAO.java`
- `/common/dao/ExtendedDAO.java`
- `/common/dao/BaseDAO.java`
- `/dao/player/PostgresPlayerDAO.java`
- `/dao/match/PostgresFinishedMatchDAO.java`
- `/handler/ErrorMapper.java`

### Шаг 2: Доменные модели

- `/domain/model/Participant.java`
- `/domain/model/Participants.java`
- `/domain/model/Point.java`
- `/domain/model/Tiebreak.java`
- `/domain/model/Game.java`
- `/domain/model/Set.java`
- `/domain/model/TennisMatch.java`
- `/domain/model/Score.java`

### Шаг 3: Сервисный слой

- `/domain/service/OngoingMatchesDomainService.java`
- `/application/PlayerApplicationService.java`
- `/application/MatchApplicationService.java`
- `/service/FinishedMatchesPersistenceService.java`
- `/service/FinishedMatchesFindingService.java`

### Шаг 4: DTO (Data Transfer Object)

- `/dto/MatchResponse.java`
- `/dto/PointAwardingRequest.java`
- `/dto/TennisMatchResponse.java`

### Шаг 5: Контроллеры

- `/controller/OngoingMatchController.java`
- `/controller/FinishedMatchController.java`
- `/controller/MatchScoreController.java`

### Шаг 6: Конфигурация, мапперы, валидаторы, обработка исключений

- `/config/DatabaseConfig.java`
- `/config/SpringConfig.java`
- `/mapper/TennisMatchMapper.java`
- `/mapper/MatchMapper.java`
- `/util/ValidationUtil.java`
- `/handler/TennisScoreboardExceptionHandler.java`
- `/exception/DatabaseException.java`

### Шаг 7: Тесты и HTML

- `src/test/java/org/example/tennisscoreboard/domain/model/TennisMatchTest.java`

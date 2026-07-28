package org.example.tennisscoreboard.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.model.TennisMatch;
import org.example.tennisscoreboard.exception.OngoingMatchNotFoundException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class OngoingMatchesDomainService {

    // Можно добавить классу аннотацию @Component

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Этот класс не относится к домену, а только выступает хранилищем текущих матчей (доменных моделей),
        // поэтому стоит перенести его в пакет service (или dao.inmemory).

    private final Map<UUID, TennisMatch> ongoingMatches = new ConcurrentHashMap<>();

    // Этот метод не должен получать ID из `TennisMatch`.
        // Хранилище должно само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
    // Можно просто add или save
    public void addNewMatch(TennisMatch tennisMatch) {
        ongoingMatches.put(tennisMatch.getUuid(), tennisMatch);
    }

    // Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
    // Можно просто get или find
    public TennisMatch getOngoingMatch(String uuid) {
        TennisMatch tennisMatch = ongoingMatches.get(UUID.fromString(uuid));
        if (tennisMatch == null) {

            // Текст сообщения в исключениях принято писать на английском языке.
            throw new OngoingMatchNotFoundException("Текущий матч не найден");
        }

        return tennisMatch;
    }

    // Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
    // Можно просто delete или remove
    public void deleteFinishedMatch(String uuid) {
        TennisMatch tennisMatch = getOngoingMatch(uuid);

        // Этот метод (и класс хранилища) не должны ничего знать о бизнес-логике
            // (что удаляются только завершённые матчи). Его задача — просто работать с хранилищем
            // и удалять матч, если он существует без дополнительных условий.
        if (tennisMatch.getWinner() != null) {
            ongoingMatches.remove(UUID.fromString(uuid));
        }
    }
}
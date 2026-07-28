package org.example.tennisscoreboard.application;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.model.TennisMatch;
import org.example.tennisscoreboard.domain.model.Participants;
import org.example.tennisscoreboard.domain.service.OngoingMatchesDomainService;
import org.example.tennisscoreboard.dto.*;
import org.example.tennisscoreboard.mapper.TennisMatchMapper;
import org.example.tennisscoreboard.service.FinishedMatchesFindingService;
import org.example.tennisscoreboard.service.FinishedMatchesPersistenceService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MatchApplicationService {

    // Не понятна роль слова Application в названии класса. Можно просто MatchService.

    // Класс стоит перенести в пакет service к другим сервисам.

    // TODO: Нет интерфейса для этого класса. (см. файл "application.md" в этом же пакете)

    // TODO: Класс нарушает Принцип единой ответственности и работает как текущими, так и с завершёнными матчами.
        // В проекте есть FinishedMatchesPersistenceService, поэтому работу с завершёнными матчами стоит оставить ему.

    private final PlayerApplicationService playerApplicationService;
    private final OngoingMatchesDomainService ongoingMatchesDomainService;
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService;
    private final FinishedMatchesFindingService finishedMatchesFindingService;

    private final TennisMatchMapper tennisMatchMapper;

    public RegisteredMatchResponse createNewMatch(MatchCreationRequest matchCreationRequest) {
        Participants participants = playerApplicationService.findParticipants(matchCreationRequest.firstPlayerName(), matchCreationRequest.secondPlayerName());
        TennisMatch tennisMatch = TennisMatch.createMatch(participants);

        ongoingMatchesDomainService.addNewMatch(tennisMatch);
        return new RegisteredMatchResponse(tennisMatch.getUuid());
    }

    // Можно назвать getOngoingMatchScore
    // Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
    public TennisMatchResponse getGeneralScore(String uuid) {
        TennisMatch tennisMatch = ongoingMatchesDomainService.getOngoingMatch(uuid);
        return tennisMatchMapper.toDTO(tennisMatch);
    }

    // Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
    // Можно назвать просто awardPoint
    // TODO: Race condition при обработке выигранного очка.
        // Если пользователь очень быстро нажмёт кнопку выигрыша очка, браузер отправит два POST-запроса почти одновременно.
        // Tomcat обработает эти два запроса в двух разных потоках, но так как оба потока будут работать с одним и тем же общим объектом `TennisMatch`,
        // будет возникать ситуация, когда счёт изменится только один раз.
        // Чтобы это исправить, нужно гарантировать, что только один поток может изменять состояние конкретного матча в один момент времени.
    public TennisMatchResponse awardPointAndGetGeneralScore(String uuid, PointAwardingRequest pointAwardingRequest) {

        TennisMatch tennisMatch = ongoingMatchesDomainService.getOngoingMatch(uuid);
        String winnerName = pointAwardingRequest.name();
        tennisMatch.awardPoint(winnerName);

        // TODO: Проверка на то, что матч завершён перед удалением должна происходить в этом сервисе.
            // Сейчас этот сервис удаляет матч из хранилища в памяти и сохраняет в БД после каждого очка.
            // Реального удаления и сохранения не происходит только потому, что другие сервисы
            // берут на себя лишнюю ответственность по проверке условий удаления/сохранения.
        ongoingMatchesDomainService.deleteFinishedMatch(uuid);
        finishedMatchesPersistenceService.addFinishedMatch(tennisMatch);

        return tennisMatchMapper.toDTO(tennisMatch);
    }

    // Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере),
        // а в метод сервиса принимать уже int.
    public FinishedMatchesResponse getFinishedMatches(String pageFromUser, String playerName) {
        if (playerName == null) {
            return finishedMatchesFindingService.findFinishedMatches(pageFromUser);
        } else {
            return finishedMatchesFindingService.findFinishedMatchesByPlayer(playerName, pageFromUser);
        }
    }
}

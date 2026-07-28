package org.example.tennisscoreboard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.dto.MatchCreationRequest;
import org.example.tennisscoreboard.dto.RegisteredMatchResponse;
import org.example.tennisscoreboard.application.MatchApplicationService;
import org.example.tennisscoreboard.dto.TennisMatchResponse;
import org.example.tennisscoreboard.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class OngoingMatchController {
    private final MatchApplicationService matchApplicationService;

    // В проекте есть TennisScoreboardExceptionHandler, поэтому стоит убрать BindingResult из аргументов и обрабатывать ошибки валидации в хендлере
    @PostMapping("/matches")
    public ResponseEntity<RegisteredMatchResponse> createNewMatch(@RequestBody @Valid MatchCreationRequest matchCreationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(ValidationUtil.getErrorMessage(bindingResult));
        }
        RegisteredMatchResponse registeredMatchResponse = matchApplicationService.createNewMatch(matchCreationRequest);

        return new ResponseEntity<>(registeredMatchResponse, HttpStatus.CREATED);
    }

    // Можно назвать getMatchScore
    @GetMapping("/matches/{uuid}")
    public ResponseEntity<TennisMatchResponse> getGeneralScore(@PathVariable(value = "uuid") String uuid) {

        // В @PathVariable(value = "uuid") по умолчанию значение required = true, поэтому ручная проверка не нужна
        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("UUID не может быть пустым");
        }
        TennisMatchResponse tennisMatchResponse = matchApplicationService.getGeneralScore(uuid);

        return new ResponseEntity<>(tennisMatchResponse, HttpStatus.OK);
    }
}

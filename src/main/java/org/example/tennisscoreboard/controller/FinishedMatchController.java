package org.example.tennisscoreboard.controller;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.application.MatchApplicationService;
import org.example.tennisscoreboard.dto.FinishedMatchesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class FinishedMatchController {
    private final MatchApplicationService matchApplicationService;

    // String pageFromUser стоит сделать int
    // Можно использовать Spring Data JPA (spring-data-jpa). Это позволит использовать удобный интерфейс Pageable.
    @GetMapping("/matches")
    public ResponseEntity<FinishedMatchesResponse> getFinishedMatches(@RequestParam(required = false, value = "page") String pageFromUser, @RequestParam(required = false, value = "player_name") String playerName) {
        FinishedMatchesResponse finishedMatchesResponse = matchApplicationService.getFinishedMatches(pageFromUser, playerName);
        return new ResponseEntity<>(finishedMatchesResponse, HttpStatus.OK);
    }
}

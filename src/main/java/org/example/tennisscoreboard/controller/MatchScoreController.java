package org.example.tennisscoreboard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.service.applicationservice.MatchApplicationService;
import org.example.tennisscoreboard.dto.PointAwardingRequest;
import org.example.tennisscoreboard.dto.TennisMatchResponse;
import org.example.tennisscoreboard.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MatchScoreController {
    private final MatchApplicationService matchApplicationService;

    @PostMapping("/matches/{uuid}/point")
    public ResponseEntity<TennisMatchResponse> awardPointAndGetGeneralScore(@PathVariable("uuid") String uuid, @RequestBody @Valid PointAwardingRequest pointAwardingRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(ValidationUtil.getErrorMessage(bindingResult));
        }
        TennisMatchResponse tennisMatchResponse = matchApplicationService.awardPointAndGetGeneralScore(uuid, pointAwardingRequest);
        return new ResponseEntity<>(tennisMatchResponse, HttpStatus.OK);
    }
}

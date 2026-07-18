package org.example.tennisscoreboard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.application.MatchApplicationService;
import org.example.tennisscoreboard.dto.PointAwardingRequest;
import org.example.tennisscoreboard.dto.TennisMatchResponse;
import org.example.tennisscoreboard.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
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

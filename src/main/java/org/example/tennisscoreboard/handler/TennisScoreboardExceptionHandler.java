package org.example.tennisscoreboard.handler;

import org.example.tennisscoreboard.dto.ExceptionResponse;
import org.example.tennisscoreboard.exception.InvalidPageException;
import org.example.tennisscoreboard.exception.OngoingMatchNotFoundException;
import org.example.tennisscoreboard.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class TennisScoreboardExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPageException.class)
    public ExceptionResponse handleInvalidPageException(InvalidPageException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PlayerNotFoundException.class)
    public ExceptionResponse handlePlayerNotFoundException(PlayerNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OngoingMatchNotFoundException.class)
    public ExceptionResponse handleOngoingMatchNotFoundException(OngoingMatchNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleUnexpected(Exception e) {
        return new ExceptionResponse("Internal server error"); // Можно использовать HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
    }
}

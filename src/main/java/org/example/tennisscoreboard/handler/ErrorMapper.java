package org.example.tennisscoreboard.handler;

import jakarta.persistence.NoResultException;
import org.example.tennisscoreboard.exception.DatabaseException;
import org.example.tennisscoreboard.exception.PlayerNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper {

    public void mapPostgresPlayerDAOInsertError(Exception e) {
        if (e instanceof org.hibernate.exception.ConstraintViolationException) {
        } else if (e instanceof ConstraintViolationException) {
            throw new IllegalArgumentException(((ConstraintViolationException) e).getConstraintViolations().stream().findFirst().get().getMessage());
        }else {
            //здесь можно добавить метод mapCommonErrors
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    public void mapPostgresPlayerDAOFindError(Exception e) {
        if (e instanceof NoResultException) {
            throw new PlayerNotFoundException("Игрок не найден");
        }
    }

}

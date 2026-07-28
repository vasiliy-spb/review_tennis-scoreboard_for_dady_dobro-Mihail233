package org.example.tennisscoreboard.exception;

public class DatabaseException extends RuntimeException {

    // TODO: Оригинальное исключение не передаётся в конструктор супер-класса и молча "проглатывается".
        // Это скрывает причину происшествия и затрудняет отладку.

    public DatabaseException(String message) {
        super(message);
    }
}

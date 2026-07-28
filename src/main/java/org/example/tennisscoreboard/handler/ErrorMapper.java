package org.example.tennisscoreboard.handler;

import jakarta.persistence.NoResultException;
import org.example.tennisscoreboard.exception.DatabaseException;
import org.example.tennisscoreboard.exception.PlayerNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper {

    // Более уместным было бы расположить этот класс в пакете dao.
        // Так как он не относится к глобальным хэндлерам и преобразует исключения для двух конкретных методов.

    // Ответственность этого класса слишком простая и имеет очень узкую направленность тесно связанную с логикой слоя DAO.
        // Поэтому избыточно выносить её в отдельный класс.

    // TODO: Все методы "проглатывают" исходное исключение.
        // Оригинальное исключение (причину) лучше передавать в конструктор исключения-обёртки,
        // чтобы было возможным установить причину происшествия.

    public void mapPostgresPlayerDAOInsertError(Exception e) {
        if (e instanceof org.hibernate.exception.ConstraintViolationException) {
            // Пустой блок if не имеет смысла
        } else if (e instanceof ConstraintViolationException) {

            // Длинная цепочка вызовов ухудшает читаемость кода. В таких случаях стоит вводить переменные с понятными именами.
            // Оборачивать любое ConstraintViolationException в IllegalArgumentException не всегда корректно.
                // Стоит разработать кастомное исключение внутри приложения и использовать его.
                // А ещё ConstraintViolationException не всегда означает нарушение уникальности имени игрока.
            throw new IllegalArgumentException(((ConstraintViolationException) e).getConstraintViolations().stream().findFirst().get().getMessage());
        }else {
            // Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
            //здесь можно добавить метод mapCommonErrors

            // Текст сообщения в исключениях принято писать на английском языке.
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    // В методе mapPostgresPlayerDAOInsertError по умолчанию бросается DatabaseException, а в этом методе — нет.
    public void mapPostgresPlayerDAOFindError(Exception e) {
        if (e instanceof NoResultException) {

            // Текст сообщения в исключениях принято писать на английском языке.
            throw new PlayerNotFoundException("Игрок не найден");
        }
    }

}

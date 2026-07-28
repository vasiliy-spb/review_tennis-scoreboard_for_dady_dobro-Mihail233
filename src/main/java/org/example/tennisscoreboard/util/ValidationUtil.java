package org.example.tennisscoreboard.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

public class ValidationUtil {

    // Класс спроектирован как утилитный, но при этом не объявлен как final и имеет публичный конструктор.
        // Можно использовать @UtilityClass из Lombok.

    public static String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .findFirst() // Вместо возврата только первого сообщения можно формировать строку из всех сообщений.
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Something went wrong"); // Сообщение можно вынести в константу и дать ей понятное имя
    }
}

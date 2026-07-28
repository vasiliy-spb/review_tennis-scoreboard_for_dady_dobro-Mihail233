package org.example.tennisscoreboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PointAwardingRequest(

        @NotBlank(message = "Имя первого игрока не может быть пустым") // Слово "первого" лишнее в сообщении
        @Size(min = 5, max = 20, message = "Имя игрока должно быть от 5 до 20 символов")
        String name
) {
}

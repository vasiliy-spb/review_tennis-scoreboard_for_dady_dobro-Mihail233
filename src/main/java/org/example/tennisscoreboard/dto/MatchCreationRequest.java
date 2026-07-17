package org.example.tennisscoreboard.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MatchCreationRequest(

        @NotBlank(message = "Имя первого игрока не может быть пустым")
        @Size(min = 5, max = 20, message = "Имя игрока должно быть от 5 до 20 символов")
        String firstPlayerName,


        @NotBlank(message = "Имя второго игрока не может быть пустым")
        @Size(min = 5, max = 20, message = "Имя игрока должно быть от 5 до 20 символов")
        String secondPlayerName
) {
}

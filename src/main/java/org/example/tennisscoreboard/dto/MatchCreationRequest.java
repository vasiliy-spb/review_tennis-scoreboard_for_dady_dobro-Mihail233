package org.example.tennisscoreboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MatchCreationRequest(

        @NotBlank(message = "First player name cannot be empty")
        @Size(max = 20, message = "First player name is too long")
        @Size(min = 5, message = "First player name is too small" )
        String firstPlayerName,


        @NotBlank(message = "Second player name cannot be empty")
        @Size(max = 20, message = "Second player name is too long")
        @Size(min = 5, message = "Second player name is too small" )
        String secondPlayerName
) {
}

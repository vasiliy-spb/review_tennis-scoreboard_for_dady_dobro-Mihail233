package org.example.tennisscoreboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PointAwardingRequest(

        @NotBlank(message = "First player name cannot be empty")
        @Size(max = 20, message = "Player name is too long")
        @Size(min = 5, message = "Player name is too small" )
        String name
) {
}

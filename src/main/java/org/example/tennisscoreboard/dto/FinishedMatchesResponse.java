package org.example.tennisscoreboard.dto;

import java.util.List;

public record FinishedMatchesResponse(List<MatchResponse> matches, int currentPage, int totalPages) {
}

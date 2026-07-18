export interface RegisteredMatchResponse {
    id: string;
}

export interface ExceptionResponse {
    message: string;
}

export interface TennisMatchResponse {
    firstPlayerName: string,
    secondPlayerName: string,
    firstPlayerPoints: string,
    secondPlayerPoints: string,
    firstPlayerGames: number,
    secondPlayerGames: number,
    firstPlayerSets: number,
    secondPlayerSets: number,
    firstPlayerTieBreakPoints: number,
    secondPlayerTieBreakPoints: number,
    winnerName: string
}

export interface FinishedMatchesResponseDTO {
    lastPage: boolean;
    pageNumber: string;
    matches: MatchDTO[];
}

export interface MatchDTO {
    firstPlayerName: string,
    secondPlayerName: string,
    winnerName: string;
}

export interface MatchViewModel {
    firstPlayerName: string,
    secondPlayerName: string,
    firstPlayerPoints: string | number,
    secondPlayerPoints: string | number,
    firstPlayerGames: number,
    secondPlayerGames: number,
    firstPlayerSets: number,
    secondPlayerSets: number,
    winnerName: string
}
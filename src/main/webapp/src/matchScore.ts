import * as types from "./types";
import { TennisMatchResponse } from "./types";
import * as httpStatusCodes from "./httpStatusCodes.js";

if (typeof window !== "undefined") {

        const startMatch = async () => {
                let uuid = getParameterFromURL('uuid');
                let result: Response = await sendRequestToGetGeneralScore(uuid);

                const status: number = result.status;
                const successStatus = Object.values(httpStatusCodes.SuccessStatus);
                const container = document.getElementById('container') as HTMLElement;

                if (successStatus.includes(status)) {

                        const tennisMatchResponse: types.TennisMatchResponse = await result.json();

                        const firstPlayerName: string = tennisMatchResponse.firstPlayerName;
                        const secondPlayerName: string = tennisMatchResponse.secondPlayerName;

                        container.innerHTML = renderInitialScore(tennisMatchResponse);
                        setEventListenersOnButtons(uuid, firstPlayerName, secondPlayerName);
                } else {
                        const exceptionResponse: types.ExceptionResponse = await result.json();
                        container.innerHTML = renderInitialError(exceptionResponse.message);
                }

        }

        const getParameterFromURL = (parameter: string): string => {
                let parameters = new URLSearchParams(document.location.search);
                return parameters.get(parameter) as string;
        }

        const sendRequestToGetGeneralScore = async (uuid: string): Promise<Response> => {
                return fetch(`api/matches/${uuid}`, {
                        method: 'GET'
                });
        }

        const renderInitialScore = (tennisMatchResponse: TennisMatchResponse) => {
                const matchViewModel: types.MatchViewModel = toMatchViewModel(tennisMatchResponse);

                return ` <div class="currentMatchHeader centered">
                                <h1>CURRENT MATCH</h1>
                        </div>
                        <div class="centered">
                                <section class="score toxicText">
                                <table class="table table-text">
                                        <thead>
                                                <th class="player">PLAYER</th>
                                                <th>Sets</th>
                                                <th>Games</th>
                                                <th>Points</th>
                                        </thead>
                                        <tbody>
                                        <tr>
                                                <td id="firstPlayerName" class="player">${matchViewModel.firstPlayerName}</td>
                                                <td id="firstPlayerSets">${matchViewModel.firstPlayerSets}</td>
                                                <td id="firstPlayerGames">${matchViewModel.firstPlayerGames}</td>
                                                <td id="firstPlayerPoints">${matchViewModel.firstPlayerPoints}</td>
                                                <td class = "table-text">
                                                <div class="scoreButton toxicText" id="firstPlayerScoreButton">Score</div>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td id="secondPlayerName" class="player">${matchViewModel.secondPlayerName}</td>
                                                <td id="secondPlayerSets">${matchViewModel.secondPlayerSets}</td>
                                                <td id="secondPlayerGames">${matchViewModel.secondPlayerGames}</td>
                                                <td id="secondPlayerPoints">${matchViewModel.firstPlayerPoints}</td>
                                                <td class = "table-text">
                                                <div class="scoreButton toxicText" id="secondPlayerScoreButton">Score</div>
                                                </td>
                                        </tr>
                                        </tbody>
                                </table>
                                <div class ="centered errorText" id ="error">
                                </div>
                                </section>
                        </div>
                        <div class="centered" id="winner">
                        </div>
                        `
        }

        const toMatchViewModel = (
                tennisMatchResponse: TennisMatchResponse
            ): types.MatchViewModel => {
                return {
                    firstPlayerName: tennisMatchResponse.firstPlayerName,
                    secondPlayerName: tennisMatchResponse.secondPlayerName,
            
                    firstPlayerPoints: tennisMatchResponse.firstPlayerTieBreakPoints == null ? tennisMatchResponse.firstPlayerPoints : tennisMatchResponse.firstPlayerTieBreakPoints,
                    secondPlayerPoints: tennisMatchResponse.secondPlayerTieBreakPoints == null ? tennisMatchResponse.secondPlayerPoints : tennisMatchResponse.secondPlayerTieBreakPoints,
            
                    firstPlayerGames: tennisMatchResponse.firstPlayerGames,
                    secondPlayerGames: tennisMatchResponse.secondPlayerGames,
            
                    firstPlayerSets: tennisMatchResponse.firstPlayerSets,
                    secondPlayerSets: tennisMatchResponse.secondPlayerSets,
            
                    winnerName: tennisMatchResponse.winnerName
                };
            };


        const setEventListenersOnButtons = (uuid: string, firstPlayerName: string, secondPlayerName: string) => {
                const firstPlayerScoreButton = document.getElementById('firstPlayerScoreButton') as HTMLElement;
                const secondPlayerScoreButton = document.getElementById('secondPlayerScoreButton') as HTMLElement;

                const firstPlayerAwardPoint = awardPoint(uuid, firstPlayerName);
                const secondPlayerAwartPoint = awardPoint(uuid, secondPlayerName);

                firstPlayerScoreButton.addEventListener("click", firstPlayerAwardPoint);
                secondPlayerScoreButton.addEventListener("click", secondPlayerAwartPoint);
        }

        const awardPoint = (uuid: string, winnerName: string) => {
                return async function () {
                        const error = document.getElementById('error') as HTMLElement;
                        try {
                                const tennisMatchResponse = await getUpdatedGeneralScore(uuid, winnerName);
                                error.innerHTML = ``;
                                renderGeneralScore(tennisMatchResponse);
                                renderWinner(tennisMatchResponse);
                        } catch (e) {
                                renderError(e as any, error);
                        }
                }
        }

        const getUpdatedGeneralScore = async (uuid: string, winnerName: string): Promise<TennisMatchResponse> => {
                const result = await sendRequestToGetUpdatedGeneralScore(uuid, winnerName);

                const status: number = result.status;
                const successStatus = Object.values(httpStatusCodes.SuccessStatus);

                if (successStatus.includes(status)) {
                        return await result.json();
                } else {
                        const exceptionResponse: types.ExceptionResponse = await result.json();
                        throw Error(exceptionResponse.message);
                }
        }

        const sendRequestToGetUpdatedGeneralScore = async (uuid: string, winnerName: string): Promise<Response> => {
                return fetch(`api/matches/${uuid}/point`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                                name: winnerName
                        })
                });
        }

        const renderGeneralScore = (tennisMatchResponse: TennisMatchResponse) => {
                const matchViewModel: types.MatchViewModel = toMatchViewModel(tennisMatchResponse);

                renderGeneralScoreUnit(matchViewModel.firstPlayerPoints, "firstPlayerPoints");
                renderGeneralScoreUnit(matchViewModel.firstPlayerGames, "firstPlayerGames");
                renderGeneralScoreUnit(matchViewModel.firstPlayerSets, "firstPlayerSets");

                renderGeneralScoreUnit(matchViewModel.secondPlayerPoints, "secondPlayerPoints");
                renderGeneralScoreUnit(matchViewModel.secondPlayerGames, "secondPlayerGames");
                renderGeneralScoreUnit(matchViewModel.secondPlayerSets, "secondPlayerSets");

        }

        const renderGeneralScoreUnit = (number: string | number, elementName: string) => {
                const element = document.getElementById(elementName) as HTMLElement;
                element.innerText = number + "";
        }

        const renderWinner = (tennisMatchResponse: TennisMatchResponse) => {
                const winnerName: string = tennisMatchResponse.winnerName;

                if (winnerName != null) {
                        const winnerElement = document.getElementById("winner") as HTMLElement;
                        winnerElement.innerHTML = `<p>WINNER: ${winnerName} </p>`
                }
        }

        const renderError = (e: any, error: HTMLElement) => {
                error.innerHTML = `<p>${e.message}</p>`;
        }


        const renderInitialError = (message: string) => {
                return `
                        <div class="currentMatchHeader centered errorText">
                                <h1>${message}</h1>
                        </div>`
        }

        document.addEventListener("DOMContentLoaded", startMatch);
}
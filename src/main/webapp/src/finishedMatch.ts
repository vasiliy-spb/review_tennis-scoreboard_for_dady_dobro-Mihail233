import * as dto from "./types";
import { FinishedMatchesResponse } from "./types";
import * as httpStatusCodes from "./httpStatusCodes.js";

const searchForm = document.getElementById("searchForm") as HTMLFormElement;
const searchString = document.getElementById("searchString") as HTMLInputElement;
const searchButton = document.getElementById("searchButton") as HTMLElement;
const searchClear = document.getElementById("searchClear") as HTMLElement;

const searchResult = document.getElementById('searchResult') as HTMLElement;
const pagination = document.getElementById("pagination") as HTMLElement;

const startedPage = 1;

const makeOriginalSearch = async () => {
    const result = await sendRequestToGetFinishedMatches(startedPage);
    await renderModelViewFinishedMatches(result);
}

const sendRequestToGetFinishedMatches = async (pageNumber: number): Promise<Response> => {
    return fetch(`api/matches?page=${pageNumber}`, {
        method: 'GET'
    });
}

const renderModelViewFinishedMatches = async (result: Response) => {

    if (httpStatusCodes.isSuccessfulRequest(result)) {
        const finishedMatchesResponse: FinishedMatchesResponse = await result.json();

        const recordsNotFound = 0;
        let matchCount = finishedMatchesResponse.matches.length;
        pagination.innerHTML = "";
        
        if (matchCount == recordsNotFound) {
            renderMatchesNotFound();
        } else {
            renderTable(finishedMatchesResponse);
            renderPagination(finishedMatchesResponse);
            setEventListenerOnButtons(finishedMatchesResponse)
            //ивент листенер
        }
    } else {
        const exceptionResponse: dto.ExceptionResponse = await result.json();
        renderInitialError(exceptionResponse.message);
    }
}

const renderMatchesNotFound = () => {
    const matchNotFound = `<div class="centered toxicText">
                        <h1>Matches Not Found</h1>
                </div>`;
    searchResult.innerHTML = matchNotFound;
}

const renderTable = (finishedMatchesResponse: FinishedMatchesResponse) => {
    const bodyOfTable = getBodyOfTable(finishedMatchesResponse);
    const table = `
                <div id="matchTable">
                    <section class="toxicText">
                    <table class="table table-text">
                            <thead>
                                    <th>PLAYER1</th>
                                    <th>PLAYER2</th>
                                    <th>WINNER</th>
                                    ${bodyOfTable}
                            </thead>
                            <tbody>
                            </tbody>
                    </table>
                    </section>
                </div>
                `
    searchResult.innerHTML = table;
}

const getBodyOfTable = (finishedMatchesResponse: FinishedMatchesResponse): string => {
    let body = "";
    for (const MatchResponse of finishedMatchesResponse.matches) {
        const record =
            `<tr>
                        <td>${MatchResponse.firstPlayerName}</td>
                        <td>${MatchResponse.secondPlayerName}</td>
                        <td>${MatchResponse.winnerName}</td>
                     </tr>`

        body += record;
    }

    return body;
}

const renderPagination = async (finishedMatchesResponse: FinishedMatchesResponse) => {
    if (isPaginationRequired(finishedMatchesResponse)) {

        const prevButton = createButton("prev", "prevButton");
        const nextButton = createButton("next", "nextButton");
        setButtonVisibility(prevButton, nextButton, finishedMatchesResponse);

        const paginationHTML = `
        <div class="centered toxicText">
                ${prevButton.outerHTML}
                <h2 id="pageNumber">${finishedMatchesResponse.currentPage}</h2>
                ${nextButton.outerHTML}
        </div>`

        pagination.innerHTML = paginationHTML;
    }
}

const isPaginationRequired = (finishedMatchesResponse: FinishedMatchesResponse) => {
    return !(finishedMatchesResponse.totalPages === startedPage);
}

const createButton = (text: string, id: string): HTMLElement => {
    const button = document.createElement('button');
    button.className = "toxicText";
    button.setAttribute("id", id);
    button.textContent = text;
    return button;
}

const setButtonVisibility = (prevButton: HTMLElement, nextButton: HTMLElement, finishedMatchesResponse: FinishedMatchesResponse) => {
    if (finishedMatchesResponse.currentPage == startedPage) {
        prevButton.style.visibility = "hidden";
    }

    if (finishedMatchesResponse.currentPage != startedPage && finishedMatchesResponse.currentPage == finishedMatchesResponse.totalPages) {
        nextButton.style.visibility = "hidden";
    }
}

const setEventListenerOnButtons = (finishedMatchesResponse: FinishedMatchesResponse) => {
    let playerName = getPlayerNameFromForm();

    const nextButton = document.getElementById("nextButton") as HTMLElement;
    const prevButton = document.getElementById("prevButton") as HTMLElement;

    const nextPageNumber = finishedMatchesResponse.currentPage + 1;
    const findNextPages = findMatchesByPlayer(nextPageNumber, playerName.toString());

    const prevPageNumber = finishedMatchesResponse.currentPage - 1;
    const findPrevPages = findMatchesByPlayer(prevPageNumber, playerName.toString());

    nextButton.addEventListener("click", findNextPages);
    prevButton.addEventListener("click", findPrevPages);
}

const getPlayerNameFromForm = () => {
    const formData = new FormData(searchForm);
    return formData.get("playerName") as FormDataEntryValue;
}

const findMatchesByPlayer = (pageNumber: number, playerName: string) => {
    return async function () {
        if (playerName.length == 0) {
            const result = await sendRequestToGetFinishedMatches(pageNumber);
            await renderModelViewFinishedMatches(result);
        } else {
            const result = await sendRequestToGetMatchesByPlayer(pageNumber, playerName);
            await renderModelViewFinishedMatches(result);
        }
    }
}

const renderInitialError = (message: string) => {
    const initialError = `
                <div class="centered errorText">
                        <h1>${message}</h1>
                </div>`
    searchResult.innerHTML = initialError;
}

const makeSearchByPlayer = async () => {
    const playerName = getPlayerNameFromForm();

    const result = await sendRequestToGetMatchesByPlayer(startedPage, playerName?.toString());
    await renderModelViewFinishedMatches(result);
}

const sendRequestToGetMatchesByPlayer = (pageNumber: number, playerName: string) => {
    return fetch(`/api/matches?page=${pageNumber}&player_name=${playerName}`, {
        method: 'GET'
    });
}


searchForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await makeSearchByPlayer();
});


searchClear.addEventListener("click", async () => {
    searchString.value = "";
    await makeOriginalSearch();
});

searchButton.addEventListener("click", makeSearchByPlayer);

document.addEventListener("DOMContentLoaded", makeOriginalSearch);


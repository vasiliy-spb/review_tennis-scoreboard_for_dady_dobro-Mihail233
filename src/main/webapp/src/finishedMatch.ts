import * as dto from "./types";
import { FinishedMatchesResponseDTO } from "./types";
import * as httpStatusCodes from "./httpStatusCodes";

if (typeof window !== "undefined") {
    const searchForm = document.getElementById("searchForm") as HTMLFormElement;
    const searchButton = document.getElementById("searchButton") as HTMLElement;
    const searchString = document.getElementById("searchString") as HTMLInputElement;
    const searchClear = document.getElementById("searchClear") as HTMLElement;
    const defaultPage = "1";
    const RECORDS_NOT_FOUND = 0;

    const makeOriginalSearch = async () => {
        const result = await sendRequestToGetFinishedMatches(defaultPage);
        await setTableWithFinishedMatches(result);
    }

    const sendRequestToGetFinishedMatches = async (pageNumber: string): Promise<Response> => {
        return fetch(`matches?page=${pageNumber}`, {
            method: 'GET'
        });
    }

    const setTableWithFinishedMatches = async (result: Response) => {

        const status: number = result.status;
        const successStatus = Object.values(httpStatusCodes.SuccessStatus);
        const searchResult = document.getElementById('searchResult') as HTMLElement;

        if (successStatus.includes(status)) {
            const finishedMatchesResponseDTO: FinishedMatchesResponseDTO = await result.json();
            let numberOfMathes = finishedMatchesResponseDTO.matches.length;

            if (numberOfMathes == RECORDS_NOT_FOUND) {
                searchResult.innerHTML = getMatchesNotFound();
            } else {
                let table = getTableFinishedMatches(finishedMatchesResponseDTO);

                if (finishedMatchesResponseDTO.pageNumber == defaultPage && finishedMatchesResponseDTO.lastPage) {
                    searchResult.innerHTML = table;
                } else {
                    let switcher = await getPagesSwitcher(finishedMatchesResponseDTO);
                    searchResult.innerHTML = table + switcher;
                    setEventListenerOnButtons(finishedMatchesResponseDTO.pageNumber);
                }
            }
            //ДОБАВИТЬ ОТРИСОВКУ КНОПКИ СНИЗУ(склеивание строк + inner)
        } else {
            const exceptionResponse: dto.ExceptionResponse = await result.json();
            searchResult.innerHTML = getDefaultError(exceptionResponse.message);
        }
    }

    const getMatchesNotFound = (): string => {
        return `<div class="centered toxicText">
                        <h1>Matches Not Found</h1>
                </div>`;
    }

    const getTableFinishedMatches = (finishedMatchesResponseDTO: FinishedMatchesResponseDTO): string => {
        const bodyOfTable = getBodyOfTable(finishedMatchesResponseDTO);
        return `
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
    }

    const getBodyOfTable = (finishedMatchesResponseDTO: FinishedMatchesResponseDTO): string => {
        let bodyOfTable = "";
        for (const MatchDTO of finishedMatchesResponseDTO.matches) {
            const record =
                `<tr>
                        <td>${MatchDTO.firstPlayerName}</td>
                        <td>${MatchDTO.secondPlayerName}</td>
                        <td>${MatchDTO.winnerName}</td>
                     </tr>`

            bodyOfTable += record;
        }

        return bodyOfTable;
    }

    const findRecords = (pageNumber: string, searchStringName: string) => {
        return async function () {
            console.log(searchStringName);
            if (searchStringName.length == 0) {
                const result = await sendRequestToGetFinishedMatches(pageNumber);
                await setTableWithFinishedMatches(result);
            } else {
                const result = await sendRequestToGetMatchesByPlayer(pageNumber, searchStringName);
                await setTableWithFinishedMatches(result);
            }
        }
    }

    const getPagesSwitcher = async (finishedMatchesResponseDTO: FinishedMatchesResponseDTO): Promise<string> => {
        const lastPage = finishedMatchesResponseDTO.lastPage;
        const pageNumber = finishedMatchesResponseDTO.pageNumber;

        const prevButton = createButton("prev", "prevButton");
        const nextButton = createButton("next", "nextButton") as HTMLElement;

        if (pageNumber == defaultPage) {
            prevButton.style.visibility = "hidden";
        }

        if (pageNumber != defaultPage && lastPage) {
            nextButton.style.visibility = "hidden";
        }

        return `
        <div class="centered toxicText">
                ${prevButton.outerHTML}
                <h2 id="pageNumber">${finishedMatchesResponseDTO.pageNumber}</h2>
                ${nextButton.outerHTML}
        </div>`
    }

    const createButton = (text: string, id: string): HTMLElement => {
        const button = document.createElement('button');
        button.className = "toxicText";
        button.setAttribute("id", id);
        button.textContent = text;
        return button;
    }

    const getDefaultError = (message: string) => {
        return `
                <div class="centered errorText">
                        <h1>${message}</h1>
                </div>`
    }

    const makeSearchByPlayer = async () => {
        //вынести в отдельную функцию
        const formData = new FormData(searchForm);
        let searchStringName = formData.get("searchStringName") as FormDataEntryValue;

        const result = await sendRequestToGetMatchesByPlayer(defaultPage, searchStringName?.toString());
        await setTableWithFinishedMatches(result);
    }

    const sendRequestToGetMatchesByPlayer = (pageNumber: string, searchStringName: string) => {
        return fetch(`matches?page=${pageNumber}&filter_by_player_name=${searchStringName}`, {
            method: 'GET'
        });
    }

    const setEventListenerOnButtons = (pageNumber: string) => {
        const formData = new FormData(searchForm);
        let searchStringName = formData.get("searchStringName") as FormDataEntryValue;


        const nextButton = document.getElementById("nextButton") as HTMLElement;
        const prevButton = document.getElementById("prevButton") as HTMLElement;

        const nextPageNumber = (parseInt(pageNumber) + 1).toString();
        const findNextPages = findRecords(nextPageNumber, searchStringName.toString());

        const prevPageNumber = (parseInt(pageNumber) - 1).toString();
        const findPrevPages = findRecords(prevPageNumber, searchStringName.toString());

        nextButton.addEventListener("click", findNextPages);
        prevButton.addEventListener("click", findPrevPages);
    }

    document.addEventListener("DOMContentLoaded", makeOriginalSearch);

    searchForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        await makeSearchByPlayer();
    });

    searchButton.addEventListener("click", makeSearchByPlayer);

    searchClear.addEventListener("click", async () => {
        searchString.value = "";
        await makeOriginalSearch();
    });
}


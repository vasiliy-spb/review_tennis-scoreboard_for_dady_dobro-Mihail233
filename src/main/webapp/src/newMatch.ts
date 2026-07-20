import * as types from "./types.js";
import * as httpStatusCodes from "./httpStatusCodes.js";


const registerMatch = async (e: any) => {
    e.preventDefault();

    const [firstPlayerName, secondPlayerName] = getPlayerNamesFromForm();
    const result: Response = await sendRequestToRegisterMatch(firstPlayerName, secondPlayerName);

    if (httpStatusCodes.isSuccessfulRequest(result)) {
        const registeredMatchResponse: types.RegisteredMatchResponse = await result.json();
        makeRedirect(registeredMatchResponse.id);
    } else {
        const exceptionResponse: types.ExceptionResponse = await result.json();
        renderError(exceptionResponse);
    }
}

const getPlayerNamesFromForm = (): string[] => {
    const formData = new FormData(newMatchRegistrationForm);

    let firstPlayerName = formData.get("firstPlayerName") as string;
    let secondPlayerName = formData.get("secondPlayerName") as string;

    return [firstPlayerName, secondPlayerName]
}

const sendRequestToRegisterMatch = async (firstPlayerName: string, secondPlayerName: string): Promise<Response> => {
    return fetch('api/matches', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            firstPlayerName,
            secondPlayerName
        })
    });
}

const makeRedirect = (id: String) => {
    //не нравится .html
    document.location.href = `../match-score.html?uuid=${id}`;
}

const renderError = (exceptionResponse: types.ExceptionResponse) => {
    const error = document.getElementById('error') as HTMLElement;
    error.innerHTML = `<p>${exceptionResponse.message}</p>`;
}

const newMatchRegistrationForm = document.getElementById('newMatchRegistrationForm') as HTMLFormElement;
newMatchRegistrationForm.addEventListener('submit', registerMatch);
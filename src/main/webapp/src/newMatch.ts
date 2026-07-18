import * as dto from "./types.js";
import * as httpStatusCodes from "./httpStatusCodes.js";

const newMatchRegistrationForm = document.getElementById('newMatchRegistrationForm') as HTMLFormElement;

const registerMatch = async (e: any) => {
    e.preventDefault();

    const [firstPlayerName, secondPlayerName] = getPlayerNamesFromForm();
    const result: Response = await sendRequestToRegisterMatch(firstPlayerName, secondPlayerName);

    if (isSuccessfulRequest(result)) {
        const registeredMatchResponse: dto.RegisteredMatchResponse = await result.json();
        makeRedirect(registeredMatchResponse.id);
    } else {
        const exceptionResponse: dto.ExceptionResponse = await result.json();
        printError(exceptionResponse);
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

const isSuccessfulRequest = (result: Response) => {
    const status: number = result.status;
    const successStatus = Object.values(httpStatusCodes.SuccessStatus);
    return successStatus.includes(status);
}

const makeRedirect = (id: String) => {
    //не нравится .html
    document.location.href = `../match-score.html?uuid=${id}`;
}

const printError = (exceptionResponse: dto.ExceptionResponse) => {
    const error = document.getElementById('error') as HTMLElement;
    error.innerHTML = `<p>${exceptionResponse.message}</p>`;
}

newMatchRegistrationForm.addEventListener('submit', registerMatch);
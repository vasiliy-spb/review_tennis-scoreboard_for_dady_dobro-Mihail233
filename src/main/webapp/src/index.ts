const redirectOnNewMatch = () => {
    document.location.href = `../new-match.html`;
}

const redirectOnFinishedMatches = () => {
    document.location.href = `../finished-match.html`;
}

const newMatchButton = document.getElementById('newMatchButton') as HTMLFormElement;
newMatchButton.addEventListener('click', redirectOnNewMatch);

const finishedMatchesButton = document.getElementById('finishedMatchesButton') as HTMLFormElement;
finishedMatchesButton.addEventListener('click', redirectOnFinishedMatches);
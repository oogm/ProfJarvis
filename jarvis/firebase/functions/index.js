const { dialogflow } = require('actions-on-google');
const functions = require('firebase-functions');

const app = dialogflow({ debug: true })


const questions = [{
    "question": "Wie viele Beine hat ein Hund?",
    "answer": "4",
    "category": "Biologie"
}, {
    "question": "Wie viele Knochen sind im menschlichen Körper?",
    "answer": "206",
    "category": "Biologie"
}, {
    "question": "Sind Hummer unsterblich?",
    "answer": "Nein",
    "category": "Biologie"
}, {
    "question": "Was wird außer Sonnenlicht und Kohlendioxid noch für Photosynthese benötigt?",
    "answer": "Wasser",
    "category": "Biologie"
}, {
    "question": "Welches ist das größte Land der Welt?",
    "answer": "Russland",
    "category": "Geografie"
}, {
    "question": "Welches Land hat die meisten Einwohner?",
    "answer": "China",
    "category": "Geografie"
}, {
    "question": "Wofür steht USA?",
    "answer": "United States of America",
    "category": "Geografie"
}, {
    "question": "In welchem Monat ist der längste Tag des Jahres?",
    "answer": "Juni",
    "category": "General Knowledge"
}, {
    "question": "Wie viele Wochen hat ein Jahr?",
    "answer": "52",
    "category": "General Knowledge"
}]

app.intent('Default Welcome Intent', conv => {
    conv.ask('Hi, how is it going?')
    conv.ask(`Here's a picture of a cat`)
})

app.intent('select_topic', conv => {
    const topic = conv.parameters.topic
    conv.data['topic'] = topic
    conv.ask(`OK, hier kommt deine ${topic} Frage:`)
    askFlashcard(conv)
})

app.intent('ask_flashcard', conv => {
    askFlashcard(conv)
})

app.intent('ask_flashcard_answer', conv => {
    const answer = conv.parameters.answer
    const flashCard = conv.contexts.get('ask_flashcard-followup').parameters.flashCard
    if (answer === flashCard.answer) {
        conv.ask('Das ist richtig. Gut gemacht!')
    } else {
        conv.ask('Leider nicht... Die richtige Antwort ist:')
        conv.ask(flashCard.answer)
    }
    conv.contexts.delete('ask_flashcard-followup')
})

app.intent('ask_flashcard_cancel', conv => {
    const flashCard = conv.contexts.get('ask_flashcard-followup').parameters.flashCard
    conv.ask('Das hättest du gewusst... Die richtige Antwort lautet:')
    conv.ask(flashCard.answer)
    conv.contexts.delete('ask_flashcard-followup')
})

app.intent('Default Fallback Intent', conv => {
    conv.ask(`I didn't understand. Can you tell me something else?`)
})

exports.jarvis = functions.region('europe-west1').https.onRequest(app);

function getNextFlashcard(topic) {
    const possibleQuestions = questions.filter(card => card.category === topic)
    return possibleQuestions[Math.floor(Math.random() * possibleQuestions.length)]
}

function askFlashcard(conv) {
    const flashCard = getNextFlashcard(conv.data['topic'])
    conv.contexts.set('ask_flashcard-followup', 5, { 'flashCard': flashCard })
    conv.ask(flashCard.question)
}
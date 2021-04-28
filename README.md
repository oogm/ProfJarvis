# Flashcards with voice - Project for the Google Munich Hackathon 2018

Train with your flashcard deck via voice while you are driving, cooking, etc. 

You can add flashcard decks and flashcards and train with your added flashcard decks at any time via the Google Assistant. Using Dialogflow integrations, you can also integrate your flashcard agent in Telegram, Skype, and more. You can also import flashcards from [Studysmarter](https://www.studysmarter.de/).   

Tools used: Dialogflow, Node.js, Firebase

## Getting Started

1. `cd` into jarvis\dialogflow
2. Create an agent with [Dialogflow](https://dialogflow.cloud.google.com/#/login)
3. Choose the option **Restore agent from zip** and upload the .zip file of the repository
4. Create a new [Firebase Cloud function](https://firebase.google.com/products/functions)
5. `cd` into jarvis\firebase\functions 
6. Deploy **index.js** and **package.json** to your new cloud function
7. Link the cloud function API URL to your agent in Dialogflow's webhooks section

To import flashcards from Studysmarter, you may add the following steps:
1. `cd` to StudySmarterImporter\src\main\java
2. Add Studysmarter API token and add your server URL in **Demo.java**
3. Run **Demo.java**

package de.Milad_Taromi.Managers;

import de.Milad_Taromi.screens.PlayScreen;

public class GameManager {

    private final PlayScreen playScreen;

    private int currentDialogueIndex = 0;



    private final TextManager[] act1Dialogues = {
        new TextManager("Johnny", "Was geht?!"),
        new TextManager("Milad", "Nicht viel. Wo sind wir hier?"),
        new TextManager("Johnny", "Das weiß ich selbst nicht genau...")
    };

    public GameManager(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    public void startAct1() {
        currentDialogueIndex = 0;
        showCurrentDialogue();
    }

    public void nextDialogue() {
        currentDialogueIndex++;

        if (currentDialogueIndex < act1Dialogues.length) {
            showCurrentDialogue();
        } else {
            endAct1Dialogue();
        }
    }

    private void showCurrentDialogue() {
        TextManager textManager = act1Dialogues[currentDialogueIndex];  ///Nimmt aktuellen Text, also act1Dialogues[0] usw.
        playScreen.setDialogue(textManager.getFormattedText());         ///setzt dann den Text formatiert in PlayScree  ein!
    }

    private void endAct1Dialogue() {                                    ///Was soll nach ende passieren? Neuer screen oder neuer Act?
        playScreen.setDialogue("");

        System.out.println("Dialog von Akt 1 beendet.");

        // Hier könnte beispielsweise Folgendes passieren:
        // playScreen.hideDialogueBox();
        // startAct2();
    }
}

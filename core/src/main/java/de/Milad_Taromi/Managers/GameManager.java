package de.Milad_Taromi.Managers;

import de.Milad_Taromi.screens.PlayScreen;

public class GameManager {

    private final PlayScreen playScreen;

    private int currentDialogueIndex = 0;
    private boolean waitingForChoice = false;

    private final TextManager[] act1Dialogues = {

        // Index 0
        new TextManager(
            "Johnny",
            "Was geht?!",
            1,
            DialoguePortrait.JOHNNY_HAPPY,
            PortraitSide.LEFT
        ),

        // Index 1
        new TextManager(
            "Milad",
            "Nicht viel. Wo sind wir hier?",
            2,
            DialoguePortrait.MILAD_NEUTRAL,
            PortraitSide.RIGHT
        ),

        // Index 2
        new TextManager(
            "Johnny",
            "Das weiß ich selbst nicht genau...",
            3,
            DialoguePortrait.JOHNNY_NEUTRAL,
            PortraitSide.LEFT
        ),

        // Index 3: Auswahl
        new TextManager(
            "Johnny",
            "Was sollen wir jetzt machen?",
            new DialogueChoice[] {
                new DialogueChoice(
                    "Nichts machen",
                    4
                ),
                new DialogueChoice(
                    "Joggen gehen",
                    6
                )
            },
            DialoguePortrait.JOHNNY_NEUTRAL,
            PortraitSide.LEFT
        ),

        // Index 4
        new TextManager(
            "Milad",
            "Lass uns einfach hierbleiben.",
            5,
            DialoguePortrait.MILAD_SAD,
            PortraitSide.RIGHT
        ),

        // Index 5
        new TextManager(
            "Johnny",
            "Das klingt ziemlich langweilig.",
            -1,
            DialoguePortrait.JOHNNY_ANGRY,
            PortraitSide.LEFT
        ),

        // Index 6
        new TextManager(
            "Milad",
            "Lass uns eine Runde joggen gehen!",
            7,
            DialoguePortrait.MILAD_HAPPY,
            PortraitSide.RIGHT
        ),

        // Index 7
        new TextManager(
            "Johnny",
            "Okay, aber nicht so schnell!",
            -1,
            DialoguePortrait.JOHNNY_HAPPY,
            PortraitSide.LEFT
        )
    };

    public GameManager(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    public void startAct1() {
        currentDialogueIndex = 0;
        waitingForChoice = false;

        showCurrentDialogue();
    }

    public void nextDialogue() {

        // Während einer Auswahl darf Leertaste nicht weiterschalten!!!
        if (waitingForChoice) {
            return;
        }

        currentDialogueIndex++;

        if (currentDialogueIndex < act1Dialogues.length) {
            showCurrentDialogue();
        } else {
            endAct1Dialogue();
        }
    }

    private void showCurrentDialogue() {
        TextManager textManager =
            act1Dialogues[currentDialogueIndex];

        playScreen.displayDialogue(textManager);

        if (textManager.hasChoices()) {
            waitingForChoice = true;

            playScreen.showChoices(
                textManager.getChoices()
            );
        } else {
            waitingForChoice = false;
            playScreen.hideChoices();
        }
    }

    public void selectChoice(DialogueChoice choice) {
        waitingForChoice = false;

        playScreen.hideChoices();

        currentDialogueIndex =
            choice.getNextDialogueIndex();

        if (currentDialogueIndex >= 0
            && currentDialogueIndex < act1Dialogues.length) {

            showCurrentDialogue();

        } else {
            endAct1Dialogue();
        }
    }

    private void endAct1Dialogue() {
        playScreen.setDialogue("--- ENDE --- ");
        playScreen.hideChoices();

        System.out.println("Dialog von Akt 1 beendet.");
    }
}


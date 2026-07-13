package de.Milad_Taromi.Managers;

import de.Milad_Taromi.Dialogue.DialogueChoice;
import de.Milad_Taromi.Dialogue.DialoguePortrait;
import de.Milad_Taromi.screens.PlayScreen;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameManager {

    private final PlayScreen playScreen;

    private String currentDialogueId;
    private boolean waitingForChoice = false;

    private final Map<String, TextManager> act1Dialogues = new LinkedHashMap<>();

    public GameManager(PlayScreen playScreen) {
        this.playScreen = playScreen;

        act1Dialogues.put("johnny_greeting", new TextManager(
            "Johnny",
            "Was geht?!",
            "milad_question",
            DialoguePortrait.PICTURE1
        ));

        act1Dialogues.put("milad_question", new TextManager(
            "Milad",
            "Nicht viel. Wo sind wir hier?",
            "johnny_unsure",
            DialoguePortrait.MILAD_NEUTRAL
        ));

        act1Dialogues.put("johnny_unsure", new TextManager(
            "Johnny",
            "Das weiß ich selbst nicht genau...",
            "johnny_choice",
            DialoguePortrait.JOHNNY_NEUTRAL
        ));

        // Auswahl
        act1Dialogues.put("johnny_choice", new TextManager(
            "Johnny",
            "Was sollen wir jetzt machen?",
            new DialogueChoice[] {
                new DialogueChoice("Nichts machen", "milad_stay"),
                new DialogueChoice("Joggen gehen", "milad_jog")
            },
            DialoguePortrait.JOHNNY_NEUTRAL
        ));

        act1Dialogues.put("milad_stay", new TextManager(
            "Milad",
            "Lass uns einfach hierbleiben.",
            "johnny_boring",
            DialoguePortrait.MILAD_SAD
        ));

        act1Dialogues.put("johnny_boring", new TextManager(
            "Johnny",
            "Das klingt ziemlich langweilig.",
            (String) null, // Ende
            DialoguePortrait.JOHNNY_ANGRY
        ));

        act1Dialogues.put("milad_jog", new TextManager(
            "Milad",
            "Lass uns eine Runde joggen gehen!",
            "johnny_okay",
            DialoguePortrait.MILAD_HAPPY
        ));

        act1Dialogues.put("johnny_okay", new TextManager(
            "Johnny",
            "Okay, aber nicht so schnell!",
            (String) null, // Ende
            DialoguePortrait.JOHNNY_HAPPY
        ));
    }

    public void startAct1() {
        currentDialogueId = "johnny_greeting";
        waitingForChoice = false;

        showCurrentDialogue();
    }

    public void nextDialogue() {

        // Während einer Auswahl darf Leertaste nicht weiterschalten!!!
        if (waitingForChoice) {
            return;
        }

        TextManager current = act1Dialogues.get(currentDialogueId);
        goToDialogue(current.getNextDialogueId());
    }

    private void showCurrentDialogue() {
        TextManager textManager = act1Dialogues.get(currentDialogueId);

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

        goToDialogue(choice.getNextDialogueId());
    }

    private void goToDialogue(String id) {
        if (id == null || !act1Dialogues.containsKey(id)) {
            endAct1Dialogue();
            return;
        }

        currentDialogueId = id;
        showCurrentDialogue();
    }

    private void endAct1Dialogue() {
        playScreen.setDialogue("--- ENDE --- ");
        playScreen.hideChoices();

        System.out.println("Dialog von Akt 1 beendet.");
    }
}

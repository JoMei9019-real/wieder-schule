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

    private final Map<String, TextManager> act1Dialogues = new LinkedHashMap<>();   /// Eine Map erlaubt es, einen Dialog direkt über seine ID zu finden
                                                                                    /// Ein LinkedHashMap speichert zusätzlich die Reihenfolge, in der die Elemente eingefügt wurden.
    public GameManager(PlayScreen playScreen) {
        this.playScreen = playScreen;                        /// Aufruf bzw Speichern von PlayScreen, um dort die rendering Methoden nutzen zu können!

        ///////// HIER IM KONSTRUKTOR WIRD DIE STORY IN DIE MAP GELADEN ///////

        act1Dialogues.put("johnny_greeting", new TextManager(       /// Mit put wird ein neuer Eintrag in die Map eingefügt mit id (hier: johnny_greeting)
            "Johnny",                                               /// TextManager wird zum rendern aufgerufen mit Infos über den Dialog wie Name, Inhalt etc.
            "Was geht?!",
            "milad_question",                           /// Wohin soll nach diesem Dialog gesprungen werden?
            DialoguePortrait.PICTURE1                               /// passendes Bild wird erzeugt!
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
                new DialogueChoice("Nichts machen", "milad_stay"),      /// Aufruf von einem Array mit Antwortmöglichkeiten und was dannach
                new DialogueChoice("Joggen gehen", "milad_jog")         /// ... passieren soll
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

    public void startAct1() {           /// Diese Methode startet den 1. AKT und wird bei der Ausführung vom PlayScreen ausgeführt als 1. Aufruf in dieser Klasse
        currentDialogueId = "johnny_greeting";  /// "Womit soll ich als erstes überhaupt anfangen?"
        waitingForChoice = false;               /// Direkt Choice am Anfang? NEIN, nicht in dem Fall

        showCurrentDialogue();              /// Anzeigen des Dialogs
    }

    public void nextDialogue() {            /// Diese Methode wird ausgeführt, sobald man SPACE drückt durch die PlayScreen Klasse

        // Während einer Auswahl darf Leertaste nicht weiterschalten!!!
        if (waitingForChoice) {
            return;
        }

        TextManager current = act1Dialogues.get(currentDialogueId);     /// Anschließend wird die ID des aktuellen Dialogs abgefragt:
        goToDialogue(current.getNextDialogueId());                      /// Anschließend wird zum nächsten Dialog geschaltet durch diese Methode!
    }

    private void showCurrentDialogue() {                            /// Laden des ausgewählten Dialogs aus der Map
        TextManager textManager = act1Dialogues.get(currentDialogueId); /// Dabei wird der TextManager.class benötigt, um korrekt zu rendern

        playScreen.displayDialogue(textManager);                    /// Setzt den formatierten Text in den Label im PlayScreen sodass dieser dann gerendert wird

        if (textManager.hasChoices()) {                             /// Auswahl? Ja / Nein?
            waitingForChoice = true;                                /// Wenn JA, dann...

            playScreen.showChoices(                                 /// ... Choices rendern im PlayScreen mit TextManager.class
                textManager.getChoices()
            );
        } else {
            waitingForChoice = false;                               /// sonst informieren, dass es nun kein Choice mehr gibt und anschließend,
            playScreen.hideChoices();                               /// die Choices wieder entfernen
        }
    }

    public void selectChoice(DialogueChoice choice) {               /// Diese Methode wird aufgerufen, wenn der Spieler einen Antwortbutton anklickt über den PlayScreen
        waitingForChoice = false;                                   /// Signal, dass die Choice-Abfrage nun vorbei ist

        playScreen.hideChoices();                                   /// Verstecken der Buttons

        goToDialogue(choice.getNextDialogueId());               /// Laden des nächsten Dialoges
    }

    private void goToDialogue(String id) {
        if (id == null || !act1Dialogues.containsKey(id)) { /// Es werden zwei Fälle überprüft: 1. ID == null, dann ist die Story vorbei oder 2. Die ID ist nicht vorhanden
            endAct1Dialogue();                              /// Act wird beendet und Methode mit return abgebrochen
            return;
        }

        currentDialogueId = id;                 /// Aktuelle ID in "id" gespeichert
        showCurrentDialogue();                  /// und der aktuelle Dialog anschließend angezeigt!
    }

    private void endAct1Dialogue() {            /// Aufgerufen, wenn Story vorbei ist oder es zu einem Fehler kommt (siehe oben)
        playScreen.setDialogue("--- ENDE --- ");    /// Manuelles überschreiben mit "ENDE" (ohne pre-rendering durch TextManager.class)
        playScreen.hideChoices();                      /// Alle entscheidungen werden versteckt

        System.out.println("Dialog von Akt 1 beendet.");    /// DEBUG bzw. Info für uns Entwickler
    }
}

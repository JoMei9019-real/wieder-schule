package de.Milad_Taromi.Managers;

import de.Milad_Taromi.Dialogue.DialogueChoice;
import de.Milad_Taromi.Dialogue.DialoguePortrait;

public class TextManager {          /// Speichert alle Informationen eines einzelnen Dialogs.

    private final String speaker;
    private final String text;

    private final String nextDialogueId;
    private final DialogueChoice[] choices; /// Speichert mehrere Antwortmöglichkeiten.

    private final DialoguePortrait portrait;

    public TextManager(
        String speaker,
        String text,
        String nextDialogueId,
        DialoguePortrait portrait
    ) {
        this.speaker = speaker == null ? "" : speaker;  /// Falls kein Sprecher angegeben wurde (null), wird stattdessen ein leerer String gespeichert.
        this.text = text == null ? "" : text;           /// Dies verhindert, dass das Programm bzw. die Engine abstürzt!

        this.nextDialogueId = nextDialogueId;
        this.choices = null;                        /// Da normale Dialoge keine Auswahl besitzen, wird choices auf null gesetzt.

        this.portrait = portrait;
    }

    public TextManager(         /// Zweiter Konstruktor (Dialog mit Auswahl)
        String speaker,
        String text,
        DialogueChoice[] choices,
        DialoguePortrait portrait
    ) {
        this.speaker = speaker == null ? "" : speaker;
        this.text = text == null ? "" : text;

        this.nextDialogueId = null; /// Keinen festen nächsten Dialog. Der weitere Verlauf hängt von der Auswahl des Spielers ab.

        this.choices = choices == null ? null : choices.clone(); /// Falls keine Auswahl vorhanden ist, wird null gespeichert.
                                                                /// sonst wird mit clone() eine Kopie erstellt -> geschützt und nicht veränderbar
        this.portrait = portrait;
    }

    public String getFormattedText() {
        if (speaker.isEmpty()) {
            return text;        /// Falls kein "SPEAKER" festgelegt ist, soll der Text einfach zurückgegeben werden
        }

        return speaker + ": \"" + text + "\"";  /// Sonst wird korrekt Formatiert -> Johnny: "Was geht?!"
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }

    public String getNextDialogueId() {
        return nextDialogueId;
    }

    public DialoguePortrait getPortrait() {
        return portrait;
    }

    public boolean hasChoices() {
        return choices != null && choices.length > 0;   /// Das Array darf nicht null sein & Es muss mindestens eine Antwort enthalten.
    }

    public DialogueChoice[] getChoices() {  /// Diese Methode gibt die gespeicherten Antwortmöglichkeiten zurück.
        return choices == null              /// Wenn choices den Wert null besitzt, dann null "returnen"
            ? null
            : choices.clone();              /// sonst soll die kopie des Arrays zurückgegeben werden.
    }
}

package de.Milad_Taromi.Dialogue;

public class DialogueChoice {

    private final String text;
    private final String nextDialogueId;

    public DialogueChoice(String text, String nextDialogueId) {
        this.text = text == null ? "" : text;   /// Falls text den Wert null besitzt, wird stattdessen ein leerer String ("") gespeichert.
        this.nextDialogueId = nextDialogueId;   /// oben: Verhindert Crashes wegen "null"
    }

    public String getText() {
        return text;        /// Diese Methode gibt den Text der Antwort zurück. Wird benötigt beim "rendern" der Buttons
    }

    public String getNextDialogueId() {
        return nextDialogueId;  /// Diese Methode gibt die ID des nächsten Dialogs zurück. Wichtig für GameManager.class
    }
}

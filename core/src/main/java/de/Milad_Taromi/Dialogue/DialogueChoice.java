package de.Milad_Taromi.Dialogue;

public class DialogueChoice {

    private final String text;
    private final String nextDialogueId;

    public DialogueChoice(String text, String nextDialogueId) {
        this.text = text == null ? "" : text;
        this.nextDialogueId = nextDialogueId;
    }

    public String getText() {
        return text;
    }

    public String getNextDialogueId() {
        return nextDialogueId;
    }
}

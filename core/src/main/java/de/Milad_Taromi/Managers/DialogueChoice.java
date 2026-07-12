package de.Milad_Taromi.Managers;

public class DialogueChoice {

    private final String text;
    private final int nextDialogueIndex;

    public DialogueChoice(String text, int nextDialogueIndex) {
        this.text = text;
        this.nextDialogueIndex = nextDialogueIndex;
    }

    public String getText() {
        return text;
    }

    public int getNextDialogueIndex() {
        return nextDialogueIndex;
    }
}

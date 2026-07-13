package de.Milad_Taromi.Managers;

import de.Milad_Taromi.Dialogue.DialogueChoice;
import de.Milad_Taromi.Dialogue.DialoguePortrait;

public class TextManager {

    private final String speaker;
    private final String text;

    private final String nextDialogueId;
    private final DialogueChoice[] choices;

    private final DialoguePortrait portrait;

    public TextManager(
        String speaker,
        String text,
        String nextDialogueId,
        DialoguePortrait portrait
    ) {
        this.speaker = speaker == null ? "" : speaker;
        this.text = text == null ? "" : text;

        this.nextDialogueId = nextDialogueId;
        this.choices = null;

        this.portrait = portrait;
    }

    public TextManager(
        String speaker,
        String text,
        DialogueChoice[] choices,
        DialoguePortrait portrait
    ) {
        this.speaker = speaker == null ? "" : speaker;
        this.text = text == null ? "" : text;

        this.nextDialogueId = null;

        this.choices = choices == null
            ? null
            : choices.clone();

        this.portrait = portrait;
    }

    public String getFormattedText() {
        if (speaker.isEmpty()) {
            return text;
        }

        return speaker + ": \"" + text + "\"";
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
        return choices != null && choices.length > 0;
    }

    public DialogueChoice[] getChoices() {
        return choices == null
            ? null
            : choices.clone();
    }
}

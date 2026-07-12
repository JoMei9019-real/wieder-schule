package de.Milad_Taromi.Managers;

public class TextManager {

    private final String speaker;
    private final String text;

    private final int nextDialogueIndex;
    private final DialogueChoice[] choices;

    private final DialoguePortrait portrait;
    private final PortraitSide portraitSide;

    /*
     * Normaler Dialog ohne Auswahl.
     */
    public TextManager(
        String speaker,
        String text,
        int nextDialogueIndex,
        DialoguePortrait portrait,
        PortraitSide portraitSide
    ) {
        this.speaker = speaker == null ? "" : speaker;
        this.text = text == null ? "" : text;

        this.nextDialogueIndex = nextDialogueIndex;
        this.choices = null;

        this.portrait = portrait;
        this.portraitSide = portraitSide;
    }

    /*
     * Dialog mit Auswahlmöglichkeiten.
     */
    public TextManager(
        String speaker,
        String text,
        DialogueChoice[] choices,
        DialoguePortrait portrait,
        PortraitSide portraitSide
    ) {
        this.speaker = speaker == null ? "" : speaker;
        this.text = text == null ? "" : text;

        this.nextDialogueIndex = -1;

        this.choices = choices == null
            ? null
            : choices.clone();

        this.portrait = portrait;
        this.portraitSide = portraitSide;
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

    public int getNextDialogueIndex() {
        return nextDialogueIndex;
    }

    public DialoguePortrait getPortrait() {
        return portrait;
    }

    public PortraitSide getPortraitSide() {
        return portraitSide;
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

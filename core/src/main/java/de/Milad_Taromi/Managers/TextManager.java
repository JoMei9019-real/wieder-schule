package de.Milad_Taromi.Managers;

public class TextManager {

    private final String speaker;
    private final String text;

    public TextManager(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    public String getFormattedText() {
        return speaker + ": \"" + text + "\"";
    }
}

package de.Milad_Taromi.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.Milad_Taromi.Game;
import de.Milad_Taromi.Dialogue.DialogueChoice;
import de.Milad_Taromi.Dialogue.DialoguePortrait;
import de.Milad_Taromi.Managers.GameManager;
import de.Milad_Taromi.Managers.TextManager;
import de.Milad_Taromi.Managers.TextureManager;

/// Die Klasse PlayScreen stellt den eigentlichen Spielbildschirm dar.
/// Sie zeigt den Hintergrund, die Dialogbox, Bilder und Antwortmöglichkeiten an.
/// Außerdem verarbeitet sie Tastatureingaben.

/// NOTES:
/// STATIC: Die Variable gehört zur Klasse selbst und nicht zu einem einzelnen PlayScreen-Objekt.
/// FINAL: Der Wert darf nach der Festlegung nicht mehr geändert werden.

public class PlayScreen implements Screen {


    private static final float DIALOGUE_HEIGHT = 210f;      /// Diese Werte bestimmen die Größe und Position der Dialogbox.
    private static final float DIALOGUE_MARGIN_X = 50f;     /// Abstand zum linken und rechten Rand
    private static final float DIALOGUE_MARGIN_BOTTOM = 35f;    /// Abstand nach unten


    private static final float TEXT_PADDING_LEFT = 69f;     /// Abstand zwischen Text und Rand der Dialogbox
    private static final float TEXT_PADDING_RIGHT = 45f;
    private static final float TEXT_PADDING_TOP = 25f;
    private static final float TEXT_PADDING_BOTTOM = 30f;

    private static final float IMAGE_MAX_WIDTH_RATIO = 0.76f;   /// Definiert max. Größe der Bilder (76% der Bildschirmbreite)
    private static final float IMAGE_MAX_HEIGHT_RATIO = 0.78f;

    private static final float IMAGE_OFFSET_Y = -12f;

    private static final float IMAGE_MARGIN_X = 25f;

    private static final float IMAGE_MARGIN_TOP = 20f;

    private static final float CHOICE_BUTTON_WIDTH = 350f;  /// Diese Konstanten bestimmen die Größe der Antwortbuttons.
    private static final float CHOICE_BUTTON_HEIGHT = 55f;
    private static final float CHOICE_BUTTON_SPACING = 12f;

    private final Game game;

    /// Diese Variable speichert, ob die Geschichte bereits gestartet wurde.
    /// Dadurch wird verhindert, dass der erste Akt mehrfach gestartet wird.
    private boolean storyStarted = false;

    /// Diese Variable blockiert Eingaben.
    /// Aktiv, wenn das Exit-Fenster geöffnet ist als bsp.
    private boolean noInput = false;

    /// Diese Variable speichert, ob aktuell Antwortmöglichkeiten angezeigt werden.
    private boolean choiceVisible = false;

    /// Kennen wir alles von vorhin, also dem MenuScreen.class
    private Stage stage;
    private Skin skin;
    private TextureManager textureManager;
    private GameManager gameManager;    /// Lädt die GameManager Klasse wo die Story u.a. definiert ist.

    private SpriteBatch batch;

    private NinePatchDrawable backgroundDrawable;   /// Hintergrund

    private Table dialogueTable;    /// Skalierung und rendern für Dialog und Choices
    private Table choicesTable;

    private Label dialogLabel;      /// Label bzw Text.
    private Cell<Label> dialogueLabelCell;

    private Image portraitImage;        /// WICHTIG: Portraits werden nicht mehr von uns verwendet. Viel Ballast noch im Code vorhanden leider!
    private DialoguePortrait currentPortrait = DialoguePortrait.NONE;   /// hier ein Beispiel haha

    private Texture currentPortraitTexture;     /// Aktuelles Bild (nicht nur Portrait)

    public PlayScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();

        textureManager = new TextureManager();
        backgroundDrawable = textureManager.textboxDrawable;    /// laden der Textur für Dialog-Box

        stage = new Stage(          /// Das gleiche wie beim Hauptmenü
            new ScreenViewport()
        );

        skin = new Skin(
            Gdx.files.internal("ui/extra/glassy-ui.json"));  /// Laden der Texturen für den Screen selbst

        gameManager = new GameManager(this);    /// Laden des GameManagers, welches unser Script enthält

        createUI(); /// Erstellen des GUIs
    }

    private void createUI() {
        createPortraitUI();     /// Laden der Portraits bzw der Bilder
        createDialogueUI();
        createChoicesUI();

        updateLayout();
    }

    private void createPortraitUI() {
        portraitImage = new Image();    /// Laden der Bilder

        portraitImage.setVisible(false);    /// Erstmal nicht sichtbar
        portraitImage.setTouchable(Touchable.disabled); /// Nicht anklickbar, um probleme beim User-Input zu vermeiden.
        portraitImage.setScaling(Scaling.fit);  /// Skalierung so, dass es auch ins Fenster passt

        stage.addActor(portraitImage);  /// Hinzufügen des Bildes zur Stage
    }

    private void createDialogueUI() {           /// Erstellen: Dialog-Box und Text
        dialogueTable = new Table();            /// Erstellen eines Tabels zur passenden Skalierung
        dialogueTable.setBackground(backgroundDrawable);    /// Hintergrund ist der, den wir oben im Konstruktor geladen haben. (Zeile 111)

        dialogLabel = new Label(        /// Ein Leerer Text (Label) wird erstellt, den wir später in der Gamemanager Klasse ändern werden.
            "",
            skin,
            "black"
        );
        dialogLabel.setFontScale(1.3f);         /// größe des Textes verändert, damit es angenehmer zu lesen ist :)
        dialogLabel.setWrap(true);              /// Verteilung von zu langen Texten auf mehreren Zeilen
        dialogLabel.setAlignment(Align.topLeft);/// Text beginnt oben Link

        dialogueLabelCell = dialogueTable.add(dialogLabel)  /// Festsetzen der Abstände und Hinzufügen zu einer Tabel (Skalierung)
            .expandX()
            .fillX()
            .padLeft(TEXT_PADDING_LEFT)             /// Variablen ganz oben im Konstruktor
            .padRight(TEXT_PADDING_RIGHT)
            .padTop(TEXT_PADDING_TOP)
            .padBottom(TEXT_PADDING_BOTTOM)
            .top()
            .left();

        stage.addActor(dialogueTable);
    }

    private void createChoicesUI() {            /// Kümmert sich um die Antwortmöglichkeiten
        choicesTable = new Table();

        choicesTable.setVisible(false);     /// erstmal unsichtbar, auf Abruf dann wieder sichtbar
        choicesTable.setTouchable(Touchable.enabled);   /// Anklickbar, sonst würden die Buttons nicht funktionieren

        stage.addActor(choicesTable);       /// Hinzufügen zum Rendern
    }

    private void updateLayout() {       /// Diese Methode berechnet die Größe und Position aller Elemente neu.
        float worldWidth =
            stage.getViewport().getWorldWidth();

        float dialogueWidth =                                   /// Die aktuelle Breite des Spielfensters wird abgefragt.
            worldWidth - DIALOGUE_MARGIN_X * 2f;                /// Von Fensterbreite werden je 50 pixel Abstand abgezogen

        dialogueTable.setSize(                                  /// Setzen der neuen größe (Nur wichtig für veränderbare Fenstergröße)
            dialogueWidth,                                      /// WICHTIG: Bei uns aktuell aus technischen Gründen ausgeschaltet!
            DIALOGUE_HEIGHT
        );

        dialogueTable.setPosition(
            DIALOGUE_MARGIN_X,
            DIALOGUE_MARGIN_BOTTOM
        );

        dialogueTable.invalidateHierarchy();        /// Anordnung zur Neuberrechnung durch die Engine

        updatePortraitSize();
        updatePortraitPosition();

        updateChoicesPosition();

        updateActorOrder();
    }

    private void updatePortraitSize() {                 /// Update Methode für die Bilder aus Open Source Game von GitHub geklaut :)  (Link: ???)
        if (
            portraitImage == null
                || !portraitImage.isVisible()               /// Falls kein Bild vorhanden ist, wird die Methode sofort beendet.
                || currentPortraitTexture == null           ///Danach werden die maximale Breite und Höhe berechnet.
        ) {
            return;
        }

        float worldWidth =
            stage.getViewport().getWorldWidth();            /// Auslesen der Fensterbreite und Höhe

        float worldHeight =
            stage.getViewport().getWorldHeight();

        float maxWidth =
            Math.min(
                worldWidth * IMAGE_MAX_WIDTH_RATIO,     /// Finde Minimum aus weltgröße * Variable und weltgröße - BildX * 2
                worldWidth - IMAGE_MARGIN_X * 2f
            );

        float maxHeightByScreen =
            worldHeight * IMAGE_MAX_HEIGHT_RATIO;

        float availableHeightAboveDialogue =
            worldHeight
                - dialogueTable.getTop()
                - IMAGE_MARGIN_TOP
                - IMAGE_OFFSET_Y;

        float maxHeight =
            Math.min(
                maxHeightByScreen,
                availableHeightAboveDialogue
            );

        maxWidth = Math.max(maxWidth, 1f);
        maxHeight = Math.max(maxHeight, 1f);

        float textureWidth =
            currentPortraitTexture.getWidth();

        float textureHeight =
            currentPortraitTexture.getHeight();

        float textureAspectRatio =
            textureWidth / textureHeight;

        float imageWidth = maxWidth;
        float imageHeight =
            imageWidth / textureAspectRatio;

        if (imageHeight > maxHeight) {
            imageHeight = maxHeight;
            imageWidth =
                imageHeight * textureAspectRatio;
        }

        portraitImage.setSize(
            imageWidth,                 /// Finale
            imageHeight
        );
    }

    private void updatePortraitPosition() {             /// Berechnung auch aus dem Git Code geklaut
        if (
            portraitImage == null
                || !portraitImage.isVisible()
        ) {
            return;
        }

        float worldWidth =
            stage.getViewport().getWorldWidth();

        float x =
            (worldWidth - portraitImage.getWidth()) / 2f;

        float y =
            dialogueTable.getTop()
                + IMAGE_OFFSET_Y;

        portraitImage.setPosition(
            MathUtils.round(x),
            MathUtils.round(y)
        );
    }

    private void updateChoicesPosition() {      /// Berechnung auch aus dem Git Code geklaut
        if (choicesTable == null) {
            return;
        }

        choicesTable.pack();

        float centerX =
            stage.getViewport().getWorldWidth() / 2f;

        float positionY =
            DIALOGUE_MARGIN_BOTTOM
                + DIALOGUE_HEIGHT
                + 35f;

        choicesTable.setPosition(
            centerX,
            positionY,
            Align.bottom
        );
    }

    private void updateActorOrder() {
        if (
            portraitImage != null
                && portraitImage.isVisible()        /// Falls Bild aktiv, dann ...
        ) {
            portraitImage.toFront();        /// Bilder immer zur Front sonst verschwinden sie im Hintergrund :)
        }

        dialogueTable.toFront();            /// Dialog auch!

        if (choiceVisible) {
            choicesTable.toFront();         /// "Auswahl" auch!
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); /// Die Stage erhält Tastatur- und Mauseingaben.

        updateLayout();     /// Layout muss up to date sein (jetzt unwichtig, da Fenstergröße eh fest ist!)

        if (!storyStarted) {        /// Wenn die Geschichte noch nicht gestartet wurde, beginnt der erste Akt.
            storyStarted = true;
            gameManager.startAct1();    /// Akt 1 wird aufgerufen aus GameManager.class
        }
    }

    public void displayDialogue(TextManager dialogue) {          /// Diese Methode zeigt einen vollständigen Dialog an.
        if (dialogue == null) {                                 /// Wenn kein Dialog festgesetzt ist, dann mach NIX!
            return;
        }

        setDialogue(                                            /// Sonst schreibe Text in Dialog-Box
            dialogue.getFormattedText()                         /// Text kommt aus dem TextManager.class, wo es vor-formatiert wird!
        );

        showPortrait(
            dialogue.getPortrait()                              /// TextManager.class gibt auch das passende Bild aus.
        );
    }

    public void setDialogue(String text) {
        dialogLabel.setText(
            text == null ? "" : text                        /// Falls text den Wert null hat, wird stattdessen ein leerer Text angezeigt um crash zu verhindern
        );                                                  /// Sonst wird "text" eingesetzt, welches wir durch den Methodenaufruf definieren (vgl. Z 347)
    }

    public void showPortrait(DialoguePortrait portrait) {
        currentPortrait = portrait;                         /// Abfrage des aktuellen Bildes (1x pro Methodenaufruf)

        if (
            portrait == null                                /// Falls keine Figur ausgewählt wurde, wird das Bild ausgeblendet.
                || portrait == DialoguePortrait.NONE
        ) {
            hidePortrait();                                 /// Ausblenden!
            return;
        }

        Texture portraitTexture =                           /// Sonst: aktuelle Textur aufrufen vom TextureManager.class
            textureManager.getPortraitTexture(portrait);

        if (portraitTexture == null) {                      /// Error-Handler, falls kein Bild aufgerufen werden konnte
            System.err.println(
                "[PORTRAIT] Keine Textur gefunden für: "
                    + portrait
            );

            hidePortrait();
            return;
        }

        currentPortraitTexture = portraitTexture;       /// currentPortraitTexture wird auf die aktuelle Textur des Bildes gesetzt

        portraitImage.setDrawable(
            new TextureRegionDrawable(                  /// Aufruf der Engine, um das Bild zu zeichnen
                new TextureRegion(portraitTexture)
            )
        );

        portraitImage.setVisible(true);             /// Dabei wird das Bild auf "sichtbar" gesetzt

        updatePortraitSize();                       /// Update der größe wird immer ausgeführt
        updatePortraitPosition();                   /// Position wird auch passend gesetzt

        updateActorOrder();                         /// Reihenfolge wird ebenfalls passend gesetzt
    }

    public void hidePortrait() {                    /// Das ist die passende Methode, um das Bild zu verstecken
        currentPortrait =
            DialoguePortrait.NONE;                  /// Setzt DialoguePortrait auf None

        currentPortraitTexture = null;              /// currentPortraitTexture Variable auf null gesetzt für passende Handler oben!

        portraitImage.setVisible(false);            /// Bild ist nun unsichtbar
        portraitImage.setDrawable(null);            /// Kann auch nicht mehr gezeichnet werden von der Engine
    }

    public void showChoices(DialogueChoice[] choices) {  /// Hier entsteht das Interface für die Entscheidungen im Game
        choicesTable.clearChildren();                    /// Entfernen alter Buttons

        if (
            choices == null                             /// Falls keine Antwortmöglichkeiten vorhanden sind, wird die Auswahltabelle ausgeblendet.
                || choices.length == 0
        ) {
            hideChoices();
            return;
        }

        choiceVisible = true;                       /// Sonst wird das Interface auf sichtbar gesetzt

        for (final DialogueChoice choice : choices) {
            final TextButton choiceButton = new TextButton(choice.getText(),
                skin,"small");              /// Mit einer Schleife wird für jede Antwortmöglichkeit ein eigener Button erstellt.

            choiceButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(            /// Button-CLICK Listener der Engine
                        ChangeEvent event,
                        Actor actor
                    ) {
                        if (
                            !choiceVisible
                                || noInput          /// Aber vorher überprüfen, ob Eingaben erlaubt sind!
                        ) {
                            return;                 /// Abbruch
                        }

                        choiceVisible = false;      /// choiceVisible auf false -> Unsichtbar

                        gameManager.selectChoice(choice); /// Der GameManager entscheidet anschließend, was beim klicken passiert!
                    }
                }
            );

            choicesTable.add(choiceButton)              /// Eintragen der Buttons mit passendem Abstand und Formatierung!
                .width(CHOICE_BUTTON_WIDTH)             /// Variablen vorher festgelegt = "Hardcoded"
                .height(CHOICE_BUTTON_HEIGHT)
                .padBottom(CHOICE_BUTTON_SPACING)
                .row();
        }

        choicesTable.pack();                        /// Komprimieren bzw. zusammenpacken
        choicesTable.setVisible(true);              /// Auf sichtbar setzen

        updateChoicesPosition();                    /// positions-update wenn Festergröße vom User geändert wird.
        updateActorOrder();                         /// Reihenfolge wieder updaten, damit nix irgendwie verschwindet oder überdeckt wird.
    }

    public void hideChoices() {                     /// Methode, um Auswahlmöglichkeiten zu verstecken
        choiceVisible = false;
        choicesTable.clearChildren();               /// Entfernen der Buttons usw.
        choicesTable.setVisible(false);             /// Verstecken der Table
    }

    private void keyManager() {                 /// Diese Methode prüft die Tastatureingaben.
        if (noInput) {
            return;
        }

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ESCAPE               /// Bei der ESC Taste ...
            )
        ) {
            System.out.println(
                "[DEBUG] ESC was pressed!"      /// Debug line
            );

            exitWindow();               /// exit-Fenster anzeigen!
            return;
        }

        if (
            !choiceVisible
                && Gdx.input.isKeyJustPressed(
                Input.Keys.SPACE        /// Mit SPACE-Taste kann man zum...
            )
        ) {
            gameManager.nextDialogue();     /// ... nächsten Dialog gelangen
        }
    }

    public void exitWindow() {  /// Exakte kopie wie von "MenuScreen.class"
        if (noInput) {          /// Nur mit überprüfung, ob das rendern erlaubt ist oder nicht, um den Spieler nicht zu stören!
            return;
        }

        noInput = true;

        final Image blocker = new Image(
            skin.newDrawable(
                "white",
                new Color(
                    0f,
                    0f,
                    0f,
                    0.5f
                )
            )
        );

        blocker.setSize(
            stage.getViewport().getWorldWidth(),
            stage.getViewport().getWorldHeight()
        );

        blocker.setPosition(0f, 0f);
        blocker.setTouchable(Touchable.enabled);

        stage.addActor(blocker);

        final Window window = new Window(
            "SPIEL VERLASSEN",
            skin
        );

        window.defaults().pad(4f);
        window.setMovable(false);
        window.setModal(true);

        Label questionLabel = new Label(
            "Willst du wirklich das Spiel verlassen?",
            skin,
            "black"
        );

        window.add(questionLabel)
            .colspan(2)
            .padBottom(20f)
            .row();

        final TextButton buttonYes =
            new TextButton(
                "JA",
                skin,
                "small"
            );

        final TextButton buttonNo =
            new TextButton(
                "NEIN",
                skin,
                "small"
            );

        buttonYes.pad(8f);
        buttonNo.pad(8f);

        buttonYes.addListener(
            new ChangeListener() {
                @Override
                public void changed(
                    ChangeEvent event,
                    Actor actor
                ) {
                    System.out.println(
                        "[DEBUG] EXIT_COMMAND"
                    );

                    window.remove();
                    blocker.remove();

                    noInput = false;

                    game.setScreen(
                        new MenuScreen(game)        /// Aufruf von Hauptmenü bei "JA"
                    );
                }
            }
        );

        buttonNo.addListener(
            new ChangeListener() {
                @Override
                public void changed(
                    ChangeEvent event,
                    Actor actor
                ) {
                    window.remove();
                    blocker.remove();           /// SONST: schließen der Fenster und Blocker, um input nicht zu behindern!

                    noInput = false;

                    Gdx.input.setInputProcessor(stage);     /// Input an Stage abgegeben
                }
            }
        );

        window.add(buttonYes)
            .width(120f)
            .height(50f)
            .padRight(20f);

        window.add(buttonNo)
            .width(120f)
            .height(50f);

        window.pack();
        window.setSize(
            350f,
            180f
        );

        window.setPosition(
            MathUtils.roundPositive(
                stage.getViewport().getWorldWidth() / 2f
                    - window.getWidth() / 2f
            ),
            MathUtils.roundPositive(
                stage.getViewport().getWorldHeight() / 2f
                    - window.getHeight() / 2f
            )
        );

        window.addAction(
            Actions.sequence(
                Actions.alpha(0f),
                Actions.fadeIn(0.3f)
            )
        );

        stage.addActor(window);

        blocker.toFront();
        window.toFront();

        Gdx.input.setInputProcessor(stage); /// Doppelt hält halt besser :D
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(    /// Der vorherige Bildschirminhalt wird gelöscht und der Hintergrund zunächst schwarz gefärbt.
            0f,
            0f,
            0f,
            1f
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        batch.begin();      /// START SIGNAL zum ZEICHNEN!

        batch.draw(
            textureManager.menuBackgroundDefault,   /// Hintergrund
            0f,
            0f,
            Gdx.graphics.getWidth(),                /// mit passender Höhe
            Gdx.graphics.getHeight()                /// und Breite
        );

        batch.end();                /// ENDE der ZEICHNUNG!

        keyManager();       /// Permanente ausführung vom KeyManager

        stage.act(delta);       /// Animationen und UI-Elemente werden aktualisiert.
        stage.draw();           /// Alle Elemente final gezeichnet
    }

    @Override
    public void resize(     /// Nur bei veränderung der Fenstergröße aufgerufen, bei uns nicht der Fall!
        int width,
        int height
    ) {
        stage.getViewport().update(
            width,
            height,
            true
        );

        updateLayout();     /// Methode zur Anpassung existiert trotzdem
    }

    @Override
    public void pause() {       /// Von der ENGINE SELBST eingesetzte Methoden, bitte nicht löschen!
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        if (
            Gdx.input.getInputProcessor() == stage
        ) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void dispose() {     /// Alles löschen beim Aufruf, weil RAM = TEUER
        batch.dispose();
        stage.dispose();
        skin.dispose();
        textureManager.dispose();
    }
}

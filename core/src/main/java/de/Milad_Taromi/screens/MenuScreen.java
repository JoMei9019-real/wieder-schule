package de.Milad_Taromi.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.Milad_Taromi.Game;
import de.Milad_Taromi.Managers.TextureManager;

public class MenuScreen implements Screen {

    private Stage stage;  /// Verwaltet alle sichtbaren und anklickbaren UI-Elemente (Buttons, Bilder und Fenster).
    private Skin skin;    /// Enthält die Texturen für GUI.
    private Skin window_skin;   /// Nicht verwendet - deswegen grau!
    private Game game;  /// Speichert die Hauptklasse des Spiels!
    private TextureManager textureManager;  /// Zuständig für unsere Texturen
    private SpriteBatch batch;  /// Hintergrund zeichnen (in diesem Fall)!
    private Window activeWindow; /// Nicht verwendet - deswegen grau!

    /// MenuScreen stellt das Hauptmenü dar.
    /// Zeigt den Hintergrund, das Logo und die drei Buttons PLAY, OPTIONS und EXIT.

    public MenuScreen(Game game){
        this.game = game;
        stage = new Stage(new ScreenViewport());    /// ScreenViewport wird benötigt für das Rendern bzw. Updaten der Fenster!
        Gdx.input.setInputProcessor(stage);         /// Stage erhält Eingabe vom USER!
        skin = new Skin(Gdx.files.internal("ui/extra/glassy-ui.json")); /// Laden des Designs (in einer JSON gespeichert)
        window_skin = new Skin(Gdx.files.internal("ui/uiskin.json"));   /// Laden des Designs (in einer JSON gespeichert)
        batch = new SpriteBatch();
    }

    @Override
    public void show() {    /// Automatische Ausführung durch die Engine beim Aufruf des Screens!
        textureManager = game.textures; /// Texturen werden von klasse Game übernommen, um RAM zu sparen (ist sehr teuer hehe)
        Table table = new Table();      /// Aufruf von "TABLE", um Logos und Buttons grafisch anzuordnen --> Mathematik :(
        table.setFillParent(true);      /// Sorgt dafür, dass die Tabelle die gesamte Stage ausfüllt.
        // table.setDebug(true); /// <- Debug-Linien deaktiviert
        stage.addActor(table);  /// Tabelle zur Benutzeroberfläche hinzugefügt - leider pflicht für jedes Element
        Image logoImage = new Image(textureManager.logoTexture);    /// Aufrufen der Logo-Textur für unser Logo

        TextButton play = new TextButton("SPIELEN", skin);            /// Erstellen der Buttons und
        TextButton options = new TextButton("OPTIONEN", skin);      /// Angabe für passende Textur - hier: "skin"
        TextButton exit = new TextButton("VERLASSEN", skin);            /// Buttons: PLAY, OPTIONS, EXIT

        table.center();      /// Die Tabelle wird in der Mitte des Bildschirms positioniert.
        table.padTop(50f);  /// Inhalt leicht nach unten verschoben.

        float logoWidth = 300f;     /// Die Breite des Logos wird auf 300 Pixel gesetzt.
        float aspectRatio = (float) textureManager.logoTexture.getHeight()      /// Hier wird das Seitenverhältnis des Logos berechnet.
            / textureManager.logoTexture.getWidth();                /// Dadurch kann die passende Höhe bestimmt werden, ohne dass das Bild verzerrt wird.
                                                                    /// Nur dank Tutorial möglich, währe selbst niemals darauf gekommen!
        table.add(logoImage)                    /// Logo wird in Tabelle geladen
            .width(logoWidth)
            .height(logoWidth * aspectRatio + 10)       /// Rendering und Skalieren ... hilfe ...
            .padBottom(0f)
            .row();                         /// Neue Zeile :)

        /// Alles für die Platzierung der Buttons. Abstand zwischeneinander etc.
        table.add(play).fill().uniformX();
        table.row().pad(10, 0, 10, 0); // gleicher Abstand oben/unten für alle Buttons
        table.add(options).fillX().uniformX();
        table.row().pad(0, 0, 10, 0);
        table.add(exit).fillX().uniformX();

        play.addListener(new ChangeListener() { ///PLAY-BUTTON ACTION
        @Override
        public void changed(ChangeEvent event, Actor actor) {   /// Ein ChangeListener erkennt, wenn der Button angeklickt wird.
            //System.out.println("[DEBUG] PLAY_S2_PRESSED");
            //System.out.println("Window: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
            game.setScreen(new PlayScreen(game));   /// Beim Klicken wird ein neuer Screen geöffnet (PlayScreen.class)
        }
        });

        options.addListener(new ChangeListener() {      ///OPTION-BUTTON-ACTION
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsWINDOW();    /// Hier wird die Methode vom "Hinweisfenster" aufgerufen!
            }
        });

        exit.addListener(new ChangeListener() {     ///EXIT-BUTTON ACTION
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            System.out.println("[DEBUG] EXIT_BUTTON_PRESSED");  ///DEBUG!
            exitWINDOW();               /// Methodenaufruf vom "exitWindow"
        }
        });
    }

    public void exitWINDOW(){           /// Aufgerufen durch Button "ChangeEvent"
        //BLOCKER
        Image blocker = new Image(skin.newDrawable("white", new Color(0, 0, 0, 0.5f))); /// Es wird ein halbtransparentes schwarzes Bild erstellt!
        blocker.setSize(stage.getWidth(), stage.getHeight());   /// Das Bild wird so groß wie der gesamte Bildschirm.
        blocker.setTouchable(Touchable.enabled);  /// Wichtig, damit es Touch-Events blockt. Der Blocker fängt Mausklicks ab.
        stage.addActor(blocker);                /// Der Blocker wird zur Stage hinzugefügt.
        //WINDOW AND BUTTONS
        Window window = new Window("BEENDEN", skin);   /// Ein neues Fenster mit der Überschrift EXIT wird erstellt.
        window.defaults().pad(4f);     /// ABSTAND!
        window.setMovable(false);     /// Das Fenster kann nicht mit der Maus verschoben werden.
        window.add(new Label("Willst du wirklich das Spiel beenden?", skin, "black")).colspan(2).row();   /// Label mit Style :)
        final TextButton button_yes = new TextButton("JA", skin, "small");  /// Ausgeben von Buttons im kleinen Style (einfach kleiner)
        final TextButton button_no = new TextButton("NEIN", skin, "small");
        button_yes.pad(8f);     /// Abstand zwischen den Buttons = 8f (keine Ahnung was das sein soll haha)
        button_no.pad(8f);      /// Abstand zwischen den Buttons = 8f (keine Ahnung was das sein soll haha)
        button_yes.addListener(new ChangeListener() {   /// Das kennen wir doch irgendwo her? - (Moin Andi)
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                System.out.println("[DEBUG] EXIT_COMMAND_BExit");   /// Debug, nicht so wichtig
                window.remove();        /// Entfernt das Fenster, sobald man auf "JA" drückt.
                Gdx.app.exit();     //EXIT!
            }
        });
        button_no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {   /// Wenn man "NEIN" drückt, dann soll einfach:
                window.remove();                                    /// Das Fenster entfernt werden
                blocker.remove();                                   /// Und der Blocker auch, sonst blöd!
            }
        });
        window.add(button_yes).padRight(20f);                       /// Buttons mit passender skalierung (20f) hinzufügen zum Fenster
        window.add(button_no);
        window.pack();                                              /// Komprimieren
        window.setSize(350, 180);                       /// Selbsterklärend

        /// Aus dem Tutorial kopiert - ist da, um "awkward half-pixel artifacts" zu vermeiden. Keine Ahnung, ob das überhaupt nötig ist.
        // We round the window position to avoid awkward half-pixel artifacts.
        // Casting using (int) would also work.
        window.setPosition(MathUtils.roundPositive(stage.getWidth() / 2f - window.getWidth() / 2f),
            MathUtils.roundPositive(stage.getHeight() / 2f - window.getHeight() / 2f));
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window); /// Fenster wird zur Stage hinzugefügt, um es zu rendern

        Gdx.input.setInputProcessor(stage); /// Input dem STAGE übergeben
    }

    public void optionsWINDOW(){            /// Das gleiche wie oben halt!
        ///BLOCKER
        Image blocker = new Image(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        blocker.setSize(stage.getWidth(), stage.getHeight());
        blocker.setTouchable(Touchable.enabled); // wichtig, damit es Touch-Events blockt
        stage.addActor(blocker);
        ///WINDOW AND BUTTONS
        Window window = new Window("OPTIONEN", skin);
        window.defaults().pad(4f);
        window.setMovable(false);
        window.add(new Label("Aktuell nicht verfuegbar :)", skin, "black")).colspan(2).row();
        final TextButton button_ok = new TextButton("OK", skin, "small");
        button_ok.pad(8f);
        button_ok.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                window.remove();
                blocker.remove(); //remove it!
            }
        });
        window.add(button_ok).padTop(20f);
        window.pack();
        window.setSize(350, 180);
        // We round the window position to avoid awkward half-pixel artifacts.
        // Casting using (int) would also work.
        window.setPosition(MathUtils.roundPositive(stage.getWidth() / 2f - window.getWidth() / 2f),
            MathUtils.roundPositive(stage.getHeight() / 2f - window.getHeight() / 2f));
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {   /// Die Methode render() wird während des Spiels ständig wiederholt. Normalerweise geschieht das viele Male pro Sekunde.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1); /// löschen des vorherigen Bildschirmes
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   /// Die Hintergrundfarbe wird dabei auf Schwarz gesetzt.
        Gdx.graphics.setTitle(Game.getTitle() + "FPS: " + Gdx.graphics.getFramesPerSecond());   /// MEINE TOLLE FPS ANZEIGE - JAYY

        batch.begin();      /// Beginn der Zeichnung
        batch.draw(
            textureManager.menuBackground,      /// Hintergrund wird gerendert
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()         /// Skalierung auf Fenstergröße
        );
        batch.end();    /// Ende der Zeichnung, sonst crasht die Engine

        /// Aktualisiert die Stage und ihre Animationen. getDeltaTime() gibt an, wie viel Zeit seit dem letzten Bild vergangen ist.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();   /// Danach werden alle UI-Elemente der Stage gezeichnet, beispielsweise Logo, Buttons und Fenster.
    }

    @Override
    public void resize(int width, int height) {         /// Diese Methode wird aufgerufen, wenn die Größe des Spielfensters verändert wird.
        stage.getViewport().update(width, height, true); /// Der Viewport wird an die neue Fenstergröße angepasst.
        //updateWindowSize(); // Window bei Resize mit skalieren
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {     /// Die Methode gibt nicht mehr benötigte Ressourcen frei. Wichtig um RAM zu sparen
        stage.dispose();
        batch.dispose();
    }
}

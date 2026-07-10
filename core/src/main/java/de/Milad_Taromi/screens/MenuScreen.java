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
import de.Milad_Taromi.Textures.TextureManager;

public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Skin window_skin;
    private Game game;
    private TextureManager textureManager;
    private SpriteBatch batch;
    private Window activeWindow;

    public MenuScreen(Game game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/extra/glassy-ui.json"));
        window_skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        textureManager = game.textures;

        Table table = new Table();
        table.setFillParent(true);
        // table.setDebug(true); // <- Debug-Linien deaktiviert
        stage.addActor(table);

        Image logoImage = new Image(textureManager.logoTexture);

        TextButton play = new TextButton("PLAY", skin);
        TextButton options = new TextButton("OPTIONS", skin);
        TextButton exit = new TextButton("EXIT", skin);

        table.center(); // zentriert bleiben, NICHT top()
        table.padTop(50f); // leichte Verschiebung nach unten, relativ zur Mitte

        float logoWidth = 300f;
        float aspectRatio = (float) textureManager.logoTexture.getHeight()
            / textureManager.logoTexture.getWidth();

        table.add(logoImage)
            .width(logoWidth)
            .height(logoWidth * aspectRatio + 10)
            .padBottom(0f)
            .row();

        table.add(play).fill().uniformX();
        table.row().pad(10, 0, 10, 0); // gleicher Abstand oben/unten für alle Buttons
        table.add(options).fillX().uniformX();
        table.row().pad(0, 0, 10, 0);
        table.add(exit).fillX().uniformX();

        play.addListener(new ChangeListener() { ///PLAY-BUTTON ACTION
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            //System.out.println("[DEBUG] PLAY_S2_PRESSED");
            //System.out.println("Window: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
            game.setScreen(new PlayScreen(game));
        }
        });

        options.addListener(new ChangeListener() {      ///OPTION-BUTTON-ACTION
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsWINDOW();
            }
        });

        exit.addListener(new ChangeListener() {     ///EXIT-BUTTON ACTION
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            System.out.println("[DEBUG] EXIT_BUTTON_PRESSED");
            exitWINDOW();
        }
        });
    }

    public void exitWINDOW(){
        ///BLOCKER
        Image blocker = new Image(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        blocker.setSize(stage.getWidth(), stage.getHeight());
        blocker.setTouchable(Touchable.enabled); // wichtig, damit es Touch-Events blockt
        stage.addActor(blocker);
        ///WINDOW AND BUTTONS
        Window window = new Window("EXIT", skin);
        window.defaults().pad(4f);
        window.setMovable(false);
        window.add(new Label("Do you really want to exit?", skin, "black")).colspan(2).row();
        final TextButton button_yes = new TextButton("YES", skin, "small");
        final TextButton button_no = new TextButton("NO", skin, "small");
        button_yes.pad(8f);
        button_no.pad(8f);
        button_yes.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                System.out.println("[DEBUG] EXIT_COMMAND_BExit");
                window.remove();    //remove it!
                Gdx.app.exit();     //EXIT!
            }
        });
        button_no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
                blocker.remove();
            }
        });
        window.add(button_yes).padRight(20f);
        window.add(button_no);
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

    public void optionsWINDOW(){
        ///BLOCKER
        Image blocker = new Image(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        blocker.setSize(stage.getWidth(), stage.getHeight());
        blocker.setTouchable(Touchable.enabled); // wichtig, damit es Touch-Events blockt
        stage.addActor(blocker);
        ///WINDOW AND BUTTONS
        Window window = new Window("OPTIONS", skin);
        window.defaults().pad(4f);
        window.setMovable(false);
        window.add(new Label("Not ready yet! :)", skin, "black")).colspan(2).row();
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
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setTitle(Game.getTitle() + "FPS: " + Gdx.graphics.getFramesPerSecond());

        batch.begin();
        batch.draw(
            textureManager.menuBackground,
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}

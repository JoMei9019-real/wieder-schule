package de.Milad_Taromi.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.Milad_Taromi.Game;
import de.Milad_Taromi.Managers.GameManager;
import de.Milad_Taromi.Managers.TextureManager;

public class PlayScreen implements Screen {

    private static final float DIALOGUE_HEIGHT = 210f;
    private static final float DIALOGUE_MARGIN_X = 50f;
    private static final float DIALOGUE_MARGIN_BOTTOM = 35f;

    private final Game game;

    private boolean storyStarted = false;
    private boolean noinput = false;

    private Stage stage;
    private Skin skin;
    private TextureManager textureManager;

    private NinePatchDrawable backgroundDrawable;
    private Table dialogueTable;
    private Label dialogLabel;
    private SpriteBatch batch;
    private GameManager gameManager;

    public PlayScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        textureManager = new TextureManager();
        backgroundDrawable = textureManager.textboxDrawable;
        gameManager = new GameManager(this);

        stage = new Stage(new ScreenViewport());

        skin = new Skin(
            Gdx.files.internal("ui/extra/glassy-ui.json")
        );

        createUI();
    }

    private void createUI() {           ////EIN UI ERSTELLEN :)
        dialogueTable = new Table();
        dialogueTable.setBackground(backgroundDrawable);

        dialogLabel = new Label(
            "Held: \"Dies ist ein Label!...\"",
            skin,
            "black"
        );

        dialogLabel.setWrap(true);
        dialogLabel.setAlignment(Align.topLeft);

        dialogueTable.add(dialogLabel)
            .expand()
            .fill()
            .padLeft(69f)
            .padRight(45f)
            .padTop(45f)
            .padBottom(30f)
            .top()
            .left();

        stage.addActor(dialogueTable);

        updateLayout();
    }

    private void updateLayout() {
        float worldWidth = stage.getViewport().getWorldWidth();

        float dialogueWidth =
            worldWidth - DIALOGUE_MARGIN_X * 2f;

        dialogueTable.setSize(
            dialogueWidth,
            DIALOGUE_HEIGHT
        );

        dialogueTable.setPosition(
            DIALOGUE_MARGIN_X,
            DIALOGUE_MARGIN_BOTTOM
        );

        dialogueTable.invalidateHierarchy();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Falls show() nach resize() aufgerufen wird
        updateLayout();
        if (!storyStarted) {
            storyStarted = true;
            gameManager.startAct1();
        }
    }

    public void setDialogue(String text) {
        dialogLabel.setText(text);
    }


    private void keyManager() {
       if(noinput != true){
           if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
               System.out.println("[DEBUG] ESC was pressed!");
               exitWINDOW();
           }

           if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
               gameManager.nextDialogue();
           }
       }
    }

    public void exitWINDOW(){
        ///BLOCKER
        Image blocker = new Image(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        blocker.setSize(stage.getWidth(), stage.getHeight());
        blocker.setTouchable(Touchable.enabled); // wichtig, damit es Touch-Events blockt
        noinput = true;
        Gdx.input.setInputProcessor(null);
        stage.addActor(blocker);
        ///WINDOW AND BUTTONS
        Window window = new Window("EXIT TO MENU", skin);
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
                noinput = false;
                game.setScreen(new MenuScreen(game));
            }
        });
        button_no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
                blocker.remove();
                noinput = false;
                Gdx.input.setInputProcessor(stage);
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();          ///Background - Handler
        batch.draw(
            textureManager.menuBackgroundHorror,
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
        batch.end();

        keyManager();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updateLayout();
    }

    @Override
    public void pause() {
        // Wird automatisch aufgerufen, wenn das Spiel pausiert wird.
    }

    @Override
    public void resume() {
        // Wird automatisch aufgerufen, wenn das Spiel fortgesetzt wird.
    }

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        skin.dispose();
        textureManager.dispose();
    }
}

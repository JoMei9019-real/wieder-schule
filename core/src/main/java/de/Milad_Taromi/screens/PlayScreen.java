package de.Milad_Taromi.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.Milad_Taromi.Game;
import de.Milad_Taromi.Textures.TextureManager;

public class PlayScreen implements Screen {

    private static final float DIALOGUE_HEIGHT = 210f;
    private static final float DIALOGUE_MARGIN_X = 50f;
    private static final float DIALOGUE_MARGIN_BOTTOM = 35f;

    private final Game game;

    private Stage stage;
    private Skin skin;
    private TextureManager textureManager;

    private NinePatchDrawable backgroundDrawable;
    private Table dialogueTable;
    private Label dialogLabel;
    private SpriteBatch batch;

    public PlayScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        textureManager = new TextureManager();
        backgroundDrawable = textureManager.textboxDrawable;

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
    }

    private void keyManager() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("[DEBUG] ESC was pressed!");

            dialogLabel.setText(
                "Held: \"Brauchst du eine Pause?...\""
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dialogLabel.setText(
                "Held: \"Mit Leertaste wurde der Text geändert.\""
            );
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(
            textureManager.menuBackground,
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
        stage.dispose();
        skin.dispose();
        textureManager.dispose();
    }
}

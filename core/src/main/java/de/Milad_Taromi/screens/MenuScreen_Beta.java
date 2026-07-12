package de.Milad_Taromi.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.Milad_Taromi.Game;
import de.Milad_Taromi.Managers.TextureManager;

public class MenuScreen_Beta implements Screen {

    private Game game;

    private Stage stage;
    private Skin skin;
    private TextureManager textureManager;

    // Eigene Kamera/Viewport NUR für den Background-Draw,
    // damit es unabhängig davon funktioniert, wie game.batch sonst genutzt wird.
    private OrthographicCamera bgCamera;
    private Viewport bgViewport;
    private SpriteBatch batch; // eigener Batch, falls game.batch anders konfiguriert ist

    private TextButton playButton;
    private TextButton optionsButton;
    private TextButton exitButton;

    private Table table;

    // Buttons sollen relativ zur Fenstergröße sein
    private static final float BUTTON_WIDTH_PERCENT = 0.25f;   // 25% der Fensterbreite
    private static final float BUTTON_HEIGHT_PERCENT = 0.12f;  // 12% der Fensterhöhe
    private static final float BUTTON_PAD_PERCENT = 0.02f;     // 2% Abstand

    public MenuScreen_Beta(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        textureManager = game.textures;

        // Eigene Kamera/Viewport für sauberes Background-Rendering
        bgCamera = new OrthographicCamera();
        bgViewport = new ScreenViewport(bgCamera);
        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.center();

        stage.addActor(table);

        playButton = new TextButton("", textureManager.playStyle);
        optionsButton = new TextButton("", textureManager.optionsStyle);
        exitButton = new TextButton("", textureManager.quitStyle);

        table.add(playButton).padBottom(20);
        table.row();
        table.add(optionsButton).padBottom(20);
        table.row();
        table.add(exitButton);

        // Initiale Größen direkt setzen
        updateButtonSizes(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playButton.addListener(new ClickListener() {        ///// PLAY BUTTON LISTENER
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("[DEBUG] PLAY clicked!");
                // game.setScreen(new GameScreen(game));
            }
        });

        optionsButton.addListener(new ClickListener() {     ///// OPTIONS BUTTON LISTENER
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("[DEBUG] OPTIONS clicked!");
                System.out.println("Window: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
                game.setScreen(new MenuScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {        ///// EXIT BUTTON LISTENER
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("[DEBUG] EXIT clicked!");    //ADD "ARE YOU SURE?" WINDOW!
                Gdx.app.exit();
            }
        });
    }

    /**
     * Berechnet Button-Größen als Prozentsatz der aktuellen Fenstergröße
     * und setzt sie auf die jeweiligen Table-Cells.
     */
    private void updateButtonSizes(int width, int height) {
        float btnW = width * BUTTON_WIDTH_PERCENT;
        float btnH = height * BUTTON_HEIGHT_PERCENT;
        float padBottom = height * BUTTON_PAD_PERCENT;

        Cell<TextButton> playCell = table.getCell(playButton);
        Cell<TextButton> optionsCell = table.getCell(optionsButton);
        Cell<TextButton> exitCell = table.getCell(exitButton);

        playCell.width(btnW).height(btnH).padBottom(padBottom);
        optionsCell.width(btnW).height(btnH).padBottom(padBottom);
        exitCell.width(btnW).height(btnH);

        table.invalidate(); // Layout neu berechnen lassen
    }

    private void drawBackground() {
        Texture bg = textureManager.menuBackground;

        float screenW = bgViewport.getScreenWidth();
        float screenH = bgViewport.getScreenHeight();

        float imageW = bg.getWidth();
        float imageH = bg.getHeight();

        // "Cover"-Skalierung: Bild füllt den ganzen Screen, kein Verzerren,
        // da scale gleichmäßig auf X und Y angewendet wird.
        float scale = Math.max(screenW / imageW, screenH / imageH);

        float width = imageW * scale;
        float height = imageH * scale;

        float x = (screenW - width) / 2f;
        float y = (screenH - height) / 2f;

        batch.draw(bg, x, y, width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.graphics.setTitle(Game.getTitle() + "FPS: " + Gdx.graphics.getFramesPerSecond());

        bgViewport.apply();
        batch.setProjectionMatrix(bgCamera.combined);
        batch.begin();
        drawBackground();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        bgViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

        updateButtonSizes(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }
}

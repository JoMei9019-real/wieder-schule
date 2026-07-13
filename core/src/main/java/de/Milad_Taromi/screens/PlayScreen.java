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

public class PlayScreen implements Screen {


    private static final float DIALOGUE_HEIGHT = 210f;
    private static final float DIALOGUE_MARGIN_X = 50f;
    private static final float DIALOGUE_MARGIN_BOTTOM = 35f;


    private static final float TEXT_PADDING_LEFT = 69f;
    private static final float TEXT_PADDING_RIGHT = 45f;
    private static final float TEXT_PADDING_TOP = 25f;
    private static final float TEXT_PADDING_BOTTOM = 30f;

    private static final float IMAGE_MAX_WIDTH_RATIO = 0.76f;
    private static final float IMAGE_MAX_HEIGHT_RATIO = 0.78f;

    private static final float IMAGE_OFFSET_Y = -12f;

    private static final float IMAGE_MARGIN_X = 25f;

    private static final float IMAGE_MARGIN_TOP = 20f;

    private static final float CHOICE_BUTTON_WIDTH = 350f;
    private static final float CHOICE_BUTTON_HEIGHT = 55f;
    private static final float CHOICE_BUTTON_SPACING = 12f;

    private final Game game;

    private boolean storyStarted = false;
    private boolean noInput = false;
    private boolean choiceVisible = false;

    private Stage stage;
    private Skin skin;
    private TextureManager textureManager;
    private GameManager gameManager;

    private SpriteBatch batch;

    private NinePatchDrawable backgroundDrawable;

    private Table dialogueTable;
    private Table choicesTable;

    private Label dialogLabel;
    private Cell<Label> dialogueLabelCell;

    private Image portraitImage;
    private DialoguePortrait currentPortrait = DialoguePortrait.NONE;

    private Texture currentPortraitTexture;

    public PlayScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();

        textureManager = new TextureManager();
        backgroundDrawable = textureManager.textboxDrawable;

        stage = new Stage(
            new ScreenViewport()
        );

        skin = new Skin(
            Gdx.files.internal(
                "ui/extra/glassy-ui.json"
            )
        );

        gameManager = new GameManager(this);

        createUI();
    }

    private void createUI() {
        createPortraitUI();
        createDialogueUI();
        createChoicesUI();

        updateLayout();
    }

    private void createPortraitUI() {
        portraitImage = new Image();

        portraitImage.setVisible(false);
        portraitImage.setTouchable(Touchable.disabled);
        portraitImage.setScaling(Scaling.fit);

        stage.addActor(portraitImage);
    }

    private void createDialogueUI() {
        dialogueTable = new Table();
        dialogueTable.setBackground(backgroundDrawable);

        dialogLabel = new Label(
            "",
            skin,
            "black"
        );
        dialogLabel.setFontScale(1.3f);
        dialogLabel.setWrap(true);
        dialogLabel.setAlignment(Align.topLeft);

        dialogueLabelCell = dialogueTable.add(dialogLabel)
            .expandX()
            .fillX()
            .padLeft(TEXT_PADDING_LEFT)
            .padRight(TEXT_PADDING_RIGHT)
            .padTop(TEXT_PADDING_TOP)
            .padBottom(TEXT_PADDING_BOTTOM)
            .top()
            .left();

        stage.addActor(dialogueTable);
    }

    private void createChoicesUI() {
        choicesTable = new Table();

        choicesTable.setVisible(false);
        choicesTable.setTouchable(Touchable.enabled);

        stage.addActor(choicesTable);
    }

    private void updateLayout() {
        float worldWidth =
            stage.getViewport().getWorldWidth();

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

        updatePortraitSize();
        updatePortraitPosition();

        updateChoicesPosition();

        updateActorOrder();
    }

    private void updatePortraitSize() {
        if (
            portraitImage == null
                || !portraitImage.isVisible()
                || currentPortraitTexture == null
        ) {
            return;
        }

        float worldWidth =
            stage.getViewport().getWorldWidth();

        float worldHeight =
            stage.getViewport().getWorldHeight();

        float maxWidth =
            Math.min(
                worldWidth * IMAGE_MAX_WIDTH_RATIO,
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
            imageWidth,
            imageHeight
        );
    }

    private void updatePortraitPosition() {
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

    private void updateChoicesPosition() {
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
                && portraitImage.isVisible()
        ) {
            portraitImage.toFront();
        }

        dialogueTable.toFront();

        if (choiceVisible) {
            choicesTable.toFront();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        updateLayout();

        if (!storyStarted) {
            storyStarted = true;
            gameManager.startAct1();
        }
    }

    public void displayDialogue(TextManager dialogue) {
        if (dialogue == null) {
            return;
        }

        setDialogue(
            dialogue.getFormattedText()
        );

        showPortrait(
            dialogue.getPortrait()
        );
    }

    public void setDialogue(String text) {
        dialogLabel.setText(
            text == null ? "" : text
        );
    }

    public void showPortrait(
        DialoguePortrait portrait
    ) {
        currentPortrait = portrait;

        if (
            portrait == null
                || portrait == DialoguePortrait.NONE
        ) {
            hidePortrait();
            return;
        }

        Texture portraitTexture =
            textureManager.getPortraitTexture(portrait);

        if (portraitTexture == null) {
            System.err.println(
                "[PORTRAIT] Keine Textur gefunden für: "
                    + portrait
            );

            hidePortrait();
            return;
        }

        currentPortraitTexture = portraitTexture;

        portraitImage.setDrawable(
            new TextureRegionDrawable(
                new TextureRegion(portraitTexture)
            )
        );

        portraitImage.setVisible(true);

        updatePortraitSize();
        updatePortraitPosition();

        updateActorOrder();
    }

    public void hidePortrait() {
        currentPortrait =
            DialoguePortrait.NONE;

        currentPortraitTexture = null;

        portraitImage.setVisible(false);
        portraitImage.setDrawable(null);
    }

    public void showChoices(
        DialogueChoice[] choices
    ) {
        choicesTable.clearChildren();

        if (
            choices == null
                || choices.length == 0
        ) {
            hideChoices();
            return;
        }

        choiceVisible = true;

        for (
            final DialogueChoice choice : choices
        ) {
            final TextButton choiceButton =
                new TextButton(
                    choice.getText(),
                    skin,
                    "small"
                );

            choiceButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(
                        ChangeEvent event,
                        Actor actor
                    ) {
                        if (
                            !choiceVisible
                                || noInput
                        ) {
                            return;
                        }

                        choiceVisible = false;

                        gameManager.selectChoice(choice);
                    }
                }
            );

            choicesTable.add(choiceButton)
                .width(CHOICE_BUTTON_WIDTH)
                .height(CHOICE_BUTTON_HEIGHT)
                .padBottom(CHOICE_BUTTON_SPACING)
                .row();
        }

        choicesTable.pack();
        choicesTable.setVisible(true);

        updateChoicesPosition();
        updateActorOrder();
    }

    public void hideChoices() {
        choiceVisible = false;

        choicesTable.clearChildren();
        choicesTable.setVisible(false);
    }

    private void keyManager() {
        if (noInput) {
            return;
        }

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ESCAPE
            )
        ) {
            System.out.println(
                "[DEBUG] ESC was pressed!"
            );

            exitWindow();
            return;
        }

        if (
            !choiceVisible
                && Gdx.input.isKeyJustPressed(
                Input.Keys.SPACE
            )
        ) {
            gameManager.nextDialogue();
        }
    }

    public void exitWindow() {
        if (noInput) {
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
            "EXIT TO MENU",
            skin
        );

        window.defaults().pad(4f);
        window.setMovable(false);
        window.setModal(true);

        Label questionLabel = new Label(
            "Do you really want to exit?",
            skin,
            "black"
        );

        window.add(questionLabel)
            .colspan(2)
            .padBottom(20f)
            .row();

        final TextButton buttonYes =
            new TextButton(
                "YES",
                skin,
                "small"
            );

        final TextButton buttonNo =
            new TextButton(
                "NO",
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
                        new MenuScreen(game)
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
                    blocker.remove();

                    noInput = false;

                    Gdx.input.setInputProcessor(stage);
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

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
            0f,
            0f,
            0f,
            1f
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        batch.begin();

        batch.draw(
            textureManager.menuBackgroundDefault,
            0f,
            0f,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        batch.end();

        keyManager();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(
        int width,
        int height
    ) {
        stage.getViewport().update(
            width,
            height,
            true
        );

        updateLayout();
    }

    @Override
    public void pause() {
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
    public void dispose() {
        batch.dispose();
        stage.dispose();
        skin.dispose();
        textureManager.dispose();
    }
}

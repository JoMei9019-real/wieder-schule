package de.Milad_Taromi.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.Milad_Taromi.Dialogue.DialoguePortrait;

public class TextureManager {

    private Skin skin;

    public TextButton.TextButtonStyle playStyle;
    public TextButton.TextButtonStyle optionsStyle;
    public TextButton.TextButtonStyle quitStyle;
    public Texture menuBackground;
    public Texture menuBackgroundHorror;
    public Texture menuBackgroundDefault;
    public Texture logoTexture;

    // Textbox (Dialogfenster)
    public Texture textboxTexture;
    public NinePatch textboxPatch;
    public NinePatchDrawable textboxDrawable;

    // Referenzen auf alle Button-Texturen, damit sie in dispose() freigegeben werden können
    private Texture playNormal, playHover, playPressed;
    private Texture optionsNormal, optionsHover, optionsPressed;
    private Texture exitNormal, exitHover, exitPressed;

    public Texture picture1;

    public Texture johnnyNeutral;
    public Texture johnnyHappy;
    public Texture johnnyAngry;

    public Texture miladNeutral;
    public Texture miladHappy;
    public Texture miladSad;

    public TextureManager() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        MenuScreenTexture_PLAY();
        MenuScreenTexture_OPTIONS();
        MenuScreenTexture_EXIT();
        Background_MENU();
        Background_DEFAULT();
        Background_HORROR();
        Logo();
        Textbox();
        Portraits();
    }

    public void Background_MENU(){
        menuBackground = new Texture("ui/menu/MenuBackground.png");
    }

    public void Background_HORROR(){
        menuBackgroundHorror = new Texture("ui/menu/Horror2.png");
    }

    public void Background_DEFAULT(){
        menuBackgroundDefault = new Texture("ui/menu/HintergrundID2.jpg");
    }

    public void Portraits() {

        picture1 = loadPortrait(
            "ui/game/portraits/Petus.png"
        );

        johnnyNeutral = loadPortrait(
            "ui/game/portraits/johnny_neutral.png"
        );

        johnnyHappy = loadPortrait(
            "ui/game/portraits/johnny_neutral.png"  //Vergiss nicht zu ändern bitte! BEI ALLEN!
        );

        johnnyAngry = loadPortrait(
            "ui/game/portraits/johnny_neutral.png"
        );

        miladNeutral = loadPortrait(
            "ui/game/portraits/johnny_neutral.png"
        );

        miladHappy = loadPortrait(
            "ui/game/portraits/johnny_neutral.png"
        );

        miladSad = loadPortrait(
            "ui/game/portraits/johnny_neutral.png"
        );
    }

    private Texture loadPortrait(String path) {
        Texture texture = new Texture(
            Gdx.files.internal(path)
        );

        texture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        return texture;
    }

    public Texture getPortraitTexture(
        DialoguePortrait portrait
    ) {
        switch (portrait) {

            case PICTURE1:
                return picture1;

            case JOHNNY_NEUTRAL:
                return johnnyNeutral;

            case JOHNNY_HAPPY:
                return johnnyHappy;

            case JOHNNY_ANGRY:
                return johnnyAngry;

            case MILAD_NEUTRAL:
                return miladNeutral;

            case MILAD_HAPPY:
                return miladHappy;

            case MILAD_SAD:
                return miladSad;

            case NONE:
            default:
                return null;
        }
    }

    private void disposeTexture(Texture texture) {
        if (texture != null) {
            texture.dispose();
        }
    }

    public void Logo(){
        logoTexture = new Texture(Gdx.files.internal("ui/menu/Logo.png"));
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void Textbox(){
        textboxTexture = new Texture(Gdx.files.internal("ui/game/Textbox.png"));
        textboxTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Rand-Werte an dein Textbox.png anpassen (links, rechts, oben, unten)
        textboxPatch = new NinePatch(textboxTexture, 12, 12, 12, 12);
        textboxDrawable = new NinePatchDrawable(textboxPatch);
    }

    public void MenuScreenTexture_PLAY(){
        playNormal = new Texture("ui/menu/PlayButton.png");
        playHover = new Texture("ui/menu/PlayButtonHighlight.png");
        playPressed = new Texture("ui/menu/PlayButtonHighlight.png");

        TextureRegionDrawable normal = new TextureRegionDrawable(new TextureRegion(playNormal));
        TextureRegionDrawable hover = new TextureRegionDrawable(new TextureRegion(playHover));
        TextureRegionDrawable pressed = new TextureRegionDrawable(new TextureRegion(playPressed));

        playStyle = new TextButton.TextButtonStyle();
        playStyle.up = normal;
        playStyle.over = hover;
        playStyle.down = pressed;
        playStyle.font = skin.getFont("font");
    }

    public void MenuScreenTexture_OPTIONS(){
        optionsNormal = new Texture("ui/menu/options.png");
        optionsHover = new Texture("ui/menu/options_high.png");
        optionsPressed = new Texture("ui/menu/options_high.png");

        TextureRegionDrawable normal_options = new TextureRegionDrawable(new TextureRegion(optionsNormal));
        TextureRegionDrawable hover_options = new TextureRegionDrawable(new TextureRegion(optionsHover));
        TextureRegionDrawable pressed_options = new TextureRegionDrawable(new TextureRegion(optionsPressed));

        optionsStyle = new TextButton.TextButtonStyle();
        optionsStyle.up = normal_options;
        optionsStyle.over = hover_options;
        optionsStyle.down = pressed_options;
        optionsStyle.font = skin.getFont("font");
    }

    public void MenuScreenTexture_EXIT(){
        exitNormal = new Texture("ui/menu/QuitButton.png");
        exitHover = new Texture("ui/menu/QuitButtonHighlight.png");
        exitPressed = new Texture("ui/menu/QuitButtonHighlight.png");

        TextureRegionDrawable normal_options = new TextureRegionDrawable(new TextureRegion(exitNormal));
        TextureRegionDrawable hover_options = new TextureRegionDrawable(new TextureRegion(exitHover));
        TextureRegionDrawable pressed_options = new TextureRegionDrawable(new TextureRegion(exitPressed));

        quitStyle = new TextButton.TextButtonStyle();
        quitStyle.up = normal_options;
        quitStyle.over = hover_options;
        quitStyle.down = pressed_options;
        quitStyle.font = skin.getFont("font");
    }

    public void dispose() {
        disposeTexture(menuBackground);
        disposeTexture(menuBackgroundHorror);
        disposeTexture(logoTexture);
        disposeTexture(textboxTexture);

        disposeTexture(picture1);

        disposeTexture(johnnyNeutral);
        disposeTexture(johnnyHappy);
        disposeTexture(johnnyAngry);

        disposeTexture(miladNeutral);
        disposeTexture(miladHappy);
        disposeTexture(miladSad);

        disposeTexture(playNormal);
        disposeTexture(playHover);
        disposeTexture(playPressed);

        disposeTexture(optionsNormal);
        disposeTexture(optionsHover);
        disposeTexture(optionsPressed);

        disposeTexture(exitNormal);
        disposeTexture(exitHover);
        disposeTexture(exitPressed);

        skin.dispose();
    }
}

package de.Milad_Taromi.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextureManager {

    private Skin skin;

    public TextButton.TextButtonStyle playStyle;
    public TextButton.TextButtonStyle optionsStyle;
    public TextButton.TextButtonStyle quitStyle;
    public Texture menuBackground;
    public Texture menuBackgroundHorror;
    public Texture logoTexture;

    // Textbox (Dialogfenster)
    public Texture textboxTexture;
    public NinePatch textboxPatch;
    public NinePatchDrawable textboxDrawable;

    // Referenzen auf alle Button-Texturen, damit sie in dispose() freigegeben werden können
    private Texture playNormal, playHover, playPressed;
    private Texture optionsNormal, optionsHover, optionsPressed;
    private Texture exitNormal, exitHover, exitPressed;

    public TextureManager() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        MenuScreenTexture_PLAY();
        MenuScreenTexture_OPTIONS();
        MenuScreenTexture_EXIT();
        Background_MENU();
        Background_HORROR();
        Logo();
        Textbox();
    }

    public void Background_MENU(){
        menuBackground = new Texture("ui/menu/MenuBackground.png");
    }

    public void Background_HORROR(){
        menuBackgroundHorror = new Texture("ui/menu/Horror2.png");
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

    public void dispose(){
        menuBackground.dispose();
        menuBackgroundHorror.dispose();
        logoTexture.dispose();
        textboxTexture.dispose();

        playNormal.dispose();
        playHover.dispose();
        playPressed.dispose();

        optionsNormal.dispose();
        optionsHover.dispose();
        optionsPressed.dispose();

        exitNormal.dispose();
        exitHover.dispose();
        exitPressed.dispose();

        skin.dispose();
    }
}

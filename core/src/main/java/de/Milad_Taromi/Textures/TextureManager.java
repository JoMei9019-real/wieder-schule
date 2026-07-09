package de.Milad_Taromi.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextureManager {

    private Skin skin;

    public TextButton.TextButtonStyle playStyle;
    public TextButton.TextButtonStyle optionsStyle;
    public TextButton.TextButtonStyle quitStyle;
    public Texture menuBackground;
    public Texture logoTexture;

    public TextureManager() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        MenuScreenTexture_PLAY();
        MenuScreenTexture_OPTIONS();
        MenuScreenTexture_EXIT();
        Background_MENU();
        Logo();
    }

    public void Background_MENU(){
        menuBackground = new Texture("ui/menu/MenuBackground.png");
    }

    public void Logo(){
        logoTexture = new Texture(Gdx.files.internal("ui/menu/Logo.png"));
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void MenuScreenTexture_PLAY(){
        Texture playNormal = new Texture("ui/menu/PlayButton.png");
        Texture playHover = new Texture("ui/menu/PlayButtonHighlight.png");
        Texture playPressed = new Texture("ui/menu/PlayButtonHighlight.png");

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
        Texture optionsNormal = new Texture("ui/menu/options.png");
        Texture optionsHover = new Texture("ui/menu/options_high.png");
        Texture optionsPressed = new Texture("ui/menu/options_high.png");

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
        Texture exitNormal = new Texture("ui/menu/QuitButton.png");
        Texture exitHover = new Texture("ui/menu/QuitButtonHighlight.png");
        Texture exitPressed = new Texture("ui/menu/QuitButtonHighlight.png");

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
        logoTexture.dispose();
        skin.dispose();

        playStyle.up.getClass();
    }
}

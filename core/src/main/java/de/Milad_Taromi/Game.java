package de.Milad_Taromi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.Milad_Taromi.Textures.TextureManager;
import de.Milad_Taromi.screens.MenuScreen;
import de.Milad_Taromi.screens.MenuScreen_Beta;

public class Game extends com.badlogic.gdx.Game {

    public SpriteBatch batch;
    public TextureManager textures;

    @Override
    public void create() {
        batch = new SpriteBatch();
        textures = new TextureManager();
        this.setScreen(new MenuScreen(this));
    }

    public static String getTitle(){
        String title = "[VALORIA] | Developer Alpha 0.3.1 | ";
        return title;
    }
}

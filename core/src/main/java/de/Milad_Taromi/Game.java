package de.Milad_Taromi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.Milad_Taromi.Managers.TextureManager;
import de.Milad_Taromi.screens.MenuScreen;

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
        String title = "[VALORIA] | Developer Alpha 0.5 | ";
        return title;
    }
}

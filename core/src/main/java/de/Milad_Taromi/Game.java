package de.Milad_Taromi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.Milad_Taromi.Managers.TextureManager;
import de.Milad_Taromi.screens.MenuScreen;

///Das ist die Hauptklasse des Spiels und wird beim Start als erstes ausgeführt
/// und bereitet einiges vor, was das Spiel zum starten benötigt.
public class Game extends com.badlogic.gdx.Game {

    /// Ein SpriteBatch wird verwendet, um Bilder und Grafiken möglichst effizient auf den Bildschirm zu zeichnen.
    public SpriteBatch batch;
    /// Dieser lädt und verwaltet alle Bilder (Texturen), damit sie nicht mehrfach geladen werden müssen.
    public TextureManager textures;

    @Override
    public void create() {      /// Wird automatisch aufgerufen durch die Game-Engine
        batch = new SpriteBatch();      /// Erstellt den SpriteBatch.
        textures = new TextureManager();        /// Erstellt den TextureManager.
        this.setScreen(new MenuScreen(this));   /// Öffnet den ersten Bildschirm des Spiels.
    }


    /// Speichert den Fenstertitel in einer String-Variable.
    /// Anschließend wird dieser Titel zurückgegeben und als Fenster-Titel gesetzt.
    public static String getTitle(){
        String title = "[VALORIA] | Developer Beta 0.7.3 | ";
        return title;
    }
}

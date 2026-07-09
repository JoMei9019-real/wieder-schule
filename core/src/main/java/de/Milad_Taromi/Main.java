package de.Milad_Taromi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.Milad_Taromi.screens.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static String title;
    private Stage stage;
    private Skin skin;


    ////    DIESE KLASSE IST NICHT MEHR IN BENUZUNG UND DIENT TESTZWECKEN!!


    @Override
    public void create() {
        stage = new Stage(new FitViewport(640, 480));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Game game = new Game();
        game.setScreen(new MenuScreen(game));


        ////////// DIES IST EINE TOTE KLASSE - INSTANT AUFRUF VON MENUSCREEN
        ///////// REST IST NUR TEST-CODE UND WIRD NICHT MEHR BENUTZT UND BENÖTIGT!

        //window1(); //Open the test GUI
    }

    public void window1(){
        Window window = new Window("Window 1", skin, "border");
        window.defaults().pad(4f);
        window.add("Milad Demo V1 (DEV) - Screen TEST-1").row();
        final TextButton button = new TextButton("Click me!", skin);
        button.pad(8f);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                //button.setText("Clicked.");
                System.out.println("[DEBUG] TEST01: Clicked the Button!");
                window.remove();    //remove it!
                window2(); //new Window2
            }
        });
        window.add(button);
        window.pack();
        // We round the window position to avoid awkward half-pixel artifacts.
        // Casting using (int) would also work.
        window.setPosition(MathUtils.roundPositive(stage.getWidth() / 2f - window.getWidth() / 2f),
            MathUtils.roundPositive(stage.getHeight() / 2f - window.getHeight() / 2f));
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window);

        Gdx.input.setInputProcessor(stage);
    }

    private String getTitle(){
        title = "GAME | Developer-Alpha 1.0";
        return title;
    }

    public void window2() {
        Window window2 = new Window("Window 2", skin, "border");
        window2.defaults().pad(4f);
        window2.add("Milad Demo V1 (DEV) - Screen TEST-2").row();
        window2.add("[TESTING DISABLED BUTTON & MORE TEXT]").row();
        final TextButton button2 = new TextButton("DISABLED", skin);
        final TextButton button3 = new TextButton("ENABLED", skin);
        button3.pad(8f);
        button3.setDisabled(false);
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               if (button3.isChecked()){
                   window2.remove();
                   window3();
               }
            }
        });
        button2.pad(8f);
        button2.setDisabled(false);  //DISABLE THE BUTTON! (=true)
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                //button2.setText("Clicked.");
                if(button2.isChecked()){    //ONLY IF ENABLED THEN
                    button2.remove();
                }
            }

        });
        window2.add(button2);
        window2.add(button3);
        window2.pack();
        window2.setPosition(MathUtils.roundPositive(stage.getWidth() / 2f - window2.getWidth() / 2f),
            MathUtils.roundPositive(stage.getHeight() / 2f - window2.getHeight() / 2f));
        window2.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window2);
        Gdx.input.setInputProcessor(stage);
    }

    public void window3() {
        Window window3 = new Window("Window 3", skin, "border");
        window3.defaults().pad(4f);
        window3.add("Milad Demo V1 (DEV) - Screen TEST-3").row();
        window3.add("[New Button render!]").row();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        TextButton button = new TextButton("Click Me!" , skin);
        table.add(button).expand().center().height(800).width(100);
        TextButton button1 = new TextButton("Click Me!!", skin);
        table.add(button1).expand().center().height(800).width(100);
        button1.pad(10f);
        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(button.isChecked()){
                    window3.remove();
                }
            }
        });
        button.pad(8f);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int check2 = 0;
                if (button.isChecked() && check2 >= 3){
                    button.setText("Nice!");
                    check2++;
                    button.setSize(800,800);
                }else{
                    button.setText("REICHT!");
                    button.setDisabled(true);
                }
            }
        });
        table.add(button).expand().center().width(300).height(100);

        window3.add(button);
        window3.add(button1).center().expand();
        window3.pack();
        window3.setPosition(MathUtils.roundPositive(stage.getWidth() / 2f - window3.getWidth() / 2f),
            MathUtils.roundPositive(stage.getHeight() / 2f - window3.getHeight() / 2f));
        window3.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window3);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

package com.sajko.skywalk.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sajko.skywalk.Skywalk;

/**
 * Created by Sajko on 23.5.2016.
 */
public class Hud implements Disposable {

    public Stage stage;
    public Viewport viewport;

    private static Integer score;
    private static Integer heartRate;

    private Label jackLabel;
    private Label heartLabel;
    private static Label scoreLabel;
    private static Label heartRateLabel;

    public Hud(SpriteBatch sb){

        score = 0;
        heartRate = 0;

        viewport = new FitViewport(Skywalk.V_WIDTH, Skywalk.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        jackLabel = new Label("JACK", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        heartLabel = new Label("HEART RATE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        heartRateLabel = new Label(String.format("%03d", heartRate), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        table.add(jackLabel).expandX().padTop(10);
        table.add(heartLabel).expandX();
        table.row();
        table.add(scoreLabel).expandX();
        table.add(heartRateLabel).expandX();

        stage.addActor(table);


    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public static void addHeartRate(int value){
        heartRate = value;
        heartRateLabel.setText(String.format("%03d", heartRate));
    }

    public void update(float dt){

    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}

package com.shooter.game.screens;

import com.badlogic.gdx.Gdx;
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
import com.shooter.game.BetaShooter;
import com.shooter.game.sprites.Player;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private float timeCount;

    private Label scoreLBL;
    private Label positionLBL;
    private Label scoreT_LBL;
    private Label playerT_LBL;

    public Hud(SpriteBatch batch) {
        timeCount = 0;

        viewport = new FitViewport(BetaShooter.V_WIDTH, BetaShooter.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        playerT_LBL = new Label("Player", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreT_LBL = new Label("Debug", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        positionLBL = new Label("X: null, Y: null", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLBL = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(playerT_LBL).expandX().padTop(10);
        table.add(scoreT_LBL).expandX().padTop(10);
        table.row();
        table.add(positionLBL).expandX().padTop(10);
        table.add(scoreLBL).expandX().padTop(10).center();


        stage.addActor(table);

    }

    public void update(){
        scoreLBL.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}


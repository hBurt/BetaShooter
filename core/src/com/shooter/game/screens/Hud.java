package com.shooter.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.game.BetaShooter;
import com.shooter.game.sprites.Player;
import com.shooter.game.sprites.util.Bullet;

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

    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonJump;
    private Button buttonFire;

    private TextureAtlas atlas;

    private int bulletCount;

    public boolean pressLeft = false;
    public boolean pressRight = false;
    public boolean pressJump = false;
    public boolean pressFire = false;

    public Hud(SpriteBatch batch, BetaShooter game) {
        timeCount = 0;
        bulletCount = 0;

        viewport = new FitViewport(BetaShooter.V_WIDTH, BetaShooter.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        atlas = game.getAsm().get("ui/ui.atlas", TextureAtlas.class);
        TextureRegionDrawable arrow_left = new TextureRegionDrawable(atlas.findRegion("arrow_left"));
        TextureRegionDrawable arrow_right = new TextureRegionDrawable(atlas.findRegion("arrow_right"));
        TextureRegionDrawable arrow_jump = new TextureRegionDrawable(atlas.findRegion("arrow_jump"));
        TextureRegionDrawable fire = new TextureRegionDrawable(atlas.findRegion("fire"));

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
        table.debug();

        stage.addActor(table);



        buttonLeft = new Button(arrow_left);
        buttonRight = new Button(arrow_right);
        buttonJump = new Button(arrow_jump);
        buttonFire = new Button(fire);

        buttonLeft.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press left");
                pressLeft = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press left up");
                pressLeft = false;
            }
        });
        buttonRight.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press right");
                pressRight = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press right up");
                pressRight = false;
            }
        });
        buttonJump.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press jump");
                pressJump = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press jump released");
                pressJump = false;
            }
        });
        buttonFire.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press fire");
                pressFire = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(this.getClass().getName(), "Button press fire released");
                pressFire = false;
            }
        });

        //Input buttons
        Table controllLeft = new Table();
        controllLeft.bottom().left().padLeft(20).padBottom(20);
        controllLeft.add(buttonLeft);
        controllLeft.add(buttonRight).padLeft(20);
        controllLeft.add(buttonFire).padLeft(450);
        controllLeft.add(buttonJump).padLeft(20);

        controllLeft.debug();

        stage.addActor(controllLeft);


    }

    public void update(Player player){
        scoreLBL.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + "\nLRJF: " + pressLeft + pressRight + pressJump + pressFire);
        if(player.bullet.size > 0){
            bulletCount = player.bullet.size;
            positionLBL.setText("X: " + player.b2Body.getPosition().x + " Y: " + player.b2Body.getPosition().y + "\n"
                + "Bullet Count: " + bulletCount);
        } else {
            positionLBL.setText("X: " + player.b2Body.getPosition().x + " Y: " + player.b2Body.getPosition().y + "\n");
        }

    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}


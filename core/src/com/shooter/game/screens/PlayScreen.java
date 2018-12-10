package com.shooter.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.game.BetaShooter;
import com.shooter.game.sprites.Player;
import com.shooter.game.tools.B2WorldCreator;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public class PlayScreen implements Screen {

    //Reference to our game
    private BetaShooter game;
    private TextureAtlas atlas;

    private OrthographicCamera gameCamera;
    private Viewport gamePort;
    private Hud hud;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    private double accumulator;
    private double currentTime;

    //Box2D constants
    private final float timeStep = 1.0f / 60.0f;
    private final int   velocityIterations = 6;
    private final int   positionIterations = 2;
    private final float gravity = -10f;

    private Player player;

    public PlayScreen(BetaShooter game){
        this.game = game;

        //Camera that follows player
        gameCamera = new OrthographicCamera();

        //Create a FitViewport to maintain games aspect ratio
        gamePort = new FitViewport(800 / BetaShooter.PPM, 480 / BetaShooter.PPM, gameCamera);

        //Create hud for score / health / etc..
        hud = new Hud(game.getBatch());

        //Get pre-loaded map from our asset manager
        map = game.getAsm().get("maps/untitled.tmx", TiledMap.class);

        //Render the map to the game world
        renderer = new OrthogonalTiledMapRenderer(map, 1 / BetaShooter.PPM);

        //Re-position the camera
        gameCamera.position.set(gamePort.getWorldWidth() / 2f, gamePort.getWorldWidth() / 2f, 0);

        //Create the box2d world with a set gravity
        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(map, world);

        player = new Player(world, game);

    }

    @Override
    public void show() {

    }

    private void handleInput(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){  // Move up
           player.b2Body.applyLinearImpulse(new Vector2(0, 3f), player.b2Body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2Body.getLinearVelocity().x >= -3) {  // Move up
            player.b2Body.applyLinearImpulse(new Vector2(-0.3f, 0), player.b2Body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2Body.getLinearVelocity().x <= 3){  // Move up
            player.b2Body.applyLinearImpulse(new Vector2(0.3f, 0), player.b2Body.getWorldCenter(), true);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)){
            gameCamera.zoom -= 0.01f;
        }

    }

    public void update(float delta){
        //Handle user input first
        handleInput(delta);

        //fixed timestep
        //max frame rate
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep && 5 > timeStep) {
            world.step(timeStep, velocityIterations, positionIterations);
            accumulator -= timeStep;
        }

        player.update(delta);

        gameCamera.position.x = player.b2Body.getPosition().x;
        gameCamera.position.y = player.b2Body.getPosition().y;

        //Update the camera
        gameCamera.update();

        //Only render what's in the cameras view
        renderer.setView(gameCamera);
    }


    @Override
    public void render(float delta) {
        update(delta);

        //Clear each color bit
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set each bit to 'black'
        Gdx.gl.glClearColor(0, 0, 0, 1);


        //Render the game map
        renderer.render();

        //Render box2d debug lines
        b2dr.render(world, gameCamera.combined);

        game.getBatch().setProjectionMatrix(gameCamera.combined);
        game.getBatch().begin();
        player.draw(game.getBatch());
        game.getBatch().end();

        //update the hud
        hud.update();

        //Set batch to draw what hud camera sees
        game.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);

        //Draw the hud
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        player.dispose();
    }
}

package com.shooter.game.screens;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooter.game.BetaShooter;
import com.shooter.game.managers.PhysicsEntityManager;
import com.shooter.game.sprites.Player;
import com.shooter.game.sprites.enemies.Zombie;
import com.shooter.game.sprites.util.PhysicsActor;
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
    //private RayHandler ray;
    private PointLight light;
    private double accumulator;

    private double currentTime;
    //Box2D constants

    private final float timeStep = 1.0f / 60.0f;
    private final int   velocityIterations = 6;
    private final int   positionIterations = 2;
    private final float gravity = -32f;
    private Player player;
    private Array<Vector2> zombieSpawns;

    public PlayScreen(BetaShooter game){
        this.game = game;

        //Camera that follows player
        gameCamera = new OrthographicCamera();

        //Create a FitViewport to maintain games aspect ratio
        gamePort = new FitViewport(800 / BetaShooter.PPM, 480 / BetaShooter.PPM, gameCamera);

        //Create hud for score / health / etc..
        hud = new Hud(game.getBatch(), game);

        //Get pre-loaded map from our asset manager
        map = game.getAsm().get("maps/intro.tmx", TiledMap.class);

        //Render the map to the game world
        renderer = new OrthogonalTiledMapRenderer(map, 1 / BetaShooter.PPM);

        //Re-position the camera
        gameCamera.position.set(gamePort.getWorldWidth() / 2f, gamePort.getWorldWidth() / 2f, 0);


        //Create the box2d world with a set gravity
        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(map, world);



        Vector2 pointPlayer = getMapSpawnPointWithPPM("player");

        player = new Player((int) pointPlayer.x, (int) pointPlayer.y,
                world, game, this, hud);

        zombieSpawns = getZombieSpawnPoints();

        for (Vector2 point : zombieSpawns){
            PhysicsEntityManager.setToUpdate(new Zombie((int) point.x, (int) point.y, world, game.getAsm()));
        }


        //ray = new RayHandler(world);

        //light = new PointLight(ray, 1000, Color.OLIVE.set(0.09f, 0.145f, 0.145f, 1f), 1000, 1, 2);
        //light.attachToBody(player.b2Body);
        gameCamera.zoom -= 0.4f;

        createContactListener();
    }

    //Get all the zombie spawn points as a vector from the map
    private Array<Vector2> getZombieSpawnPoints(){
        //Initiate new vector array to hold results
        Array<Vector2> vector2Array = new Array<Vector2>();

        //Loop through each map object
        for(MapObject mo : getSpawnPoints()){

            //If the map object is not null && the name == zombie || zombie1
            if(mo != null && (mo.getName().matches("zombie") || mo.getName().matches("zombie1")) ) {
                //System.out.println( mo.getProperties().get("name").toString() );
                int spawnOriginX = (int) Double.parseDouble(mo.getProperties().get("x").toString());
                int spawnOriginY = (int) Double.parseDouble(mo.getProperties().get("y").toString());

                vector2Array.add(new Vector2((int) (spawnOriginX / BetaShooter.PPM), (int) (spawnOriginY / BetaShooter.PPM)));
                System.out.println("Spawn here");

            }
        }
        return vector2Array;
    }

    //
    private Vector2 getMapSpawnPointWithPPM(String spawnPoint){
        MapObject mapSpawnPoint = getSpawnPoints().get(spawnPoint);
        int spawnOriginX = (int) Double.parseDouble(mapSpawnPoint.getProperties().get("x").toString());
        int spawnOriginY = (int) Double.parseDouble(mapSpawnPoint.getProperties().get("y").toString());

        return new Vector2((int) (spawnOriginX / BetaShooter.PPM), (int) (spawnOriginY / BetaShooter.PPM));
    }

    //Return all objects from object layer 'spawns'
    private MapObjects getSpawnPoints(){
        return map.getLayers().get("spawns").getObjects();
    }

    @Override
    public void show() {

    }

    private void handleInput(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            handleMoveLeft();
        } else if(hud.pressLeft){
            handleMoveLeft();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            handleMoveRight();
        } else if(hud.pressRight){
            handleMoveRight();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            handleMoveUp();
        } else if (hud.pressJump) {
            handleMoveUp();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if(!player.isRunningRight()) {
                player.createNewBullet(player.getPositionCenter().x - 0.6f, player.getPositionCenter().y - 0.2f, "left");
            } else {
                player.createNewBullet(player.getPositionCenter().x +  0.6f, player.getPositionCenter().y - 0.2f, "right");
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            Gdx.app.log(this.getClass().getName(), "pressed 1: Weapon change to SHOTGUN" );
            player.applyWeaponType(Player.WeaponType.SHOTGUN);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            Gdx.app.log(this.getClass().getName(), "pressed 2: Weapon change to ASSULT_RIFLE");
            player.applyWeaponType(Player.WeaponType.ASSULT_RIFLE);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            Gdx.app.log(this.getClass().getName(), "pressed 3: Weapon change to SNIPER");
            player.applyWeaponType(Player.WeaponType.SNIPER);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)){
            gameCamera.zoom -= 0.01f;
        }
    }

    private void handleMoveLeft(){
        if(player.b2Body.getLinearVelocity().x >= -6) {  // Move left
            if(player.b2Body.getLinearVelocity().x > 0){
                player.b2Body.setLinearVelocity(0, player.b2Body.getLinearVelocity().y);
            } else {
                player.b2Body.applyLinearImpulse(new Vector2(-0.9f, 0), player.b2Body.getWorldCenter(), true);
            }

        }
    }
    private void handleMoveRight(){
        if(player.b2Body.getLinearVelocity().x <= 6){  // Move right
            if(player.b2Body.getLinearVelocity().x < 0){
                player.b2Body.setLinearVelocity(0, player.b2Body.getLinearVelocity().y);
            } else {
                player.b2Body.applyLinearImpulse(new Vector2(0.9f, 0), player.b2Body.getWorldCenter(), true);
            }
        }
    }
    private void handleMoveUp(){
        if(player.isGrounded()) {
            player.b2Body.applyLinearImpulse(new Vector2(0, 8f), player.b2Body.getWorldCenter(), true);
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


        //Update the PEM
        PhysicsEntityManager.update(world, delta);

        //Set play texture to player position &&
        //update the region to the proper Texture Region based on the statetimer
        player.update(delta);

        //Set camera x & y position to player position
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

        //Draw the ray
        //ray.updateAndRender();
        //ray.setCombinedMatrix(gameCamera.combined);

        //Set the batch projection matrix to our game camera
        game.getBatch().setProjectionMatrix(gameCamera.combined);

        //Draw the player
        player.render(game.getBatch());

        //Draw all entities to the game world
        PhysicsEntityManager.draw(game.getBatch());

        //update the hud
        hud.update(player, delta);

        //Set batch to draw what hud camera sees
        game.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);

        //Draw the hud
        hud.stage.draw();

    }

    private void handleColision(Contact contact, boolean begin){

        //Get the two fixture that are colliding
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Create a PhysicsActor from each fixtures user data
        PhysicsActor actorA = (PhysicsActor) fixA.getBody().getUserData();
        PhysicsActor actorB = (PhysicsActor) fixB.getBody().getUserData();

        //Check to make sure both fixtures are not null
        if(actorA != null && actorB != null){
            //Check if the contact is started or ended
            if(begin) { //Start contact
                actorA.handleCollision(actorB);
                actorB.handleCollision(actorA);
            } else {    //End contact
                actorA.handleUnCollide(actorB);
                actorB.handleUnCollide(actorA);
            }
        }
    }


    //Handle world collision
    public void createContactListener(){
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                handleColision(contact, true);

            }

            @Override
            public void endContact(Contact contact) {
                handleColision(contact, false);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
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

    public TiledMap getMap() {
        return map;
    }
}

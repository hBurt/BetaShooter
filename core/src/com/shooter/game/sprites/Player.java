package com.shooter.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.shooter.game.BetaShooter;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public class Player extends Sprite implements Disposable {

    private BetaShooter game;

    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2Body;
    private BodyDef bdef;

    private TextureAtlas playerAndEnemies;

    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private TextureRegion playerStand;
    private float stateTimer;
    private boolean runningRight;


    CircleShape shape;


    private Vector2 position = new Vector2(7, 5);

    public Player(World world, BetaShooter game) {
        this.world = world;
        this.game = game;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        /********Player Animation********/
        //Retrieve the player and enemy atlas
        playerAndEnemies = game.getAsm().get("characters/player_and_enemies.atlas", TextureAtlas.class);

        //Create an array to briefly hold our animations
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Add our run frames to the animation
        //frames.add(playerAndEnemies.findRegion("ppd-1"));
        frames.add(playerAndEnemies.findRegion("ppd-6"));
        frames.add(playerAndEnemies.findRegion("ppd-7"));
        frames.add(playerAndEnemies.findRegion("ppd-8"));
        frames.add(playerAndEnemies.findRegion("ppd-9"));

        //Set the run animationto the frames from our array
        playerRun = new Animation<TextureRegion>(0.1f, frames);

        //Clear the array
        frames.clear();

        //Add our run frames to the animation
        frames.add(playerAndEnemies.findRegion("ppd-6"));
        frames.add(playerAndEnemies.findRegion("ppd-7"));

        //Set the run animationto the frames from our array
        playerJump = new Animation<TextureRegion>(0.1f, frames);

        //Clear the array
        frames.clear();

        playerStand = playerAndEnemies.findRegion("ppd-0");
        /******End Player Animation*******/

        definePLayer();

        setBounds(0,0,32 / BetaShooter.PPM, 32 / BetaShooter.PPM);
        setRegion(playerStand);
    }

    public void definePLayer(){
        bdef = new BodyDef();
        bdef.position.set((position.x * 32) / BetaShooter.PPM, (position.y * 32) / BetaShooter.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        shape = new CircleShape();
        shape.setRadius(15f / BetaShooter.PPM);

        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }

    public void update(float delta){
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        if((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        } else if((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;

    }

    public State getState(){
        if(b2Body.getLinearVelocity().y > 0 || b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING){
            return State.JUMPING;
        } else if(b2Body.getLinearVelocity().y < 0){
            return State.FALLING;
        } else if(b2Body.getLinearVelocity().x != 0){
            return State.RUNNING;
        } else {
            return State.STANDING;
        }

    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}

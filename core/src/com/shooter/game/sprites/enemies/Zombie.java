package com.shooter.game.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.shooter.game.BetaShooter;
import com.shooter.game.managers.PhysicsEntityManager;
import com.shooter.game.sprites.util.Bullet;
import com.shooter.game.sprites.util.PhysicsActor;

/**
 * Created by: Harrison on 13 Dec 2018
 */
public class Zombie extends PhysicsActor {

    public enum State { STANDING, WALING }

    //Initiate states
    public State currentState;
    public State previousState;

    //Box2d Init
    public World world;
    public Body b2Body;

    //Textures / anims
    private TextureAtlas playerAndEnemies;

    private TextureRegion entityStand;

    private boolean runningRight;

    private int health;

    private boolean hasBeenDeleted = false;

    public Zombie(int x, int y, World world, AssetManager asm) {
        super(world);
        currentState = State.STANDING;
        previousState = State.STANDING;

        //Retrieve the player and enemy atlas
        playerAndEnemies = asm.get("characters/player_and_enemies.atlas", TextureAtlas.class);

        entityStand = playerAndEnemies.findRegion("zombie");

        /******Set player starting position******/
        b2Body = getPlayerBody(x, y, 15);   //3, 10
        b2body.setUserData(this);
        /****************************************/
        setBounds(0,0,32 / BetaShooter.PPM, 32 / BetaShooter.PPM);
        setRegion(entityStand);

        //Get the entities MassData
        MassData massData = b2Body.getMassData();

        //Set mass in mass data
        massData.mass = 500f;

        //Apply the mass to our entity
        b2Body.setMassData(massData);

        health = 100;


    }

    public void update(){
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(entityStand);
        if(health < 1){
            Gdx.app.log(this.getClass().getName(), "Health: DEAD");
            PhysicsEntityManager.setToDestroy(this);
        }

    }

    public void handleCollision(PhysicsActor actor){
        if(actor instanceof Bullet){
            //Previous Bug : #1
            //Type: Fatal; Crashes game with "Expression: m_bodyCount > 0"
            //Fix: Set flag checkDeleted physics actors to see if they have been deleted before.
           PhysicsEntityManager.setToDestroy(actor);
            Gdx.app.log(this.getClass().getName(), "Bullet colliding:  bullet");
            health -= 25;
        }

    }

    public boolean checkDeleted() {
        return hasBeenDeleted;
    }

    public void flagAsDeleted() {
        this.hasBeenDeleted = true;
    }

}

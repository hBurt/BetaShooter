package com.shooter.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.BetaShooter;
import com.shooter.game.managers.PhysicsEntityManager;
import com.shooter.game.screens.PlayScreen;
import com.shooter.game.sprites.util.Bullet;
import com.shooter.game.sprites.util.PhysicsActor;
import com.shooter.game.sprites.util.Weapon;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public class Player extends PhysicsActor {

    //Instance of our game
    private BetaShooter game;
    private PlayScreen screen;

    //Create the states for out players animation
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
    public enum WeaponType {SHOTGUN, ASSULT_RIFLE, SNIPER, GUN_4, GUN_5, GUN_6, GUN_7, GUN_8 }

    //Initiate states
    public State currentState;
    public State previousState;

    public WeaponType weaponType;
    public Weapon weapon;

    //Box2d Init
    public World world;
    public Body b2Body;

    //Textures / anims
    private TextureAtlas playerAndEnemies;
    private TextureAtlas weaponsWithBullets;

    private TextureRegion weaponTexture;
    private TextureRegionDrawable weaponDrawable;
    private int weaponUnitHolderPosition;

    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private TextureRegion playerStand;
    private TextureRegionDrawable healthBarDraw;
    private TextureRegionDrawable healthBarBorder;
    private float stateTimer;

    private boolean runningRight;

    private float health;
    public Player(World world, BetaShooter game, PlayScreen screen) {
        super(world);
        this.world = world;
        this.game = game;
        this.screen = screen;
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

        //Set the run animation the frames from our array
        playerJump = new Animation<TextureRegion>(0.1f, frames);

        //Clear the array
        frames.clear();

        playerStand = playerAndEnemies.findRegion("ppd-0");
        /******End Player Animation*******/

        getPlayerBody(3, 4, 15);
        b2Body = super.b2body;
        b2Body.setUserData(this);

        setBounds(0,0,32 / BetaShooter.PPM, 32 / BetaShooter.PPM);
        setRegion(playerStand);

        //Health Bar
        healthBarDraw = new TextureRegionDrawable(playerAndEnemies.findRegion("healthBar"));
        healthBarBorder = new TextureRegionDrawable(playerAndEnemies.findRegion("healthBarBorder"));
        health = 100;

        //Weapons

        //Load the atlas to get the textures
        weaponsWithBullets = game.getAsm().get("characters/weapons_with_bullets.atlas", TextureAtlas.class);

        //Set weapon type
        weaponType = WeaponType.ASSULT_RIFLE;

        //Do the weapon logic
        applyWeaponType(weaponType);
        weaponUnitHolderPosition = weapon.getRightX();
    }

    public void applyWeaponType(WeaponType weaponType) {
        if (weaponType == WeaponType.SHOTGUN) {
            weaponTexture = weaponsWithBullets.findRegion("weapon_d-0");
            weapon = new Weapon(weaponType, 18, 7, 15, 25, 17);
        } else if (weaponType == WeaponType.ASSULT_RIFLE) {
            weaponTexture = weaponsWithBullets.findRegion("weapon_d-2");
            weapon = new Weapon(weaponType, 18, 7, 15, 25, 17);
        } else if (weaponType == WeaponType.SNIPER) {
            weaponTexture = weaponsWithBullets.findRegion("weapon_d-5");
            weapon = new Weapon(weaponType, 18, 7, 15, 25, 17);
        }
        weaponDrawable = new TextureRegionDrawable(weaponTexture);

    }

    public void createNewBullet(float x, float y, String direction){
        PhysicsEntityManager.setToUpdate(new Bullet(game, world, this, weaponType, x, y, direction));
    }

    public void update(float delta){

        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public void render(SpriteBatch batch){
        int avg = (int) (0.32f * health);
        healthBarDraw.getRegion().setRegionWidth(avg);
        batch.begin();
        draw(batch);
        healthBarDraw.draw(batch,
                b2Body.getPosition().x - 16 / BetaShooter.PPM ,
                b2Body.getPosition().y + 20 / BetaShooter.PPM,
                healthBarDraw.getRegion().getRegionWidth() / BetaShooter.PPM, 4 / BetaShooter.PPM);

        healthBarBorder.draw(batch,
                b2Body.getPosition().x - 16 / BetaShooter.PPM ,
                b2Body.getPosition().y + 20 / BetaShooter.PPM,
                32 / BetaShooter.PPM, 4 / BetaShooter.PPM);

        //Weapon Stuff
        weaponDrawable.draw(batch,
                b2Body.getPosition().x - weaponUnitHolderPosition / BetaShooter.PPM,
                b2Body.getPosition().y - weapon.getY() / BetaShooter.PPM,
                weapon.getWidth() / BetaShooter.PPM, weapon.getHeight() / BetaShooter.PPM);

        batch.end();
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

        if((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){ //left
            region.flip(true, false);
            if(!weaponDrawable.getRegion().isFlipX()) {
                weaponUnitHolderPosition = weapon.getLeftX();
                weaponDrawable.getRegion().flip(true, false);
            }
            runningRight = false;
        } else if((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            if(weaponDrawable.getRegion().isFlipX()) {
                weaponUnitHolderPosition = weapon.getRightX();
                weaponDrawable.getRegion().flip(true, false);
            }
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

    public void handleCollision(PhysicsActor actor){
        if(actor instanceof Player){
            Gdx.app.log(this.getClass().getName(), "Player touched player");
        }

        if(actor instanceof Bullet){
            Gdx.app.log(this.getClass().getName(), "Player touched bullets");
            //health -= 10;
            //PhysicsEntityManager.setToDestroy(actor);
        }
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    public Vector2 getPositionCenter() {
        return new Vector2(getX() + 0.5f, getY() + 0.5f);
    }


    public boolean isRunningRight() {
        return runningRight;
    }
}

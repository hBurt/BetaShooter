package com.shooter.game.sprites.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.shooter.game.BetaShooter;
import com.shooter.game.managers.PhysicsEntityManager;
import com.shooter.game.sprites.Player;
import com.shooter.game.sprites.Player.WeaponType;

/**
 * Created by: Harrison on 11 Dec 2018
 */
public class Bullet extends PhysicsActor {

    private BetaShooter game;
    private TextureAtlas textureAtlas;
    private TextureRegion texture;
    private WeaponType weaponType;
    private Player player;

    //Box2d Init
    private World world;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    private boolean on = true;


    public Bullet(BetaShooter game, World world, Player player, WeaponType weaponType, float x, float y, String direction) {
        super(world);

        this.game = game;
        this.world = world;
        this.player = player;
        this.weaponType = weaponType;

        b2body = getBulletBody(x, y, 3f, direction);
        b2body.setUserData(this);

        setBounds(0,0,6 / BetaShooter.PPM, 6 / BetaShooter.PPM);
        textureAtlas = game.getAsm().get("characters/weapons_with_bullets.atlas", TextureAtlas.class);
        texture = textureAtlas.findRegion("bullet_pink");
        setRegion(texture);
    }


    public void update() {
        //Sets the position of the TextureRegion relative to the bullets body
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void handleCollision(PhysicsActor actor){
        if(actor instanceof Bullet){

            //TODO  Game Perriodically crashes
            Gdx.app.log(this.getClass().getName(), "b | b");
            PhysicsEntityManager.setToDestroy(actor);
            Gdx.app.log(this.getClass().getName(), "c | c");
        }
    }


    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    public Body getBody(){
        return this.b2body;
    }

}

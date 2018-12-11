package com.shooter.game.sprites.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Disposable;
import com.shooter.game.BetaShooter;
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


    public Bullet(BetaShooter game, World world, Player player, WeaponType weaponType, float x, float y, String direction) {
        super(world);
        this.game = game;
        this.world = world;
        this.player = player;
        this.weaponType = weaponType;

        getBulletBody(x, y, 3f, direction);
        b2body.setUserData(this);

        setBounds(0,0,6 / BetaShooter.PPM, 6 / BetaShooter.PPM);
        textureAtlas = game.getAsm().get("characters/weapons_with_bullets.atlas", TextureAtlas.class);
        texture = textureAtlas.findRegion("bullet_pink");
        setRegion(texture);
    }


    public void handleCollision(PhysicsActor actor){
        if(actor instanceof Player){
            Gdx.app.log(this.getClass().getName(), "Bullet touched player");

        }

    }


    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    public void render(SpriteBatch batch){
        batch.begin();
        draw(batch);
        batch.end();
    }

    public void update(){
        //Sets the position of the TextureRegion relative to the bullets body
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}

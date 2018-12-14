package com.shooter.game.sprites.util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.shooter.game.BetaShooter;

/**
 * Created by: Harrison on 11 Dec 2018
 */
public abstract class PhysicsActor extends Sprite implements Disposable {
    public Body getB2body() {
        return b2body;
    }

    protected Body b2body;
    protected World world;

    public PhysicsActor(World world) {
        this.world = world;
    }

    public Body getPlayerBody(float x, float y, float radius){
        createBody(x,y,radius, false);
        b2body.setGravityScale(2.5f);
        return b2body;
    }

    public Body getBulletBody(float x, float y, float radius, String direction){
        createBody(x,y,radius, false);
        if(direction.matches("left")) {
            b2body.applyLinearImpulse(new Vector2(-24f, 0), b2body.getWorldCenter(), true);
        } else if(direction.matches("right")){
            b2body.applyLinearImpulse(new Vector2(24f, 0), b2body.getWorldCenter(), true);
        }
        b2body.setGravityScale(0);
        return b2body;
    }

    public Body createWallBody(float x, float y, float width, float height){
        BodyDef bdef = new BodyDef();
        bdef.position.set(0, 0);
        //Gdx.app.log(this.getClass().getName(), "x,y: " + (x / BetaShooter.PPM) + "," + (y / BetaShooter.PPM));
        bdef.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(x / BetaShooter.PPM, y / BetaShooter.PPM);
        vertices[1] = new Vector2( (x + width) / BetaShooter.PPM, y / BetaShooter.PPM);
        vertices[2] = new Vector2( (x + width) / BetaShooter.PPM, (y + height) / BetaShooter.PPM);
        vertices[3] = new Vector2( x / BetaShooter.PPM, (y + height) / BetaShooter.PPM);
        shape.set(vertices);

        fdef.isSensor = true;
        fdef.shape = shape;

        b2body.createFixture(fdef);
        b2body.setGravityScale(0);
        return b2body;
    }

    private Body createBody(float x, float y, float radius, boolean isSensor){
        BodyDef bdef = new BodyDef();
        bdef.position.set((x * 32) / BetaShooter.PPM, (y * 32) / BetaShooter.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / BetaShooter.PPM);
        if(isSensor){
            fdef.isSensor = true;
        } else {
            fdef.isSensor = false;
        }
        fdef.shape = shape;
        b2body.createFixture(fdef);

        return b2body;
    }

    public void render(SpriteBatch batch){ }

    public void handleCollision(PhysicsActor actor){ }

    public void handleUnCollide(PhysicsActor actor){ }

    @Override
    public void dispose() {
        world.dispose();
    }

}

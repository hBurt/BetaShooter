package com.shooter.game.sprites.util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.shooter.game.BetaShooter;

/**
 * Created by: Harrison on 11 Dec 2018
 */
public class PhysicsActor extends Sprite implements Disposable {
    protected Body b2body;
    protected World world;

    public PhysicsActor(World world) {

        this.world = world;
    }

    public Body getPlayerBody(float x, float y, float radius){
        createBody(x,y,radius);

        return b2body;
    }

    public Body getBulletBody(float x, float y, float radius, String direction){
        createBody(x,y,radius);
        if(direction.matches("left")) {
            b2body.applyLinearImpulse(new Vector2(-12f, 0), b2body.getWorldCenter(), true);
        } else if(direction.matches("right")){
            b2body.applyLinearImpulse(new Vector2(12f, 0), b2body.getWorldCenter(), true);
        }
        b2body.setGravityScale(0);
        return b2body;
    }

    private Body createBody(float x, float y, float radius){
        BodyDef bdef = new BodyDef();
        bdef.position.set((x * 32) / BetaShooter.PPM, (y * 32) / BetaShooter.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / BetaShooter.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        return b2body;
    }

    public void handleCollision(PhysicsActor actor){

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}

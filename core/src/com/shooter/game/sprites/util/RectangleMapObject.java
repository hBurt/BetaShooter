package com.shooter.game.sprites.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.shooter.game.managers.PhysicsEntityManager;
import com.shooter.game.sprites.Player;

/**
 * Created by: Harrison on 12 Dec 2018
 */
public class RectangleMapObject extends PhysicsActor {

    World world;

    public RectangleMapObject(World world, int x, int y, int width, int height) {
        super(world);
        this.world = world;

        //Create the object in our world
        b2body = createWallBody(x, y, width, height);
        b2body.setUserData(this);

    }

    public void handleCollision(PhysicsActor actor){
        if(actor instanceof Bullet){
            //Previous Bug : #1
            //Type: Fatal; Crashes game with "Expression: m_bodyCount > 0"
            //Fix: Set flag on physics actors to see if they have been deleted before.
            PhysicsEntityManager.setToDestroy(actor);
            Gdx.app.log(this.getClass().getName(), "Wall: bullet hit");
        }

        if(actor instanceof Player){
            ((Player) actor).setGrounded(true);
            Gdx.app.log(this.getClass().getName(), "Player grounded = " + ((Player) actor).isGrounded());
        }

    }

    @Override
    public void handleUnCollide(PhysicsActor actor) {
        if(actor instanceof Player){
            ((Player) actor).setGrounded(false);
            Gdx.app.log(this.getClass().getName(), "Player grounded = " + ((Player) actor).isGrounded());
        }
    }
}

package com.shooter.game.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.BetaShooter;
import com.shooter.game.sprites.Player;
import com.shooter.game.sprites.util.Bullet;
import com.shooter.game.sprites.util.PhysicsActor;

/**
 * Created by: Harrison on 12 Dec 2018
 */
public class PhysicsEntityManager {

    private static Array<PhysicsActor> objects = new Array<PhysicsActor>();
    private static Array<PhysicsActor> updateObjects = new Array<PhysicsActor>();
    private static Array<PhysicsActor> destroyObjects = new Array<PhysicsActor>();

    public static void add(PhysicsActor object){
        objects.add(object);
    }

    public static void setToUpdate(PhysicsActor object){
        add(object);
        updateObjects.add(object);
    }

    public static void setToDestroy(PhysicsActor object){
        destroyObjects.add(object);
        objects.removeValue(object, true);
        updateObjects.removeValue(object, true);
    }

    public static void update(World world, float delta){
        for(PhysicsActor object : destroyObjects){
            if(object != null) {
                if(((Bullet) object).isOn()) {
                    ((Bullet) object).setOn(false);
                    world.destroyBody(object.getB2body());
                }
            }
        }
        destroyObjects.clear();

        for (PhysicsActor object : updateObjects){
            if(object instanceof Bullet){
                    ((Bullet) object).update();
            }
        }

    }

    public static void draw(SpriteBatch batch){
        batch.begin();
        for(PhysicsActor object : objects){
            batch.draw(object, object.getX(), object.getY(), 6 / BetaShooter.PPM, 6 / BetaShooter.PPM);
            object.render(batch);
        }
        batch.end();
    }

}

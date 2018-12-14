package com.shooter.game.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shooter.game.BetaShooter;
import com.shooter.game.sprites.enemies.Zombie;
import com.shooter.game.sprites.util.Bullet;
import com.shooter.game.sprites.util.PhysicsActor;
import com.shooter.game.sprites.util.RectangleMapObject;

import java.sql.BatchUpdateException;

/**
 * Created by: Harrison on 12 Dec 2018
 */
public class PhysicsEntityManager {

    //Physics Actor arrays to hold all out objects
    private static Array<PhysicsActor> objects = new Array<PhysicsActor>();
    private static Array<PhysicsActor> updateObjects = new Array<PhysicsActor>();
    private static Array<PhysicsActor> destroyObjects = new Array<PhysicsActor>();

    //Add an object to the objects array
    public static void add(PhysicsActor object){
        objects.add(object);
    }

    //Add object to objects that get updated
    public static void setToUpdate(PhysicsActor object){
        add(object);
        updateObjects.add(object);
    }

    //Destroy the object
    public static void setToDestroy(PhysicsActor object){
        //Add object to objects that get deleted
        destroyObjects.add(object);
        objects.removeValue(object, true);
        updateObjects.removeValue(object, true);
    }

    public static void update(World world, float delta){
        //For each object that is ready to be destroyed; destroy
        for(PhysicsActor object : destroyObjects){

            //Null check
            if(object != null) {

                if(object instanceof Bullet) {
                    //Check object deleted flag to ensure object has not been deleted before
                    if (!((Bullet) object).checkDeleted()) {

                        //Set object deleted flag to true
                        ((Bullet) object).flagAsDeleted();

                        world.destroyBody(object.getB2body());
                    }
                }
                if(object instanceof Zombie){
                    //Check object deleted flag to ensure object has not been deleted before
                    if (!((Zombie) object).checkDeleted()) {

                        //Set object deleted flag to true
                        ((Zombie) object).flagAsDeleted();

                        world.destroyBody(object.getB2body());
                    }
                }
            }
        }
        destroyObjects.clear();

        for (PhysicsActor object : updateObjects){
            if(object instanceof Bullet){
                    ((Bullet) object).update();
            }
            if(object instanceof Zombie){
                ((Zombie) object).update();
            }
        }

    }

    public static void draw(SpriteBatch batch){
        batch.begin();
        for(PhysicsActor object : objects){
            if(object instanceof Bullet) {
                batch.draw(object, object.getX(), object.getY(), 6 / BetaShooter.PPM, 6 / BetaShooter.PPM);
                object.render(batch);
            }
            if(object instanceof Zombie){
                batch.draw(object, object.getX(), object.getY(), 32 / BetaShooter.PPM, 32  / BetaShooter.PPM);
                object.render(batch);
            }
            if(object instanceof RectangleMapObject){
               // Gdx.app.log("PEM",  object.getB2body().getPosition() + "");
            }
        }
        batch.end();
    }

}

package com.shooter.game.sprites.util;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.shooter.game.managers.PhysicsEntityManager;

/**
 * Created by: Harrison on 12 Dec 2018
 */
public class RectangleMapObjectFactory {

    private World world;

    private MapObjects walls;

    public RectangleMapObjectFactory(World world, TiledMap map) {
        this.world = world;

        //Get all rectangle properties from each object in specified layer
        walls = map.getLayers().get("walls").getObjects();

        //Loop through each object
        for (MapObject object : walls){
        //MapObject object = walls.get(0);
            //Get object attributes
            int x = (int) Double.parseDouble(object.getProperties().get("x").toString());
            int y = (int) Double.parseDouble(object.getProperties().get("y").toString());
            int width = (int) Double.parseDouble(object.getProperties().get("width").toString());
            int height = (int) Double.parseDouble(object.getProperties().get("height").toString());

            //Gdx.app.log(this.getClass().getName(), "xyWH: " + x + ", " + y + ", " + width + ", " + height);

            //Create the object in our world
            PhysicsEntityManager.setToUpdate(new RectangleMapObject(world, x, y, width, height));

        }
    }




}

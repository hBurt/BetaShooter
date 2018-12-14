package com.shooter.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.shooter.game.sprites.Brick;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public class B2WorldCreator {
    public B2WorldCreator(TiledMap map, World world) {
        //Ground objects
        for(MapObject object : map.getLayers().get("walls").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(world, map, rect);
        }

        /*//Coin objects
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Coin(world, map, rect);
        }*/
    }
}

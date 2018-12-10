package com.shooter.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public class Coin extends InteractiveTileObject{

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

    }

    @Override
    public void onLeftHit() {

    }
}

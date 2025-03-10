package com.shooter.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.shooter.game.BetaShooter;

/**
 * Created by: Harrison on 08 Dec 2018
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / BetaShooter.PPM, (bounds.getY() + bounds.getHeight() / 2) / BetaShooter.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth() / 2 / BetaShooter.PPM), (bounds.getHeight() / 2 / BetaShooter.PPM));
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onLeftHit();

    public void setCAtegoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public String toString() {
        return "x: " + getBounds().x / 32 + " y: " + getBounds().y / 32;
    }
}

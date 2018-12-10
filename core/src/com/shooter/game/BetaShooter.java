package com.shooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.shooter.game.screens.PlayScreen;
import com.shooter.game.screens.ovr.AbstractGame;

public class BetaShooter extends AbstractGame {

	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 480;
	public static final float PPM = 100f;

	private SpriteBatch batch;
	private AssetManager asm;

	@Override
	public void create () {
		batch = new SpriteBatch();

		asm = new AssetManager();
		asm.load("characters/player_and_enemies.atlas", TextureAtlas.class);
		asm.load("characters/weapons_with_bullets.atlas", TextureAtlas.class);
		asm.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		asm.load("maps/untitled.tmx", TiledMap.class);
		asm.finishLoading();

		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();

	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public AssetManager getAsm() {
		return asm;
	}
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.main.Play;

public class TitleFight extends Game {

	@Override
	public void create () {
		setScreen(new Play());
	}

	@Override
	public void render () {
		super.render();
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void pause(){
		super.pause();
	}

	public void resume(){
		super.resume();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}

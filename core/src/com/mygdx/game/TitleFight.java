package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.main.World;

public class TitleFight extends Game {

	@Override
	public void create () {
		setScreen(new World());
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

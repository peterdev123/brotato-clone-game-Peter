package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Screens.Intermession;
import com.mygdx.game.main.World;

public class TitleFight extends Game {
	private Intermession intermession;
	private World world;

	@Override
	public void create() {
		world = new World(this);
		intermession = new Intermession();
		setScreen(world);
	}

	public void showIntermessionScreen() {
		setScreen(intermession);
	}

	public void showGameScreen() {
		setScreen(world);
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

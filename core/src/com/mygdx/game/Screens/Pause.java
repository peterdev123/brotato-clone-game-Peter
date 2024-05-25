package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.main.World;


import javax.swing.*;

public class Pause implements Screen {
    private SpriteBatch batch;
    private Texture background, background2;
    private int counter = 0;
    private boolean keyPressed = false;
    private boolean gamePaused = true;
    public World world;
    public Pause(World world) {
        this.world = world;
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/P_Resume.jpg");
        background2 = new Texture("assets/Pages/P_Exit.jpg");
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (counter == 0) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                world.gamePaused = false;
                world.playBackgroundMusic0("assets/Audio/Game/BattleTheme.wav");
            }
        } else if (counter == 1) {
            batch.draw(background2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
//                SwingUtilities.invokeLater(() -> {
//                    try {
//                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    dispose(); // Close the Pause screen
//                    Menu menu = new Menu(); // Create instance of Menu screen
//                    menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    menu.setVisible(true);
//                });
                System.exit(0);
            }

        }
        batch.end();

        // Check for key presses
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!keyPressed) {
                counter = (counter + 1) % 2;
                keyPressed = true;
            }
        } else {
            keyPressed = false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        background2.dispose();
    }
}
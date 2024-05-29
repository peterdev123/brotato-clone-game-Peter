package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.main.World;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Pause implements Screen {
    private SpriteBatch batch;
    private Texture background, background2;
    private int counter = 0;
    private boolean keyPressed = false;
    private boolean gamePaused = true;
    public World world;
    private Clip bgclip0;
    private Thread musicThread;
    private boolean playMusic = true;

    public Pause(World world) {
        this.world = world;
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/P_Resume.jpg");
        background2 = new Texture("assets/Pages/P_Exit.jpg");

    }

    public void stopGameOverMusic() {
        if (bgclip0 != null && bgclip0.isRunning()) {
            bgclip0.stop();
            bgclip0.close();
        }
    }

    public void playGameOverMusic(String filePath) {
        try {
            // Open an audio input stream.
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            bgclip0 = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            bgclip0.open(audioIn);

            // Adjust volume
            FloatControl gainControl = (FloatControl) bgclip0.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.7) / Math.log(10.0) * 20.0); // -20 dB
            gainControl.setValue(volume);

            // Loop the clip continuously.
            bgclip0.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playing the clip
            bgclip0.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    @Override
    public void show() {
        musicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                playGameOverMusic("assets/Audio/GameOver/Realize(PauseMenu).wav");
            }
        });
        musicThread.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (counter == 0) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                world.gamePaused = false;
                stopGameOverMusic(); // Stop the pause menu music
                world.playBackgroundMusic0("assets/Audio/Game/BattleTheme.wav");
            }
        } else if (counter == 1) {
            batch.draw(background2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                world.dispose();
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
        playMusic = false; // Stop the music thread
        if (musicThread != null) {
            try {
                musicThread.join(); // Wait for the music thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        batch.dispose();
        background.dispose();
        background2.dispose();
    }
}

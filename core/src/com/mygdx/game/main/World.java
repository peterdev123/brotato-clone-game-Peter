package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Screens.Pause;
import com.mygdx.game.TitleFight;
import com.mygdx.game.enemies.EnemyHandler;
import com.mygdx.game.player.Player;
import com.mygdx.game.Screens.Intermession;
import com.mygdx.game.utilities.WaveHandler;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class World implements Screen {
    private TitleFight titleFight;
    private final OrthogonalTiledMapRenderer renderer;
    private final Map map;
    private OrthographicCamera camera;
    private Sprite character;
    private SpriteBatch spriteBatch;
    private Player player;

    private Texture hpbarTexture;
    private Sprite hpbarSprite;
    private float hpbarWidth;
    private final float hpbarHeight;

    private final float CAMERA_SPEED = 150.0f;
    private final float VIRTUAL_WIDTH = 1440;  // Virtual width
    private final float VIRTUAL_HEIGHT = 900;  // Virtual height

    // ENEMIES
    private EnemyHandler enemyHandler;

    // Intermission
    private boolean intermissionScreenShown = false;
    private Intermession intermissionScreen;

    // Pause
    public boolean gamePaused = false;
    private Pause pauseScreen;

    // BG music
    private Clip bgclip0;

    // Wave timer
    private WaveHandler waveTimerThread;
//    private int currentWave = 1;
//    private float waveTimer = 30; // Duration of each wave in seconds
    private BitmapFont font;

    public World(TitleFight titleFight) {
        this.titleFight = titleFight;
        map = new Map();
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        spriteBatch = new SpriteBatch();
        renderer = map.makeMap();
        hpbarTexture = new Texture("assets/Extras/hpbar2.png");
        hpbarSprite = new Sprite(hpbarTexture);
        hpbarWidth = hpbarSprite.getWidth();
        hpbarHeight = hpbarSprite.getHeight();
        intermissionScreen = new Intermession();
        player = new Player(intermissionScreen);
        pauseScreen = new Pause(this);

        waveTimerThread = new WaveHandler();
        waveTimerThread.start();

        // ENEMIES
        enemyHandler = new EnemyHandler(player.getWeapon(), player);
        playBackgroundMusic0("assets/Audio/Game/BattleTheme.wav");

        // Initialize font
        font = new BitmapFont();
        font.getData().setScale(4); // Set the font scale if needed
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = .3f;
        player.character.setPosition(280, 200);
        camera.position.set(player.character.getX(), player.character.getY(), 0);
    }

    public void playBackgroundMusic0(String filePath) {
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
            float volume = (float) (Math.log(0.1) / Math.log(10.0) * 20.0); // -16 dB
            gainControl.setValue(volume);

            // Loop the clip continuously.
            bgclip0.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic0() {
        if (bgclip0 != null && bgclip0.isRunning()) {
            bgclip0.stop();
            bgclip0.close();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // DEBUG: Render the intermission screen when true
        if (intermissionScreenShown) {
            intermissionScreen.render(delta);
            waveTimerThread.pauseTimer();
            // Hides the intermission screen when "ESC" pressed
            if (Gdx.input.isKeyJustPressed(Input.Keys.K) || !intermissionScreen.getIntermessionShown()) {
                hideIntermissionScreen();
                playBackgroundMusic0("assets/Audio/Game/BattleTheme.wav");
                intermissionScreen.hide();
                player.updatePlayerStats();
            }
            return; // Stop rendering the game world if the intermission screen is shown
        }

        // Pause Screen
        if (gamePaused) {
            pauseScreen.render(delta);
            pauseScreen.setGamePaused(gamePaused);
            return;
        }

        // Pauses the Screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gamePaused = true;
            waveTimerThread.pauseTimer();
            stopBackgroundMusic0();
        }

        // DEBUG: Shows the intermission screen when "ESC" pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            if (!intermissionScreenShown) {
                intermissionScreen.setIntermessionShown(true);
                showIntermissionScreen();
            }
        }

        if (!gamePaused) {
            waveTimerThread.resumeTimer();
        }

        //Class
        // Handle the wave timer
//        waveTimer -= delta;
//        if (waveTimer <= 0) {
//            currentWave++;
//            //Increment Stat Points for intermession
//            intermissionScreen.setStatPoints(2);
//            showIntermissionScreen();
//            waveTimer = 30; // Reset the timer for the next wave
//        }

        int currentWave = waveTimerThread.getCurrentWave();
        int waveTimer = waveTimerThread.getWaveTimer();

        if (waveTimer == 0) {
            intermissionScreen.setStatPoints(2);
            showIntermissionScreen();
            waveTimerThread.setWaveTimer(30);
            waveTimerThread.setWave(1);
            enemyHandler.setHealthEnemies(waveTimerThread.getCurrentWave());
        }

        zoom(); // Call zoom to adjust zoom level if keys are pressed

        // Clamps the camera to prevent out of bounds camera movement
        clampCamera();

        // Render game elements
        renderer.setView(camera);
        renderer.render(new int[]{0, 1});
        player.handleMovement(camera);

        // ENEMIES: DEBUGGING
        enemyHandler.handleWave(camera);

        renderer.render(new int[]{2});


        renderData(currentWave, waveTimer);


        camera.update();
    }

//    public int getCurrentWave() {
//        return currentWave;
//    }

    public void clampCamera() {
        float playerCenterX = player.character.getX();
        float playerCenterY = player.character.getY();

        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2;

        float minX = cameraHalfWidth;
        float minY = cameraHalfHeight;
        float maxX = map.MAP_WIDTH - cameraHalfWidth;
        float maxY = map.MAP_HEIGHT - cameraHalfHeight;

        camera.position.x = MathUtils.clamp(playerCenterX, minX, maxX);
        camera.position.y = MathUtils.clamp(playerCenterY, minY, maxY);
    }

    public void renderData(int currentWave, float waveTimer) {
        // gi comment out ky mu max si player ambot ngnu
//        renderer.setView(camera);
//        renderer.render(new int[]{0, 1});
//        player.handleMovement(camera);
        spriteBatch.begin();
        // PRA HP HEALTH PANGUTANA LNG NKO PRA UPDATE2 NIYA KY LIBOG JD KUN E REDRAW NA SIYA NGA MAGBASE SA PLAYER HP
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            hpbarWidth -= 100;
            player.decreaseHealth(10);
            System.out.println("player health percentage: " + player.getHealthPercentage());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            hpbarWidth += 100;
            player.increaseHealth(10);
            System.out.println("player health percentage: " + player.getHealthPercentage());
            if (hpbarWidth < 0) {
                hpbarWidth = 0;
            }
        }

        String timerText = "Wave " + currentWave + ": " + (int) waveTimer;
        font.draw(spriteBatch, timerText, ((float) Gdx.graphics.getWidth() / 2) - 150, Gdx.graphics.getHeight() - 50);

        // Draw the HP bar at the top left corner
        hpbarSprite.setSize(hpbarWidth, hpbarHeight);
        hpbarSprite.setPosition(10, Gdx.graphics.getHeight() - hpbarHeight - 10); // Top left corner
        hpbarSprite.draw(spriteBatch);

        // Draw current health and total health text
        String healthText = player.getCurrentHealth() + "/" + player.getMaxHealth();
        font.draw(spriteBatch, healthText, 20, Gdx.graphics.getHeight() - hpbarHeight - 30);
        spriteBatch.end();
        // ENEMIES: DEBUGGING
        // gi comment out ky mu max si player ambot ngnu
//        enemyHandler.handleWave(camera);
//
//        renderer.render(new int[]{2});
    }

    public void zoom() {
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            camera.zoom += 0.05f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            camera.zoom -= 0.05f;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = VIRTUAL_WIDTH;
        camera.viewportHeight = VIRTUAL_HEIGHT;
        camera.zoom = 0.3f;
        camera.update();
    }

    public void showIntermissionScreen() {
        stopBackgroundMusic0();
        intermissionScreenShown = true;
        waveTimerThread.pauseTimer();
        intermissionScreen.show();
    }

    public void hideIntermissionScreen() {
        intermissionScreenShown = false;
        waveTimerThread.resumeTimer();
    }

    @Override
    public void pause() {
        // TODO
    }

    @Override
    public void resume() {
        // TODO
    }

    @Override
    public void hide() {
        stopBackgroundMusic0();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        font.dispose();
        spriteBatch.dispose(); // Don't forget to dispose the spriteBatch
    }


}

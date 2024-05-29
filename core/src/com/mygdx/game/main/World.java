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
import com.mygdx.game.Screens.GameOver;
import com.mygdx.game.Screens.Leaderboard;
import com.mygdx.game.Screens.Pause;
import com.mygdx.game.TitleFight;
import com.mygdx.game.enemies.EnemyHandler;
import com.mygdx.game.player.Player;
import com.mygdx.game.Screens.Intermession;
//import com.mygdx.game.utilities.HeadStart;
import com.mygdx.game.utilities.ResetRunnable;
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
    private final float max_hpbarwidth;
    private final float hpbarHeight;
    private float hpPercentage;
    private final float CAMERA_SPEED = 150.0f;
    private final float VIRTUAL_WIDTH = 1440;  // Virtual width
    private final float VIRTUAL_HEIGHT = 900;  // Virtual height

    // ENEMIES
    private EnemyHandler enemyHandler;

    // Intermission
    private boolean intermissionScreenShown = false;
    private Intermession intermissionScreen;


    // GAMEOVER
    private boolean gameOverScreenShown = false;
    private GameOver gameOver;
    // Pause
    public boolean gamePaused = false;
    private Pause pauseScreen;

    // BG music
    private Clip bgclip0;

    // Wave timer
    private WaveHandler waveTimerThread;
//    private int currentWave = 1;
//    private float waveTimer = 30; // Duration of each wave in seconds

    //Text
    private BitmapFont font;

    //Boolean for game done
    private boolean gameDone;

    //For leaderboard
    private Leaderboard leaderboard;

    public World(TitleFight titleFight) {
        this.titleFight = titleFight;
        map = new Map();

        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        spriteBatch = new SpriteBatch();
        renderer = map.makeMap();
        hpbarTexture = new Texture("assets/Extras/hpbar2.png");
        hpbarSprite = new Sprite(hpbarTexture);
        hpbarWidth = hpbarSprite.getWidth();
        max_hpbarwidth = hpbarSprite.getWidth();
        hpbarHeight = hpbarSprite.getHeight();
        intermissionScreen = new Intermession();
        player = new Player(intermissionScreen);
        pauseScreen = new Pause(this);
        gameOver = new GameOver(this);

        // Start wave
        waveTimerThread = new WaveHandler();
        waveTimerThread.start();

        this.gameDone = false;
        leaderboard = new Leaderboard(this);


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
        System.out.println(Player.totalScore);
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (gameDone) {
            leaderboard.render(delta);
            return;
        }
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
        }else{
            pauseScreen.stopGameOverMusic();
        }

        // GAME OVER SCREEN
        if(gameOverScreenShown){
            gameOver.render(delta);
            gameOver.setGameOver(gameOverScreenShown);
            return;
        }

        // GAMEOVER DEBUG
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            gameOverScreenShown = true;
            waveTimerThread.pauseTimer();
            gameOver.show();
            stopBackgroundMusic0();
        }

        // Pauses the Screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gamePaused = true;
            waveTimerThread.pauseTimer();
            pauseScreen.show();
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

        // GAME OVER
        if (player.getHealthPercentage() == 0) {
            gameOverScreenShown = true;
            waveTimerThread.pauseTimer();
            gameOver.show();
            stopBackgroundMusic0();
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

        checkWave(waveTimer);

//        if (waveTimer == 0) {
//            intermissionScreen.setStatPoints(2);
//            showIntermissionScreen();
////            waveTimerThread.setHeadStart();
//            waveTimerThread.setWaveTimer(30);
//            waveTimerThread.setWave(1);
//            enemyHandler.setHealthEnemies(waveTimerThread.getCurrentWave());
//        }

        zoom(); // Call zoom to adjust zoom level if keys are pressed

        // Clamps the camera to prevent out of bounds camera movement
        clampCamera();

        // Render game elements
        renderer.setView(camera);
        renderer.render(new int[]{0, 1});
        player.handleMovement(camera);

        // ENEMIES: DEBUGGING
        enemyHandler.handleWave(camera, waveTimer);

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

        String timerText;

        if (waveTimer == 30) {
            timerText = "They are coming!";
        }else {
            timerText = "Wave " + currentWave + ": " + (int) waveTimer;
        }

        font.draw(spriteBatch, timerText, ((float) Gdx.graphics.getWidth() / 2) - 180, Gdx.graphics.getHeight() - 50);

        // Draw the HP bar at the top left corner

//        updateHp();
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            hpbarWidth += 100;
            player.increaseHealth(10);
//            System.out.println("player health percentage: " + player.getHealthPercentage());
            if (hpbarWidth < 0) {
                hpbarWidth = 0;
            }
        }
        if (enemyHandler.handlePlayerCollisions()) {
            hpbarWidth -= 100;
            if (hpbarWidth < 0) {
                hpbarWidth = 0; // Ensure HP bar width doesn't go below zero
            }
        }
        hpbarWidth = max_hpbarwidth;
        hpPercentage = player.getHealthPercentage();
        float newHpBarWidth = hpbarWidth * hpPercentage;
        hpbarSprite.setSize(newHpBarWidth, hpbarHeight);
        hpbarSprite.setPosition(10, Gdx.graphics.getHeight() - hpbarHeight - 10); // Top left corner
        hpbarSprite.draw(spriteBatch);

        // Draw current health and total health text
        String healthText = String.format("%.2f",player.getCurrentHealth()) + "/" + String.format("%.0f", Math.ceil(player.getMaxHealth()));
        font.draw(spriteBatch, healthText, 20, Gdx.graphics.getHeight() - hpbarHeight - 30);
        spriteBatch.end();
        // ENEMIES: DEBUGGING
        // gi comment out ky mu max si player ambot ngnu
//        enemyHandler.handleWave(camera);
//
//        renderer.render(new int[]{2});
    }

    public void checkWave(int waveTimer) {
        if (waveTimer == 0) {
            showIntermissionScreen();
            ResetRunnable resetRunnable = new ResetRunnable(waveTimerThread, enemyHandler, intermissionScreen);
            Thread resetThread = new Thread(resetRunnable);
            resetThread.start();
        }
    }

    public void setGameDone() {
        gameDone = true;
    }

    public void zoom() {
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            camera.zoom += 0.05f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            camera.zoom -= 0.05f;
        }
    }

//    private void updateHp(){
//
//        spriteBatch.begin();
//        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
//            hpbarWidth += 100;
//            player.increaseHealth(10);
//            System.out.println("player health percentage: " + player.getHealthPercentage());
//            if (hpbarWidth < 0) {
//                hpbarWidth = 0;
//            }
//        }
//        if (enemyHandler.handlePlayerCollisions()) {
//            hpbarWidth -= 100;
//            if (hpbarWidth < 0) {
//                hpbarWidth = 0; // Ensure HP bar width doesn't go below zero
//            }
//        }
//
//        float healthPercentage = player.getHealthPercentage();
//        float newHpBarWidth = hpbarWidth * healthPercentage;
//        hpbarSprite.setSize(newHpBarWidth, hpbarHeight);
//        hpbarSprite.setPosition(10, Gdx.graphics.getHeight() - hpbarHeight - 10); // Top left corner
//        hpbarSprite.draw(spriteBatch);
//        spriteBatch.end();
//    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = VIRTUAL_WIDTH;
        camera.viewportHeight = VIRTUAL_HEIGHT;
        camera.zoom = 0.3f;
        camera.update();
    }

    public void showIntermissionScreen() {
        stopBackgroundMusic0();
        player.resetHealth();
        intermissionScreenShown = true;
        waveTimerThread.pauseTimer();
        intermissionScreen.show();

    }

    public void resetAll(){
        intermissionScreen.resetAllStatsBackToNormal();
        titleFight.create();

    }

    public void hideIntermissionScreen() {
        intermissionScreenShown = false;
        spriteBatch.begin();
        float healthPercentage = player.getHealthPercentage();
        float newHpBarWidth = hpbarWidth * healthPercentage;
        hpbarSprite.setSize(newHpBarWidth, hpbarHeight);
        hpbarSprite.setPosition(10, Gdx.graphics.getHeight() - hpbarHeight - 10); // Top left corner
        hpbarSprite.draw(spriteBatch);
        spriteBatch.end();
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
        // Dispose renderer
        if (renderer != null) {
            renderer.dispose();
        }

        // Dispose font
        if (font != null) {
            font.dispose();
        }

        // Dispose sprite batch
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }

        // Dispose textures and sprites
        if (hpbarTexture != null) {
            hpbarTexture.dispose();
        }

        if (character.getTexture() != null) {
            character.getTexture().dispose();
        }

        // Dispose intermission screen resources
        if (intermissionScreen != null) {
            intermissionScreen.dispose();
        }

        // Dispose game over screen resources
        if (gameOver != null) {
            gameOver.dispose();
        }

        // Dispose pause screen resources
        if (pauseScreen != null) {
            pauseScreen.dispose();
        }

        // Stop and dispose of background music
        stopBackgroundMusic0();
    }



}

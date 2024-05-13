package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.player.Player;

public class Play implements Screen {
    private final OrthogonalTiledMapRenderer renderer;
    private final Map map;
    private OrthographicCamera camera;
    private Sprite character;
    private SpriteBatch spriteBatch;
    private Player player;

    private final float CAMERA_SPEED = 150.0f;

    public Play(){
        map = new Map();
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        spriteBatch = new SpriteBatch();
        renderer = map.makeMap();
        player = new Player();
    }

    public void show(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = .2f;
        player.character.setPosition(300, 200);
        camera.position.set(player.character.getX(), player.character.getY(), 0);


    }

    public void render(float delta){
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.handleMovement(); // Handle player movement first

        handleInput(delta); // Call handleInput to move the camera with WASD keys
        zoom(); // Call zoom to adjust zoom level if keys are pressed

        // Calculate the center position of the player
        float playerCenterX = player.character.getX();
        float playerCenterY = player.character.getY();

        // Set camera position to follow the player, adjusting for zoom level
        camera.position.set(playerCenterX, playerCenterY, 0);

        camera.update(); // Update camera after changing position

        // Debug output
        System.out.println("Player position: (" + player.character.getX() + ", " + player.character.getY() + ")");
        System.out.println("Camera position: (" + camera.position.x + ", " + camera.position.y + ")");

        renderData(); // Render the map and playeryer
    }

    public void renderData(){
        renderer.setView(camera);
        renderer.render();
        player.handleMovement();
    }

    public void handleInput(float delta) {
        float moveSpeed = CAMERA_SPEED * delta; // Adjust speed based on frame time

        // Move camera with WASD keys
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y += moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y -= moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += moveSpeed;
        }
    }

    public void zoom(){
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            camera.zoom += 0.05f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
            camera.zoom -= 0.05f;
        }
    }

    public void resize(int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public void pause(){
        //TODO
    }

    public void resume(){
        //TODO
    }

    public void hide(){
        dispose();
    }

    public void dispose(){
        renderer.dispose();
    }
}

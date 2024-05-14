package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.player.Player;

public class Play implements Screen {
    private final OrthogonalTiledMapRenderer renderer;
    private final Map map;
    private OrthographicCamera camera;
    private Sprite character;
    private SpriteBatch spriteBatch;
    private Player player;

    private final float CAMERA_SPEED = 150.0f;
    private final float VIRTUAL_WIDTH = 1440;  // Virtual width
    private final float VIRTUAL_HEIGHT = 900;  // Virtual height

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
        camera.zoom = .3f;
        player.character.setPosition(280, 200);
        camera.position.set(player.character.getX(), player.character.getY(), 0);


    }

    public void render(float delta){
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        zoom(); // Call zoom to adjust zoom level if keys are pressed

        // Calculate the center position of the player
        float playerCenterX = player.character.getX();
        float playerCenterY = player.character.getY();



        // Set camera position to follow the player, adjusting for zoom level
        camera.position.set(playerCenterX, playerCenterY, 0);

         // Update camera after changing position

        // Debug output
//        System.out.println("Player position: (" + player.character.getX() + ", " + player.character.getY() + ")");
//        System.out.println("Camera position: (" + camera.position.x + ", " + camera.position.y + ")");

        System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

        renderData();
        camera.update();// Render the map and playeryer
    }

    public void renderData(){
        renderer.setView(camera);
        renderer.render(new int[] {0, 1});
        player.handleMovement(camera);
        renderer.render(new int[] {2});
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
        camera.viewportWidth = VIRTUAL_WIDTH;
        camera.viewportHeight = VIRTUAL_HEIGHT;
        camera.zoom = 0.3f;
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

package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Play implements Screen {
    private final OrthogonalTiledMapRenderer renderer;
    private final Map map;
    private OrthographicCamera camera;

    private final float CAMERA_SPEED = 150.0f;

    public Play(){
        map = new Map();
        renderer = map.makeMap();
    }

    public void show(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = .2f;
        camera.position.set(150, 150, 0);

    }

    public void render(float delta){
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        zoom();
        handleInput(delta);
        camera.update();
        renderData();
    }

    public void renderData(){
        renderer.setView(camera);
        renderer.render();
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

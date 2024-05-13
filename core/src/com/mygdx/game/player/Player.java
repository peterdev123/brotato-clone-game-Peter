package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.utilities.Animator;

public class Player{
    public SpriteBatch spriteBatch;
    public Sprite character;
    private Animator animator;
    private float stateTime;
    private int speed;
    private int prev_movement;

    private Texture idle;
    private Texture run;

    public Player(){
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        character.setScale(-2f);
        spriteBatch = new SpriteBatch();
        animator = new Animator();
        stateTime = 0f;
        speed = 65;
        prev_movement = 0;

        idle = new Texture(Gdx.files.internal("animations/idle.png"));
        run = new Texture(Gdx.files.internal("animations/run.png"));
    }

    public void handleMovement(){
        stateTime += Gdx.graphics.getDeltaTime() * 0.15f;
        TextureRegion currentFrame = null;


        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            float x = character.getX();
            character.setX(x -= Gdx.graphics.getDeltaTime() * speed);
            prev_movement = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            currentFrame = animator.animateRun(run).getKeyFrame(stateTime, true);
            float x = character.getX();
            character.setX(x += Gdx.graphics.getDeltaTime() * speed);
            prev_movement = 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            float y = character.getY();
            character.setY(y += Gdx.graphics.getDeltaTime() * speed);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            float y = character.getY();
            character.setY(y -= Gdx.graphics.getDeltaTime() * speed);
        }

        spriteBatch.begin();
        if(currentFrame != null){
            spriteBatch.draw(currentFrame, 800, 500, 158, 158);
        }
        else{
            TextureRegion idles = animator.animateIdle(idle).getKeyFrame(stateTime, true);
            spriteBatch.draw(idles, 800, 500, 158, 158);
        }
        spriteBatch.end();
    }


}

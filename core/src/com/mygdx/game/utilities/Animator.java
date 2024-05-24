package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {


    public Animation<TextureRegion> animateRun(Texture animateTexture){
        int FRAME_COLS = 4, FRAME_ROWS = 2;
        TextureRegion[][] tmp = TextureRegion.split(animateTexture,
                animateTexture.getWidth() / FRAME_COLS,
                animateTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] runFrames = new TextureRegion[(FRAME_ROWS * FRAME_COLS)];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++){
            for(int j = 0; j < FRAME_COLS; j++){
                runFrames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.025f, runFrames);
    }

    public Animation<TextureRegion> animateIdle(Texture animateTexture){
        int FRAME_COLS = 2, FRAME_ROWS = 3;
        TextureRegion[][] tmp = TextureRegion.split(animateTexture,
                animateTexture.getWidth() / FRAME_COLS,
                animateTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] runFrames = new TextureRegion[(FRAME_ROWS * FRAME_COLS)];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++){
            for(int j = 0; j < FRAME_COLS; j++){
                runFrames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.025f, runFrames);
    }

    public Animation<TextureRegion> animateZombie(Texture animateTexture){
        int FRAME_COLS = 4, FRAME_ROWS = 3;
        TextureRegion[][] tmp = TextureRegion.split(animateTexture,
                animateTexture.getWidth() / FRAME_COLS,
                animateTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] runFrames = new TextureRegion[(FRAME_ROWS * FRAME_COLS)];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++){
            for(int j = 0; j < FRAME_COLS; j++){
                runFrames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.025f, runFrames);
    }
}

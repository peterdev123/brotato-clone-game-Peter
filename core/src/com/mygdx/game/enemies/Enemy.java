package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public Texture enemy_texture;
    public Vector2 position;
    private int health;

    private Texture Zombie_1;
    private Texture Zombie_2;

    public Enemy(Texture enemey_texture, Vector2 position){
        this.enemy_texture = enemey_texture;
        this.position = position;
        health = 100;
    }



}

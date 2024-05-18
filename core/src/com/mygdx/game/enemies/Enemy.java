package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public Texture enemy_texture;
    public Vector2 position;
    public Vector2 size;

    private int health;
    private int damage;
    private boolean isALive;

    //ENEMY HITBOX
    private Rectangle hitbox;

    public Enemy(Texture enemey_texture, Vector2 position){
        this.enemy_texture = enemey_texture;
        this.position = position;
        this.size = new Vector2(45, 45);
        health = 20;
        damage = 5;

        //ENEMY HITBOX
        isALive = true;
        hitbox = new Rectangle(position.x + 10, position.y + 10, size.x - 20, size.y - 20);
    }

    public Rectangle getEnemyHitbox(){
        return hitbox;
    }

    public void takeDamage(int damage){
        System.out.println("DAMAGE TAKEN: " + damage);
        System.out.println("Health Before: " + this.health);
        health -= damage;
        System.out.println("Health After: " + this.health);

        //NUMBER GOES OUT DISPLAYING DAMAGE

        if(health <= 0){
            //TODO: PLAY DEATH ANIMATION AT LOCATION OF DEATH
            isALive = false;
        }
    }

    public boolean checkIsAlive(){
        return isALive;
    }



}

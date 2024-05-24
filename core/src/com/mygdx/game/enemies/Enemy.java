package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utilities.Animator;

public class Enemy {
    public Texture enemy_texture;
    public Vector2 position;
    public Vector2 size;
    public Animator animator;

    private int health;
    private int damage;
    private boolean isALive;
    private int speed;

    private float stateTime;

    //ENEMY HITBOX
    private Rectangle hitbox;

    public Enemy(Texture enemey_texture, Vector2 position){
        this.enemy_texture = enemey_texture;
        this.position = position;
        this.size = new Vector2(45, 45);
        health = 20;
        damage = 5;
        speed = 40;
        stateTime = 0f;

        //ENEMY HITBOX
        isALive = true;
        hitbox = new Rectangle(position.x + 10, position.y + 10, size.x - 20, size.y - 20);
    }

    public Rectangle getEnemyHitbox(){
        return hitbox;
    }

    //DEBUGGING
    public void moveEnemyTowardsPlayer(Vector2 player_position, SpriteBatch spriteBatch){
        stateTime += Gdx.graphics.getDeltaTime() * 0.30f;
        TextureRegion currentFrame = null;

        // Calculate the direction vector
        Vector2 direction = new Vector2(player_position.x - position.x, player_position.y - position.y);

        // Normalize the direction vector
        if (direction.len() != 0) {
            direction.nor();
        }

        // Calculate movement vector by scaling direction with speed and delta time
        Vector2 movement = direction.scl(speed * Gdx.graphics.getDeltaTime());

        // Update position and hitbox
        position.add(movement);
        hitbox.setPosition(position.x + 10, position.y + 10);
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

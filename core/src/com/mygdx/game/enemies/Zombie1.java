package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Zombie1 extends Enemies {
    public Texture enemy_texture;
//    public Vector2 position;
//    public Vector2 size;

    // ENEMY HITBOX
    private Rectangle hitbox;

    public Zombie1(int health, Texture enemy_texture, Vector2 position) {
        super("Zombie 1", health, 5, 0, 70, position, new Vector2(45, 45),
                new Rectangle(position.x + 10, position.y + 10, new Vector2(45, 45).x - 30, new Vector2(45, 45).y - 30), 0);  // Initializing with default values, can be modified
        this.enemy_texture = enemy_texture;
//        this.position = position;
//        this.size = new Vector2(45, 45);

        // ENEMY HITBOX
//        hitbox = new Rectangle(position.x + 10, position.y + 10, size.x - 20, size.y - 20);
    }

    @Override
    public void attack() {
        System.out.println("Enemy attacks with power " + getAttackPower() + "!");
    }

    @Override
    public void takeDamage(int damage) {
        System.out.println("DAMAGE TAKEN: " + damage);
        System.out.println("Health Before: " + getHealth());
        setHealth(getHealth() - (damage - getDefense()));
        System.out.println("Health After: " + getHealth());

        // NUMBER GOES OUT DISPLAYING DAMAGE

        if (getHealth() <= 0) {
            // TODO: PLAY DEATH ANIMATION AT LOCATION OF DEATH
            setAlive(false);
        }
    }

    @Override
    public void move() {
        System.out.println("Enemy moves to a new position.");
        // Implement the logic for moving the enemy
    }

//    public Vector2 getPosition() {
//        return position;
//    }

//    public Rectangle getEnemyHitbox() {
//        return hitbox;
//    }

    public boolean checkIsAlive() {
        return isAlive();
    }
}

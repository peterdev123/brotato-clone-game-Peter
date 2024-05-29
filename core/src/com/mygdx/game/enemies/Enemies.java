package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utilities.Animator;

public abstract class Enemies {

    private int health;
    private float attackPower;
    private int defense;
    private int speed;
    private String name;
    private boolean isAlive;
    private Vector2 position;
    private Vector2 size;
    private Rectangle hitbox;
    private int zombie_number;
    private Animator animator;

    public Enemies(String name, int health, float attackPower, int defense, int speed, Vector2 position, Vector2 size, Rectangle hitbox, int zombie_number) {
        //Initializing stats
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.isAlive = true;
        this.position = position;
        this.size = size;
        this.zombie_number = zombie_number;
        this.speed = speed;

        //Hitbox
        this.hitbox = hitbox;

        //animations
        animator = new Animator();
    }

    // Abstract methods that must be implemented by subclasses
    public abstract void attack();
    public abstract void takeDamage(int damage);
    public abstract void move();

    // Common methods that all enemies can use
    public void displayStatus() {
        System.out.println(name + " - Health: " + health + ", Attack: " + attackPower + ", Defense: " + defense);
    }

    //DEBUGGING
    public void moveEnemyTowardsPlayer(Vector2 player_position, SpriteBatch spriteBatch){

        // Calculate the direction vector
        Vector2 direction = new Vector2(player_position.x - position.x - 18, player_position.y - position.y - 10);

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

    // Getters and Setters
    public int getHealth() {
        return health;
    }

    public int getZombieNumber() {
        return zombie_number;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Rectangle getEnemyHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}

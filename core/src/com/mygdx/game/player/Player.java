package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Screens.Intermession;
import com.mygdx.game.utilities.Animator;
import com.mygdx.game.utilities.Collision;
import com.mygdx.game.weapons.Weapon;

public class Player{
    private Rectangle hitbox;
    //CONSTANTS
    private float PLAYER_WIDTH = 84, PLAYER_HEIGHT = 84, COLLISION_WIDTH = 5, COLLISION_HEIGHT = 5,
            LOS_WIDTH = 180, LOS_HEIGHT = 180;
    private float centerX = Gdx.graphics.getWidth() / 2f;
    private float centerY = Gdx.graphics.getHeight() / 2f;

    // Calculate the position to draw the player sprite
    private float playerDrawX = centerX - PLAYER_WIDTH / 2f;
    private float playerDrawY = centerY - PLAYER_HEIGHT / 2f;

    private Weapon weaponHandler;

    //Player Attributes
    private float maxHealth;
    private float health;
    private int armor;
    public static float damage_multiplier;

    //Player Prerequisites
    public SpriteBatch spriteBatch;
    public Sprite character;
    private Animator animator;
    private float stateTime;
    private int speed;
    private boolean isMoving;
    private boolean isMovingLeft;

    //Player Collision Attributes
    private Collision collision;
    private Rectangle player_bounds;
    private float previous_x;
    private float previous_y;

    //Player Line of Sight
    private Rectangle line_of_sight;

    //Player preloaded textures
    private Texture idle;
    private Texture run;
    private Texture idle_inverse;
    private Texture run_inverse;

    //Debugging
    private ShapeRenderer shapeRendererCollision;
    private ShapeRenderer shapeRendererLOS;

    private Intermession intermessionData;

    public Player(Intermession intermessionData){
        this.intermessionData = intermessionData;
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        character.setScale(-2f);
        spriteBatch = new SpriteBatch();
        animator = new Animator();
        stateTime = 0f;
        speed = 55 * 2;
        isMoving = false;
        isMovingLeft = false;

        collision = new Collision();
        player_bounds = new Rectangle(playerDrawX, playerDrawY, COLLISION_WIDTH, COLLISION_HEIGHT);
        line_of_sight = new Rectangle(playerDrawX, playerDrawY, LOS_WIDTH, LOS_HEIGHT);
        previous_x = 0;
        previous_y = 0;
        shapeRendererCollision = new ShapeRenderer();
        shapeRendererLOS = new ShapeRenderer();

        weaponHandler = new Weapon(this);

        idle = new Texture(Gdx.files.internal("animations/idle_test.png"));
        run = new Texture(Gdx.files.internal("animations/run_test.png"));
        idle_inverse = new Texture(Gdx.files.internal("animations/idle_inverse_test.png"));
        run_inverse = new Texture(Gdx.files.internal("animations/run_inverse_test.png"));

        //PLAYER ATTRIBUTES
        hitbox = new Rectangle(playerDrawX , playerDrawY , new Vector2(45, 45).x - 30, new Vector2(45, 45).y - 30);
        maxHealth = 30;
        health = maxHealth;
        damage_multiplier = 1.0f;
        armor = 0;
    }

    //RETURNS PLAYER HITBOX
    public Rectangle getPlayerHitbox() {
        return hitbox;
    }

    //SET SPEED BASED ON STATS
    private void setSpeed() {
        speed = (55 + (intermessionData.getSpeedData() * 4)) * 2;
    }

    //SET HP BASED ON STATS
    private void setHealth() {
        health = health + (intermessionData.getHpData() * 2);
        if (health > maxHealth) {
            maxHealth = health;
        }

    }



    //SET ARMOR BASED ON STATS
    private void setArmor() {
        armor = intermessionData.getArmorData();
    }

    //DEBUG
//    private void showStats() {
//        System.out.print(health + " " + damage_multiplier + " " + speed + " " + armor);
//        System.out.println();
//    }

    //SET DAMAGE BASED ON STATS
    private void setDamage_multiplier() {
        damage_multiplier = 1 + (intermessionData.getDamageData() * 0.2f);
    }
    public void updatePlayerStats(){
        setSpeed();
        setHealth();
        setArmor();
        setDamage_multiplier();
    }
    public void handleMovement(OrthographicCamera camera){
        stateTime += Gdx.graphics.getDeltaTime() * 0.30f;
        TextureRegion currentFrame = null;

        //debugging
//        shapeRendererLOS.setProjectionMatrix(camera.combined);
//        shapeRendererLOS.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRendererLOS.setColor(Color.BLUE);
//        shapeRendererLOS.rect(line_of_sight.x, line_of_sight.y, line_of_sight.width, line_of_sight.height);
//        shapeRendererLOS.end();
//
//        shapeRendererCollision.setProjectionMatrix(camera.combined);
//        shapeRendererCollision.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRendererCollision.setColor(Color.RED); // Adjust color as needed
//        shapeRendererCollision.rect(player_bounds.x, player_bounds.y, player_bounds.width, player_bounds.height);
//        shapeRendererCollision.end();

        collision.playerCollision(player_bounds, character);

        isMovingLeft = checkDirectionFacing(camera);

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            previous_x = character.getX();
            character.setX(previous_x -= Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            previous_x = character.getX();
            character.setX(previous_x += Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            previous_y = character.getY();
            character.setY(previous_y += Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            previous_y = character.getY();
            character.setY(previous_y -= Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(isMoving){
            if(isMovingLeft){
                currentFrame = animator.animateRun(run_inverse).getKeyFrame(stateTime, true);
            }
            else{
                currentFrame = animator.animateRun(run).getKeyFrame(stateTime, true);
            }
        }

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);

        if(isMoving){
            spriteBatch.draw(currentFrame, character.getX() - 40, character.getY() - 10, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        else{
            TextureRegion idles = null;
            if(isMovingLeft){
                idles = animator.animateIdle(idle_inverse).getKeyFrame(stateTime, true);
            }
            if(!isMovingLeft){
                idles = animator.animateIdle(idle).getKeyFrame(stateTime, true);
            }
            spriteBatch.draw(idles, character.getX() - 40, character.getY() - 10, PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        //Weapons
        weaponHandler.handleWeapon(camera, spriteBatch, character.getX(), character.getY());

        //Change Weapon Test
        weaponHandler.test();

        //collision box
        player_bounds = new Rectangle(character.getX(), character.getY(), COLLISION_WIDTH, COLLISION_HEIGHT);

        hitbox.setPosition(player_bounds.getX() - 5 , player_bounds.getY() + 3);

        //line of sight
        line_of_sight = new Rectangle(character.getX() - (LOS_WIDTH / 2), character.getY() - (LOS_HEIGHT / 2), LOS_WIDTH, LOS_HEIGHT);

        isMoving = false;
        spriteBatch.end();
    }

    private boolean checkDirectionFacing(OrthographicCamera camera){
        Vector3 position = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return !(position.x - character.getX() > 0);
    }

    public Weapon getWeapon(){
        return weaponHandler;
    }

    public float getMultiplier(){
        return damage_multiplier;
    }

    public void decreaseHealth(float amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }


    public void increaseHealth(float amount) {
        health += amount;
        if (health > maxHealth) {
            maxHealth = health;
        }
    }

    public float getHealthPercentage() {
        return health / maxHealth;
    }

    public float getCurrentHealth(){
        return health;
    }

    public float getMaxHealth(){
        return maxHealth;
    }

    public Vector2 getLocation(){
        return new Vector2(character.getX(), character.getY());
    }

}
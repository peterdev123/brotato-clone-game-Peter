package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.player.Player;
import com.mygdx.game.utilities.Animator;
import com.mygdx.game.utilities.Collision;
import com.mygdx.game.weapons.Projectile;
import com.mygdx.game.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyHandler {
    public SpriteBatch spriteBatch;
    private ArrayList<Enemies> enemies; // Change type to Enemies
    private Random random;
    private Animator animator;

    // HP PERCENTAGE INCREASE EVERY AFTER WAVE;
    double hpPercentage;

    // SPAWN TIME
    private long lastSpawnTime;
    private static final long SPAWN_INTERVAL = 5000; // 5 seconds in milliseconds

    // ZOMBIE TEXTURES
    private Texture[] zombieTextures;

    // DEBUGGING
    private Array<Projectile> projectiles;
    private ShapeRenderer shapeRenderer;
    private Collision enemyCollision;
    private Thread enemyCollisionThread;
    private Weapon weapon;
    private Player player;

    private float stateTime;

    private Array<Enemies> dead_enemies;

    //animations
    private Texture zombie_1_run;
    private Texture zombie_1_run_inverse;
    private Texture zombie_2_run;
    private Texture zombie_2_run_inverse;
    private Texture zombie_3_run;
    private Texture zombie_3_run_inverse;

    public EnemyHandler(Weapon weapon, Player player) {
        spriteBatch = new SpriteBatch();
        random = new Random();
        enemies = new ArrayList<>();
        animator = new Animator();

        lastSpawnTime = TimeUtils.millis();
        this.player = player;
        hpPercentage = 1;

        // Initialize zombie textures
        zombieTextures = new Texture[] {
                new Texture(Gdx.files.internal("assets/enemies/Zombie 1/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 2/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 3/Idle/0_Zombie_Villager_Idle_000.png"))
        };

        zombie_1_run = new Texture(Gdx.files.internal("animations/zombie_1/zombie_run.png"));
        zombie_1_run_inverse = new Texture(Gdx.files.internal("animations/zombie_1/zombie_run_inverse.png"));
        zombie_2_run = new Texture(Gdx.files.internal("animations/zombie_2/zombie_2_run.png"));
        zombie_2_run_inverse = new Texture(Gdx.files.internal("animations/zombie_2/zombie_2_run_inversed.png"));
        zombie_3_run = new Texture(Gdx.files.internal("animations/zombie_3/zombie_3_run.png"));
        zombie_3_run_inverse = new Texture(Gdx.files.internal("animations/zombie_3/zombie_3_run_inversed.png"));

        // DEBUGGING
//        spawnEnemies();
        this.weapon = weapon;
        projectiles = weapon.getProjectiles();
        shapeRenderer = new ShapeRenderer();
        enemyCollision = new Collision();

        dead_enemies = new Array<>();
        stateTime = 0f;
    }

    public void setHealthEnemies(int currentWave) {
        if(hpPercentage == 1) {
            hpPercentage = 1.2;
        }else {
            hpPercentage = Math.pow(1.2, currentWave - 1);
        }
    }

    public void handleWave(OrthographicCamera camera, int waveTimer) {
        spriteBatch.begin();
        stateTime += Gdx.graphics.getDeltaTime() * 0.45f;

        // DEBUGGING
        if (TimeUtils.timeSinceMillis(lastSpawnTime) >= SPAWN_INTERVAL) {
            if (waveTimer != 30) {
                spawnEnemies();
                lastSpawnTime = TimeUtils.millis();
            }
        }

        if (waveTimer == 0) {
            enemies.clear();  // Clear the entire list safely
        }

        // CHECK ZOMBIE TAKING DAMAGE OR DEAD
        projectiles = weapon.getProjectiles();
        Projectile collided_projectile = enemyCollision.enemyCollision(projectiles, enemies, weapon.damage , weapon.getDamage());
        if(collided_projectile != null){
            projectiles.removeValue(collided_projectile, true);
        }

        // HANDLES DEAD ENEMIES
        List<Enemies> deadEnemies = new ArrayList<>(); // Temporary list to store dead enemies
        for(Enemies enemy: enemies){ // Iterate over Enemies, not Zombie1
            if(!enemy.isAlive()){
                deadEnemies.add(enemy); // Add dead enemies to the temporary list
            }
        }
        handleDeadEnemies(deadEnemies); // Pass the list to handleDeadEnemies method

        //DEBUGGING
        handleEnemyMovement();

        spriteBatch.setProjectionMatrix(camera.combined);

        // RENDERS FLOATING DAMAGE
        enemyCollision.renderFloatingDamages(spriteBatch);

        // DEBUGGING
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);

        for (Enemies enemy : enemies) { // Iterate over Enemies, not Zombie1
            Rectangle enemy_hitbox = enemy.getEnemyHitbox();
            if (enemy instanceof Zombie1) {
                TextureRegion zombie = animator.animateZombie(getRunTexture(enemy)).getKeyFrame(stateTime, true);
                spriteBatch.draw(zombie, enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
            } else if (enemy instanceof Zombie2) {
                TextureRegion zombie = animator.animateZombie(getRunTexture(enemy)).getKeyFrame(stateTime, true);
                spriteBatch.draw(zombie, enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
            } else if (enemy instanceof Zombie3) {
                TextureRegion zombie = animator.animateZombie(getRunTexture(enemy)).getKeyFrame(stateTime, true);
                spriteBatch.draw(zombie, enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
            }
        }

        handlePlayerCollisions();

        spriteBatch.end();
        shapeRenderer.end();
    }

    public void handleEnemyMovement(){
        for(Enemies enemy: enemies){
            enemy.moveEnemyTowardsPlayer(player.getLocation(), spriteBatch);
        }
    }

    private Texture getRunTexture(Enemies enemy){
        if(enemy.getZombieNumber() == 0){
            return (player.getLocation().x < enemy.getPosition().x) ? zombie_1_run_inverse : zombie_1_run;
        }
        if(enemy.getZombieNumber() == 1){
            return (player.getLocation().x < enemy.getPosition().x) ? zombie_2_run_inverse : zombie_2_run;

        }
        if(enemy.getZombieNumber() == 2){
            return (player.getLocation().x < enemy.getPosition().x) ? zombie_3_run_inverse : zombie_3_run;
        }
        return null;
    }

    public void spawnEnemies() {
        for (int i = 0; i < 4; i++) {
            int type = random.nextInt(3);
            Enemies enemy;
            switch (type) {
                case 1:
                    enemy = new Zombie2((int) (23 * hpPercentage), zombieTextures[type], rand());
                    break;
                case 2:
                    enemy = new Zombie3((int) (25 * hpPercentage), zombieTextures[type], rand());
                    break;
                default:
                    enemy = new Zombie1((int) (20 * hpPercentage), zombieTextures[type], rand());
                    break;
            }
            enemies.add(enemy);
        }
    }

    // REMOVES THE ENEMIES FROM ARRAYLIST WHEN ISALIVE IS FALSE
    private void handleDeadEnemies(List<Enemies> deadEnemies) {
        enemies.removeAll(deadEnemies); // Safely remove all dead enemies from the enemies list
    }

    private Vector2 rand() {
        Vector2 random_position = new Vector2();
        int random_x = random.nextInt(450 - 50) + 50;
        int random_y = random.nextInt(300 - 50) + 50;

        random_position.set(random_x, random_y);
        return random_position;
    }

    public boolean handlePlayerCollisions() {
        for (Enemies enemy : enemies) {
            if (enemyCollision.playerEnemyCollision(player, enemies)) {
                enemy.attack();
                System.out.println("Player Health: " + player.getCurrentHealth());
                return true;
            }
        }
        return false;
    }
}

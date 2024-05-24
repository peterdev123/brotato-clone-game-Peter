package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.player.Player;
import com.mygdx.game.utilities.Collision;
import com.mygdx.game.weapons.Projectile;
import com.mygdx.game.weapons.Weapon;

import java.util.ArrayList;
import java.util.Random;

public class EnemyHandler {
    public SpriteBatch spriteBatch;
    private ArrayList<Enemy> enemies;
    private Random random;

    //SPAWN TIME
    private long lastSpawnTime;
    private static final long SPAWN_INTERVAL = 5000; // 5 seconds in milliseconds

    // ZOMBIE TEXTURES
    private Texture[] zombieTextures;

    //DEBUGGING
    private Array<Projectile> projectiles;
    private ShapeRenderer shapeRenderer;
    private Collision enemyCollision;
    private Weapon weapon;
    private Player player;

    private Array<Enemy> dead_enemies;

    public EnemyHandler(Weapon weapon, Player player) {
        spriteBatch = new SpriteBatch();
        random = new Random();
        enemies = new ArrayList<>();
        lastSpawnTime = TimeUtils.millis();
        this.player = player;

        // Initialize zombie textures
        zombieTextures = new Texture[] {
                new Texture(Gdx.files.internal("assets/enemies/Zombie 1/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 2/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 3/Idle/0_Zombie_Villager_Idle_000.png"))
        };

        //DEBUGGING
        spawnEnemies();
        this.weapon = weapon;
        projectiles = weapon.getProjectiles();
        shapeRenderer = new ShapeRenderer();
        enemyCollision = new Collision();

        dead_enemies = new Array<>();
    }

    public void handleWave(OrthographicCamera camera) {
        spriteBatch.begin();

        //DEBUGGING
        if (TimeUtils.timeSinceMillis(lastSpawnTime) >= SPAWN_INTERVAL) {
            spawnEnemies();
            lastSpawnTime = TimeUtils.millis();
        }

        //CHECK ZOMBIE TAKING DAMAGE OR DEAD
        projectiles = weapon.getProjectiles();
        Projectile collided_projectile = enemyCollision.enemyCollision(projectiles, enemies, weapon.damage , weapon.getDamage());
        if(collided_projectile != null){
            projectiles.removeValue(collided_projectile, true);
        }

        //HANDLES DEAD ENEMIES
        for(Enemy enemy: enemies){
            if(!enemy.checkIsAlive()){
                dead_enemies.add(enemy);
            }
        }
        handleDeadEnemies();

        //DEBUGGING
        handleEnemyMovement();

        spriteBatch.setProjectionMatrix(camera.combined);

        //RENDERS FLOATING DAMAGE
        enemyCollision.renderFloatingDamages(spriteBatch);

        //DEBUGGING
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);

        for (Enemy enemy : enemies) {
            Rectangle enemy_hitbox = enemy.getEnemyHitbox();
            spriteBatch.draw(enemy.enemy_texture, enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y);
//            shapeRenderer.rect(enemy_hitbox.x, enemy_hitbox.y, enemy_hitbox.width, enemy_hitbox.height);

        }
        spriteBatch.end();
        shapeRenderer.end();
    }

    //DEBUGGING
    public void handleEnemyMovement(){
        for(Enemy enemy: enemies){
            enemy.moveEnemyTowardsPlayer(player.getLocation(), spriteBatch);
        }
    }

    public void spawnEnemies() {
        for (int i = 0; i < 3; i++) {
            Texture randomTexture = zombieTextures[random.nextInt(zombieTextures.length)];
            enemies.add(new Enemy(randomTexture, rand()));
        }
    }

    //REMOVES THE ENEMIES FROM ARRAYLIST WHEN ISALIVE IS FALSE
    private void handleDeadEnemies(){
       for(Enemy dead_enemy: dead_enemies){
           enemies.remove(dead_enemy);
       }
    }

    private Vector2 rand() {
        Vector2 random_position = new Vector2();
        int random_x = random.nextInt(450 - 50) + 50;
        int random_y = random.nextInt(300 - 50) + 50;

        random_position.set(random_x, random_y);
        return random_position;
    }
}

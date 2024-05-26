package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enemies.Enemies;
import com.mygdx.game.enemies.Zombie1;
import com.mygdx.game.main.Map;
import com.mygdx.game.player.Player;
import com.mygdx.game.weapons.Projectile;

import java.util.ArrayList;

public class Collision {
    private MapObjects collision_objects;
    private MapObjects bullet_collision_objects;

    private Array<DamageIndicator> floatingDamages;

    public Collision(){
        collision_objects = new Map().getCollissionObjects();
        bullet_collision_objects = new Map().getBulletCollissionObjects();

        floatingDamages = new Array<>();
    }

    public void playerCollision(Rectangle player_bounds, Sprite character){
        for (MapObject object : collision_objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                // Check for collision
                if (Intersector.overlaps(player_bounds, rectangle)) {
                    // Determine the direction of collision (left, right, top, bottom)
                    float overlapX = Math.max(0, Math.min(player_bounds.x + player_bounds.width, rectangle.x + rectangle.width) - Math.max(player_bounds.x, rectangle.x));
                    float overlapY = Math.max(0, Math.min(player_bounds.y + player_bounds.height, rectangle.y + rectangle.height) - Math.max(player_bounds.y, rectangle.y));

                    // Adjust player's position based on the collision direction
                    if (overlapX < overlapY) {
                        // Horizontal collision
                        if (player_bounds.x < rectangle.x) {
                            // Collided from the left
                            character.setX(rectangle.x - player_bounds.width);
                        } else {
                            // Collided from the right
                            character.setX(rectangle.x + rectangle.width);
                        }
                    } else {
                        // Vertical collision
                        if (player_bounds.y < rectangle.y) {
                            // Collided from the bottom
                            character.setY(rectangle.y - player_bounds.height);
                        } else {
                            // Collided from the top
                            character.setY(rectangle.y + rectangle.height);
                        }
                    }
                }
            }
        }
    }

    //CHECKS FOR BULLET COLLISION WITH A WALL
    public boolean bulletCollision(Rectangle projectile){
        for (MapObject object : bullet_collision_objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                // Check for collision
                if (Intersector.overlaps(projectile, rectangle)) {
                    return true;
                }
            }
        }
        return false;
    }

    //CHECKS WHETHER ENEMY GOT HIT
    public Projectile enemyCollision(Array<Projectile> projectiles, ArrayList<Enemies> enemies, int base_damage, int damage){
        for(Projectile projectile : projectiles){
            for (Enemies enemy: enemies){
                if(Intersector.overlaps(projectile.rectangle, enemy.getEnemyHitbox())){
                    enemy.takeDamage(damage);
                    floatingDamages.add(new DamageIndicator(new Vector2(enemy.getPosition().x, enemy.getPosition().y), base_damage, damage));
                    return projectile;
                }
            }
        }
        return null;
    }

    //RENDERS DAMAGE TAKEN BY ENEMIES
    public void renderFloatingDamages(SpriteBatch batch) {
        for (int i = 0; i < floatingDamages.size; i++) {
            DamageIndicator floatingDamage = floatingDamages.get(i);
            if (floatingDamage.isExpired()) {
                floatingDamages.removeIndex(i);
                i--; // Adjust the index after removal
            } else {
                floatingDamage.render(batch);
            }
        }
    }

    public boolean playerEnemyCollision(Player player, ArrayList<Enemies> enemies) {
        for (Enemies enemy : enemies) {
            if (Intersector.overlaps(player.getPlayerHitbox(), enemy.getEnemyHitbox())) {
                player.decreaseHealth(enemy.getAttackPower());
                System.out.println("Player collided with Enemy! Health: " + player.getCurrentHealth());
                return true;  // Return true immediately after detecting a collision
            }
        }
        return false;
    }
}

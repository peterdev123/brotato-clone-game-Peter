package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.main.Map;

public class Collision {
    private MapObjects collision_objects;
    private MapObjects bullet_collision_objects;

    public Collision(){
        collision_objects = new Map().getCollissionObjects();
        bullet_collision_objects = new Map().getBulletCollissionObjects();
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

    //Checks for Bullet Collision with a wall
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
}

package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;


public class DamageIndicator {
    private Vector2 position;
    private int damage;
    private long creationTime;
    private static final long DURATION = 500; // Duration in milliseconds
    private BitmapFont font;

    public DamageIndicator(Vector2 position, int base_damage, int damage){
        this.position = new Vector2(position);
        this.damage = damage;
        this.creationTime = TimeUtils.millis();
        this.font = new BitmapFont();
        this.font.getData().setScale(0.5f);
        this.font.setColor(Color.WHITE);
        checkCrit(base_damage, damage);
    }

    public boolean isExpired() {
        return TimeUtils.timeSinceMillis(creationTime) > DURATION;
    }

    public void render(SpriteBatch batch) {
        float elapsed = TimeUtils.timeSinceMillis(creationTime) / 1000.0f;
        position.y += elapsed * 5; // Move the number upwards over time
        font.draw(batch, String.valueOf(damage), position.x, position.y);
    }

    public void checkCrit(int base_damage, int damage_dealt){
        if(damage_dealt >= (base_damage + 3)){
            this.font.getData().setScale(0.7f);
            this.font.setColor(Color.RED);
        }
    }

    public void dispose() {
        font.dispose();
    }
}

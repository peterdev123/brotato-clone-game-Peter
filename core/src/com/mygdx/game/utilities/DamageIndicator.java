package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.player.Player;

public class DamageIndicator extends Thread {
    private Vector2 position;
    private int damage;
    private long creationTime;
    private static final long DURATION = 500; // Duration in milliseconds
    private BitmapFont font;
    private float damageMultiplier;
    private SpriteBatch batch;

    public DamageIndicator(Vector2 position, int baseDamage, int damage) {
        this.position = new Vector2(position);
        this.damage = damage;
        this.creationTime = TimeUtils.millis();
        this.font = new BitmapFont();
        this.font.getData().setScale(0.5f);
        this.font.setColor(Color.WHITE);
        checkCrit(baseDamage, damage);
    }

    @Override
    public void run() {
        render(this.batch);
    }

    public boolean isExpired() {
        return TimeUtils.timeSinceMillis(creationTime) > DURATION;
    }

    public void setDamageMultiplier() {
        this.damageMultiplier = Player.damage_multiplier;
    }

    public void render(SpriteBatch batch) {
        float elapsed = TimeUtils.timeSinceMillis(creationTime) / 1000.0f;
        position.y += elapsed * 5; // Move the number upwards over time
        font.draw(batch, String.valueOf(damage), position.x, position.y);
    }

    public void checkCrit(int base_damage, int damage_dealt){
        setDamageMultiplier();
        if(damage_dealt >= ((base_damage * damageMultiplier) + 3)){
            this.font.getData().setScale(0.7f);
            this.font.setColor(Color.RED);
        }
        Player.totalScore += damage_dealt;
    }

    public void dispose() {
        font.dispose();
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }
}

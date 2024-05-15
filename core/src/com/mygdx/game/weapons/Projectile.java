package com.mygdx.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Projectile{
    private Texture projectile_texture;
    public Vector2 position;
    public Vector2 direction;
    public final float angle;

    public Projectile(Vector2 position, Vector2 direction, float angle){
        projectile_texture = new Texture(Gdx.files.internal("bullets/12.png"));
        this.position = position;
        this.direction = direction;
        this.angle = angle;

    }

    public TextureRegion getProjectileTexture(){
        return new TextureRegion(projectile_texture);
    }

    public void shootProjectile(float delta){
        float speed = 1000.0f;
        position.add(direction.x * delta * speed, direction.y * delta * speed);
    }
}

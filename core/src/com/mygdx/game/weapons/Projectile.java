package com.mygdx.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utilities.Collision;

public class Projectile{
    public Texture projectile_texture;
    public Vector2 position;
    public Vector2 direction;
    public final float angle;

    //BULLET COLLISION
    public Rectangle rectangle;
    public Collision collision;

    //DEBUGGING
    public ShapeRenderer shapeRenderer;

    public Projectile(Vector2 position, Vector2 direction, float angle){
        projectile_texture = new Texture(Gdx.files.internal("bullets/13.png"));
        this.position = position;
        this.direction = direction;
        this.angle = angle;

        //BULLET COLLISION
        rectangle = new Rectangle();
        collision = new Collision();

        //DEBUGGING
        shapeRenderer = new ShapeRenderer();
    }

    public TextureRegion getProjectileTexture(){
        return new TextureRegion(projectile_texture);
    }

    public boolean shootProjectile(float delta, OrthographicCamera camera){
        float speed = 300.0f;
        position.add(direction.x * delta * speed, direction.y * delta * speed);

        rectangle.set(position.x, position.y, 20, 20);

        //DEBUGGING
//            shapeRenderer.setProjectionMatrix(camera.combined);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.rect(position.x, position.y, rectangle.getWidth(), rectangle.getHeight());
//            shapeRenderer.end();

        return collision.bulletCollision(rectangle);

    }
}

package com.mygdx.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.utilities.Collision;
import com.mygdx.game.utilities.Rumble;
import com.sun.org.apache.bcel.internal.Const;

public class Weapon{
    public Texture current_weapon;
    public Array<Projectile> projectiles;
    public ShapeRenderer shapeRenderer;

    //COLLISION
    public Collision collision;

    public Weapon(){
        current_weapon = new Texture(Gdx.files.internal("assets/Weapons/weaponR1.png"));
        projectiles = new Array<>();
        shapeRenderer = new ShapeRenderer();

        collision = new Collision();
    }

    public TextureRegion getWeapon(){
        return new TextureRegion(current_weapon);
    }

    public TextureRegion flippedWeapon(){
        TextureRegion flipped_weapon = new TextureRegion(current_weapon);
        flipped_weapon.flip(true, false);
        return flipped_weapon;
    }

    //Change Weapon Test
    public void test(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            current_weapon = new Texture(Gdx.files.internal("assets/Weapons/weaponR1.png"));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            current_weapon = new Texture(Gdx.files.internal("assets/Weapons/weaponR2.png"));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            current_weapon = new Texture(Gdx.files.internal("assets/Weapons/weaponR3.png"));
        }
    }

    //TODO: Make bullet disappear when hitting a wall
    public void handleWeapon(OrthographicCamera camera, SpriteBatch spriteBatch, float char_x, float char_y) {
        Vector3 unprojectedPosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        float characterX;
        float characterY;

        float deltaX;
        float deltaY;

        float angle;

        float weaponWidth = 30;
        float weaponHeight = 30;


        if(!checkDirectionFacing(camera, char_x)){
            TextureRegion weapon = getWeapon();
            characterX = char_x + 6;
            characterY = char_y + 15;

            // Calculate the angle between the character and the target position
            deltaX = (unprojectedPosition.x) - characterX;
            deltaY = (unprojectedPosition.y) - characterY;

            angle = (float) ((float) Math.atan2(deltaY, deltaX) * 180 / Math.PI);

            spriteBatch.draw(weapon,
                    characterX - weaponWidth / 2, characterY - weaponHeight / 2, // Position
                    weaponWidth / 2, weaponHeight / 2, // Origin for rotation (center of the weapon)
                    weaponWidth, weaponHeight, // Width and height
                    1, 1, // Scale
                    angle);

        }
        else{
            TextureRegion weapon = flippedWeapon();
            characterX = char_x - 2;
            characterY = char_y + 15;

            // Calculate the angle between the character and the target position
            deltaX = unprojectedPosition.x - characterX;
            deltaY = unprojectedPosition.y - characterY;

            angle = ((float) ((float) Math.atan2(deltaY, deltaX) * 180 / Math.PI)) + 180;

            spriteBatch.draw(weapon,
                    characterX - weaponWidth / 2, characterY - weaponHeight / 2, // Position
                    weaponWidth / 2, weaponHeight / 2, // Origin for rotation (center of the weapon)
                    weaponWidth, weaponHeight, // Width and height
                    1, 1, // Scale
                    angle);
        }

        if(!checkDirectionFacing(camera, char_x)){
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                Vector2 direction = new Vector2(unprojectedPosition.x - char_x, unprojectedPosition.y - char_y - 10).nor();
                projectiles.add(new Projectile(new Vector2(char_x, char_y), direction, angle));
                Rumble.rumble(2, .2f);
            }

            for(Projectile proj : projectiles){
                proj.shootProjectile(Gdx.graphics.getDeltaTime(), camera);
            }

            for(int i = 0; i < projectiles.size; i++){
                Projectile proj = projectiles.get(i);

                //removes bullet when projectile hits wall
                if(proj.shootProjectile(Gdx.graphics.getDeltaTime(), camera)){
                    projectiles.removeIndex(i);
                    i--;
                }
                else{
                    spriteBatch.draw(proj.getProjectileTexture(),
                            proj.position.x, proj.position.y, // Position
                            (weaponWidth - 20) / 2, (weaponHeight - 10) / 2, // Origin for rotation (center of the weapon)
                            20, 20, // Width and height
                            1, 1, // Scale
                            proj.angle);
                }
            }
        }
        else{
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                Vector2 direction = new Vector2(unprojectedPosition.x - char_x, unprojectedPosition.y - char_y - 10).nor();
                projectiles.add(new Projectile(new Vector2(char_x, char_y), direction, angle));
                Rumble.rumble(2, .2f);
            }

            for(Projectile proj : projectiles){
                proj.shootProjectile(Gdx.graphics.getDeltaTime(), camera);
            }

            for(int i = 0; i < projectiles.size; i++){
                Projectile proj = projectiles.get(i);
                TextureRegion projectile = proj.getProjectileTexture();
                projectile.flip(true, false);

                //removes bullet when projectile hits wall
                if(proj.shootProjectile(Gdx.graphics.getDeltaTime(), camera)){
                    projectiles.removeIndex(i);
                    i--;
                }
                else{
                    spriteBatch.draw(projectile,
                            proj.position.x, proj.position.y, // Position
                            (weaponWidth - 20) / 2, (weaponHeight - 10) / 2, // Origin for rotation (center of the weapon)
                            20, 20, // Width and height
                            1, 1, // Scale
                            proj.angle);
                }


                //DEBUGGING
//                collision.bulletCollision(proj);
//                shapeRenderer.setProjectionMatrix(camera.combined);
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                shapeRenderer.setColor(Color.RED);
//                shapeRenderer.rect(proj.position.x, proj.position.y, proj.rectangle.getWidth(), proj.rectangle.getHeight());
//                shapeRenderer.end();
            }
        }
        if (Rumble.getRumbleTimeLeft() > 0){
            Rumble.tick(Gdx.graphics.getDeltaTime());
            camera.translate(Rumble.getPos());
        }
    }

    private boolean checkDirectionFacing(OrthographicCamera camera, float char_x){
        Vector3 position = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return !(position.x - char_x > 0);
    }


}

package com.mygdx.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.player.Player;

public class Weapon{
    public Texture current_weapon;

    public Weapon(){
        current_weapon = new Texture(Gdx.files.internal("assets/Weapons/weaponR1.png"));
    }

    public TextureRegion getWeapon(){

        return new TextureRegion(current_weapon);
    }

    public TextureRegion flippedWeapon(){
        TextureRegion flipped_weapon = new TextureRegion(current_weapon);
        flipped_weapon.flip(true, false);
        return flipped_weapon;
    }


}

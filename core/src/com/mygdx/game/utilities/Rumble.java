package com.mygdx.game.utilities;

import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Rumble extends Thread{
    private float time = 0;
    private float currentTime = 0;
    private float power = 0;
    private float currentPower = 0;
    private float delta;
    private Random random;
    private Vector3 pos = new Vector3();

    public void rumble(float rumblePower, float rumbleLength) {
        random = new Random();
        power = rumblePower;
        time = rumbleLength;
        currentTime = 0;
    }

    @Override
    public void run() {
        tick();
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public void tick() {
        if (currentTime <= time) {
            currentPower = power * ((time - currentTime) / time);

            pos.x = (random.nextFloat() - 0.5f) * 2 * currentPower;
            pos.y = (random.nextFloat() - 0.5f) * 2 * currentPower;

            currentTime += delta;
        } else {
            time = 0;
        }
    }

    public float getRumbleTimeLeft() {
        return time;
    }

    public Vector3 getPos() {
        return pos;
    }
}


package com.mygdx.game.utilities;

import com.mygdx.game.Screens.Intermession;
import com.mygdx.game.main.World;

public class WaveHandler extends Thread {
    private int currentWave = 1;
    private int waveTimer = 30; // Duration of each wave in seconds
    private boolean running = true;
    private boolean paused = false;

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                if (!paused) {
                    waveTimer -= 1;
//                    if (waveTimer <= -1) {
//                        currentWave++;
//                        waveTimer = 5; // Reset the timer for the next wave
//                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getWaveTimer() {
        return waveTimer;
    }

    public void stopTimer() {
        running = false;
    }

    public void setWaveTimer(int time) {
        waveTimer = time;
    }

    public void setWave(int wave) {
        currentWave += wave;
    }

    public void pauseTimer() {
        paused = true;
    }

    public void resumeTimer() {
        paused = false;
    }
}



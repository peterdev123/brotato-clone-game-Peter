package com.mygdx.game.utilities;

public class WaveHandler extends Thread {
    private int currentWave = 1;
    private int waveTimer = 30; // Duration of each wave in seconds
    private boolean running = true;
    private boolean paused = false;
    private boolean headStart = true;

    @Override
    public void run() {
        while (running) {
            if (headStart) {
                if (!paused) {
                    try {
                        Thread.sleep(5000); // Headstart for 3 seconds
                        headStart = false; // Headstart done
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // If paused, don't decrement waveTimer during headstart
                    continue;
                }
            }

            try {
                synchronized (this) {
                    while (paused) {
                        wait(); // Wait until notified (paused becomes false)
                    }
                }

                Thread.sleep(1000); // Sleep for 1 second
                waveTimer -= 1;

                // Check wave timer and handle wave logic here if needed

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Reset wave timer and apply headstart before the next wave
            if (waveTimer <= 0) {
//                currentWave++;
//                waveTimer = 30; // Reset wave timer
                headStart = true; // Apply headstart before the next wave
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
        resumeTimer(); // Ensure any waiting thread is resumed before stopping
    }

    public void setWaveTimer(int time) {
        waveTimer = time;
    }

    public void setWave(int wave) {
        currentWave += wave;
    }

    public synchronized void pauseTimer() {
        paused = true;
    }

    public synchronized void resumeTimer() {
        paused = false;
        notify(); // Notify any waiting thread
    }

    @Override
    public synchronized void start() {
        super.start(); // Start the thread
    }

}

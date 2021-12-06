package Game;

import StartUpServer.AppServer;

public abstract class GameLoop implements Runnable {
    private final double frameRate;
    private boolean running = true;

    public GameLoop(int frameRate) {
        this.frameRate = frameRate;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
        thread.setName("game loop");
    }

    @Override
    public void run() {
        double ns = 1000000000.0 / frameRate;//nano secs per frame
        double delta = 0;
        long lastTime = System.nanoTime();
//        long timer = System.currentTimeMillis();
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                handle();
                delta--;
            }
            if (!running) {
                AppServer.currentGame = null;
                return;
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public abstract void handle();

    public boolean isRunning() {
        return running;
    }
}

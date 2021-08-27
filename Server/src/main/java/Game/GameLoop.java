package Game;

public abstract class GameLoop  implements Runnable{
    private final double frameRate;

    public GameLoop(int frameRate) {
        this.frameRate = frameRate;
    }

    public void start(){
        new Thread(this).start();
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
        }
    }

    public abstract void handle();
}

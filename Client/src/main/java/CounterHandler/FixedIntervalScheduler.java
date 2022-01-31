package CounterHandler;

public abstract class FixedIntervalScheduler {
    private volatile boolean running = true;

    //interval in millis
    public void schedule(int interval){
        new Thread(()->{
            double timeOfLastInterval = System.currentTimeMillis();
            while (running){
                double delta =System.currentTimeMillis() - timeOfLastInterval;
                if (delta >= interval){
                    timeOfLastInterval = System.currentTimeMillis();
                    this.run();
                }
            }
        }).start();

    }

    public abstract void run();

    public  void cancel(){
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

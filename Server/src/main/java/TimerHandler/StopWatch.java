package TimerHandler;

public class StopWatch {
    private double initialTime;
    private double endTime;


    public void start(){
        this.initialTime = System.nanoTime();
    }

    public double stop(){
        this.endTime = System.nanoTime();
        return endTime - initialTime;
    }

    public double getTimeDifference(){
        return endTime - initialTime;
    }

//    public enum Precision{
//        SECONDS,
//        MILLIS,
//        NANOS
//    }


}

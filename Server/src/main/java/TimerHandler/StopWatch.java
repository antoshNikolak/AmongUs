package TimerHandler;

public class StopWatch {
    private double initialTime;
    private double endTime;
    private final Precision precision;

    public StopWatch(Precision precision) {
        this.precision = precision;
    }

    public void start(){
        this.initialTime = precision.getTime();
    }

    public double stop(){
        this.endTime = precision.getTime();
        return endTime - initialTime;
    }

    public double getTimeDifference(){
        return endTime - initialTime;
    }

    public enum Precision{
        SECONDS{
            @Override
            public Double getTime() {
                return (double)Math.round(System.currentTimeMillis()/10e3);
            }
        },
        MILLIS{
            @Override
            public Double getTime() {
                return (double)System.currentTimeMillis();
            }
        },
        NANOS{
            @Override
            public Double getTime() {
                return (double)System.nanoTime();
                // Returns the current value of the most precise available system timer, in nanoseconds.
//                This method provides nanosecond precision, but not necessarily nanosecond accuracy.
            }
        };

        public abstract Double getTime();
    }


}

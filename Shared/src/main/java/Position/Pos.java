package Position;

public class Pos {
    private double x,y, timeStamp;

    private Pos(){}

    public Pos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Pos(double x, double y, double timeStamp) {
        this.x = x;
        this.y = y;
        this.timeStamp = timeStamp;
    }

    public Pos(Pos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.timeStamp = pos.getTimeStamp();
    }

    public void setPos(Pos pos){
        this.x = pos.getX();
        this.y = pos.getY();
    }

    @Override
    public String toString() {
        return "x: "+x +
                " y: "+y;
    }

    public void incrementX(double distance){
        this.x+=distance;
    }

    public void incrementY(double distance){
        this.y+=distance;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp = timeStamp;
    }
}

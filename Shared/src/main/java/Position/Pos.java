package Position;

import java.util.Objects;

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

    //todo document overriden .equals value, not comparing time stamp as it doesnt define this object
    //I DID this because when verifying the sudoku in sudoku task state, I would add multiple positions with same pos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return Double.compare(pos.x, x) == 0 && Double.compare(pos.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    //    @Override
//    public int hashCode() {
//        return Objects.hash(x, y, timeStamp);
//    }

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

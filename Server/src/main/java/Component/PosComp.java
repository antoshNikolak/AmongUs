package Component;

import Position.Pos;

public class PosComp implements Component{
    private double  width, height;
    private final Pos pos;

    public PosComp(double x, double y, double width, double height) {
        this.pos = new Pos(x, y);
        this.width = width;
        this.height = height;
    }

    public PosComp(Pos pos, double width, double height) {
        this.width = width;
        this.height = height;
        this.pos = pos;
    }



    public void setPos(Pos pos){
        this.pos.setPos(pos);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    public Pos getPos() {
        return pos;
    }
}

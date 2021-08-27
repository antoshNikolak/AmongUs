package Component;

public class VelComp implements Component{
    private double velX = 0;
    private double velY = 0;
    private double previousVelX = 0;
    private double previousVelY = 0;



    public VelComp(double velX, double velY) {
        this.velX = velX;
        this.velY = velY;
    }

    public VelComp() {}

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getPreviousVelX() {
        return previousVelX;
    }

    public void setPreviousVelX(double previousVelX) {
        this.previousVelX = previousVelX;
    }

    public double getPreviousVelY() {
        return previousVelY;
    }

    public void setPreviousVelY(double previousVelY) {
        this.previousVelY = previousVelY;
    }
}

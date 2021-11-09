package Math;

import Position.Pos;

public class CircleProperty {
    private double radius;
    private Pos centre;

    public CircleProperty(double radius, Pos centre) {
        this.radius = radius;
        this.centre = centre;
    }

    public double [] getYValues(double x){
        double b = -2 * centre.getY();
        double c = (centre.getY() * centre.getY()) -((radius * radius) - Math.pow(x - centre.getX(), 2));
        return QuadraticSolver.getSolutions(1, b, c);
    }

    public double [] getXValues(double y){
        double b = -2 * centre.getX();
        double c = (centre.getY() * centre.getY()) -(radius * radius) +  Math.pow(y - centre.getY(), 2);
        return QuadraticSolver.getSolutions(1, b, c);
    }

    public Pos getCentre() {
        return centre;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setCentre(Pos centre) {
        this.centre = centre;
    }
}

package Math;

public class QuadraticSolver {

    public static double[] getSolutions(double a, double b, double c){
        double sqrt = Math.sqrt(b * b - 4 * a * c);
        double v1 = ((-b + sqrt)/(2 *a));
        double v2 = ((-b - sqrt)/(2 *a));
        return new double[]{v1, v2};
    }


}

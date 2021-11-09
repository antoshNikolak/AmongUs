package Math;

public class QuadraticSolver {

    public static double[] getSolutions(double a, double b, double c){
        double sqrt = Math.sqrt(b * b - 4 * a * c);
        double x1 = ((-b + sqrt)/(2 *a));
        double x2 = ((-b - sqrt)/(2 *a));
        return new double[]{x1, x2};
    }


}

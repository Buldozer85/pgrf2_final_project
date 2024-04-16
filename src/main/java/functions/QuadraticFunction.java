package functions;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class QuadraticFunction implements UnivariateFunction {

    private double a;
    private double b;
    private double c;

    public static final int QUADRATIC_COEFFICIENT = 1;
    public static final int LINEAR_COEFFICIENT = 2;
    public static final int CONSTANT_COEFFICIENT = 3;

    public final static String regex = "^\\s*y\\s*=\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*x\\s*\\^2\\s*([+-]?\\s*\\d+\\.?\\d*)?x\\s*([+-]?\\s*\\d+\\.?\\d*)?$";

    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    @Override
    public double value(double x) {
        return (a * Math.pow(x, 2)) + (b * x) + c;
    }
}

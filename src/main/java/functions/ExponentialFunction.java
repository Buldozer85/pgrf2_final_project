package functions;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class ExponentialFunction implements UnivariateFunction {
    private final double a; // Amplituda funkce
    private final double b; // Exponent

    public ExponentialFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }
    public static final String regex = "^y\\s*=\\s*(\\d*\\.?\\d+)\\s*\\*?\\s*e\\^\\(([-+]?\\d*\\.?\\d+)x\\)$";
    @Override
    public double value(double x) {
        return a * Math.exp(b * x);
    }
}

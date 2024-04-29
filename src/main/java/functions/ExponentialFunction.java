package functions;

import interfaces.FunctionInterface;

public class ExponentialFunction implements FunctionInterface {
    private final double a; // Amplituda funkce
    private final double b; // Exponent

    public static final int AMPLITUDE = 1;
    public static final int EXPONENT = 2;

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

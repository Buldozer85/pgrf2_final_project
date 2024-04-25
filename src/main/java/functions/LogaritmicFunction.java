package functions;

import interfaces.FunctionInterface;
import org.apache.commons.math3.analysis.UnivariateFunction;

public class LogaritmicFunction implements FunctionInterface {

    private double a;
    private double base;

    public static int AMPLITUDE = 1;
    public static int BASE = 2;

    public static final String regex = "^y\\s*=\\s*log_(\\d*\\.?\\d*)\\((\\d*\\.?\\d*)x\\)$";

    public LogaritmicFunction(double a, double base) {
        this.a = a;
        this.base = base;
    }

    @Override
    public double value(double x) {
        return  Math.log(a * x) / Math.log(base);
    }
}

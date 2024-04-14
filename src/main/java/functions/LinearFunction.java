package functions;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class LinearFunction implements UnivariateFunction {
    private final double slope;
    private final double intercept;

    public static final String regex = "^\\s*y\\s*=\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*x\\s*([+-]?\\s*\\d+\\.?\\d*)?\\s*$";

    public LinearFunction(double slope, double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    @Override
    public double value(double x) {
        return slope * x + intercept;
    }
}

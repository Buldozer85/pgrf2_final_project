package functions;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class LogaritmicFunction implements UnivariateFunction {

    private double a;
    private double base;

    public LogaritmicFunction(double a, double base) {
        this.a = a;
        this.base = base;
    }

    @Override
    public double value(double x) {
        return a*  Math.log(x) / Math.log(base);
    }
}

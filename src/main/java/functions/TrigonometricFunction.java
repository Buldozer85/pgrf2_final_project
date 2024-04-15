package functions;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class TrigonometricFunction implements UnivariateFunction {

    private double amplitude;
    private double frequency;
    private double phaseShift;
    private double verticalShift;

    private String type;

    private static final String regex = "^\\s*y\\s*=\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*(sin|cos)\\s*\\(\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*x\\s*\\+?\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\)\\s*([+-]?\\s*\\d+\\.?\\d*)?$";

    public TrigonometricFunction(double amplitude, double frequency, double phaseShift, double verticalShift, String type) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phaseShift = phaseShift;
        this.verticalShift = verticalShift;
        this.type = type;
    }

    @Override
    public double value(double x) {
        switch (type) {
            case "sin":
                return amplitude * Math.sin(frequency * x + phaseShift) + verticalShift;
            case "cos":
                return amplitude * Math.cos(frequency * x + phaseShift) + verticalShift;
        }
        return 0;
    }
}

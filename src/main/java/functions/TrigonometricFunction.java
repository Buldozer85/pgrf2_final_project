package functions;

import interfaces.FunctionInterface;

public class TrigonometricFunction implements FunctionInterface {

    private double amplitude;
    private double frequency;
    private double phaseShift;
    private double verticalShift;

    private String type;

    public static final int AMPLITUDE = 1;
    public static final int TYPE = 2;
    public static final int FREQUENCY = 3;
    public static final int PHASE_SHIFT = 4;
    public static final int VERTICAL_SHIFT = 5;

    public static final String regex = "^\\s*y\\s*=\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*(sin|cos|tan|cot)\\s*\\(\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*x\\s*\\+?\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\)\\s*([+-]?\\s*\\d+\\.?\\d*)?$";

    public TrigonometricFunction(double amplitude, double frequency, double phaseShift, double verticalShift, String type) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phaseShift = phaseShift;
        this.verticalShift = verticalShift;
        this.type = type;
    }

    @Override
    public double value(double x) {
        return switch (type) {
            case "sin" -> amplitude * Math.sin(frequency * x + phaseShift) + verticalShift;
            case "cos" -> amplitude * Math.cos(frequency * x + phaseShift) + verticalShift;
            case "tan" -> amplitude * Math.tan(frequency * x + phaseShift) + verticalShift;
            case "cot" -> amplitude / Math.tan(frequency * x + phaseShift) + verticalShift;
            default -> throw new IllegalArgumentException("Unsupported trigonometric function: " + type);
        };
    }

}

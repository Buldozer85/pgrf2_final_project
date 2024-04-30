package functions;

import interfaces.FunctionInterface;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class QuadraticFunction implements FunctionInterface {

    private double a;
    private double b;
    private double c;
    private Expression expression;

    public static final int QUADRATIC_COEFFICIENT = 1;
    public static final int LINEAR_COEFFICIENT = 2;
    public static final int CONSTANT_COEFFICIENT = 3;

    public final static String regex = "^\\s*y\\s*=\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*x\\s*\\^2\\s*([+-]?\\s*\\d+\\.?\\d*)?x?\\s*([+-]?\\s*\\d+\\.?\\d*)?$";

    public QuadraticFunction(String expression) {
        this.expression = new ExpressionBuilder(expression)
                .variables("x")
                .build();
    }
    @Override
    public double value(double x) {
        return this.expression.setVariable("x", x).evaluate();
    }
}

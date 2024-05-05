package functions;

import interfaces.FunctionInterface;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
public class QuadraticFunction implements FunctionInterface {

    private final Expression expression;

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

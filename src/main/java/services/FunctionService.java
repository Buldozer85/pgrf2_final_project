package services;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionService {
    public static String replaceAbsExpression(String expression) {
        int startPos = expression.lastIndexOf('|');
        while (startPos >= 0) {
            int endPos = expression.indexOf('|', startPos + 1);
            if (endPos == -1) {
                throw new IllegalArgumentException("Nesprávně uzavřené svislé čáry.");
            }
            String innerExpression = expression.substring(startPos + 1, endPos);
            String absExpression = "abs(" + innerExpression + ")";
            expression = expression.substring(0, startPos) + absExpression + expression.substring(endPos + 1);
            startPos = expression.lastIndexOf('|', startPos - 1);
        }
        return expression;
    }
}

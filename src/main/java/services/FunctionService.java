package services;

import helpers.TextHelper;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionService {
    public static String replaceAbsExpression(String expression) {
        String regex = "\\|([^|]+?)\\|";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        // Nahrazení všech výskytů výrazů mezi svislými čarami za abs()
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String innerExpression = matcher.group(1);
            String absExpression = "abs(" + innerExpression + ")";
            matcher.appendReplacement(sb, absExpression);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static boolean hasAbsExpression(String expression) {
        String regex = "\\|([^|]+?)\\|";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        return matcher.find() && (TextHelper.countSpecificChars(expression, '|') % 2 == 0);
    }
}

package services;

import java.util.regex.Matcher;

public class RegexService {
    public static String extractGroupValue(Matcher matcher, int group, boolean defaultZero) {
        if (matcher.group(group) == null || matcher.group(group).isEmpty()) {
            return defaultZero ?  "0" : "1";
        }

        if(matcher.group(group).equals("-")) {
            return defaultZero ?  "0" : "-1";
        }

        return matcher.group(group).replace(" ", "");
    }
}

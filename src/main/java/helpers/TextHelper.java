package helpers;

public class TextHelper {
    public static int countSpecificChars(String text, char targetChar) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == targetChar) {
                count++;
            }
        }

        return count;
    }
}

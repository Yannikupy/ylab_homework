package hw2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnilsValidatorImpl implements SnilsValidator {

    @Override
    public boolean validate(String snils) {
        if (snils == null || snils.length() != 11) {
            return false;
        }
        String regex = ".*[a-zA-Z].*";  // regex to check if string contains any letters
        Pattern pattern = Pattern.compile(regex);
        Matcher matcherText = pattern.matcher(snils);
        if (matcherText.matches()) {
            return false;
        }
        int multiplicationSum = 0;
        int controlNumber;
        for (int i = 0; i < 9; ++i) {
            multiplicationSum += Character.digit(snils.charAt(i), 10) * (9 - i);
        }
        if (multiplicationSum < 100) {
            controlNumber = multiplicationSum;
        } else if (multiplicationSum == 100) {
            controlNumber = 0;
        } else {
            multiplicationSum %= 101;
            controlNumber = multiplicationSum == 100 ? 0 : multiplicationSum;
        }
        return controlNumber == Integer.parseInt(snils.substring(9, 11));
    }
}

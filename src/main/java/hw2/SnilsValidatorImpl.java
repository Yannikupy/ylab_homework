package hw2;

public class SnilsValidatorImpl implements SnilsValidator {

    @Override
    public boolean validate(String snils) {
        if (snils == null || snils.length() != 11) {
            return false;
        }
        if (!snils.matches("[0-9]+")) { // regex to check if string contains only numbers
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

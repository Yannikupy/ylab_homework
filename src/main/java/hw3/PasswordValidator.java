package hw3;

public class PasswordValidator {
    private static final String regex = "^[a-zA-Z0-9_]*$"; // only alphanumeric and underscores

    private static boolean validateLogin(String login) throws WrongLoginException {
        if (!login.matches(regex)) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        }
        if (login.length() >= 20) {
            throw new WrongLoginException("Логин слишком длинный");
        }
        return true;
    }

    private static boolean validatePassword(String password, String confirmPassword) throws WrongPasswordException {
        if (!password.matches(regex)) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        }
        if (password.length() >= 20) {
            throw new WrongPasswordException("Пароль слишком длинный");
        }
        if (!password.equals(confirmPassword)) {
            throw new WrongPasswordException("Пароль и подтверждение не совпадают");
        }
        return true;
    }

    public static boolean validate(String login, String password, String confirmPassword) {
        boolean validated = false;
        try {
            validated = validateLogin(login) && validatePassword(password, confirmPassword);
        } catch (WrongLoginException | WrongPasswordException e) {
            e.printStackTrace();
        }
        return validated;
    }
}

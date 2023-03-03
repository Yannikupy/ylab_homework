package hw1;

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void playGame(int number, int maxAttempts) {
        System.out.println(number);
        int curAttempt = 0;
        try (Scanner sc = new Scanner(System.in)) {
            while (curAttempt != maxAttempts) {
                int attempt = sc.nextInt();
                curAttempt++;
                if (attempt == number) {
                    System.out.println("Ты угадал с " + curAttempt + " попытки");
                    return;
                } else if (attempt < number) {
                    int remainingAttempts = maxAttempts - curAttempt;
                    System.out.println("Мое число больше! У тебя осталось " + remainingAttempts + " попыток");
                } else {
                    int remainingAttempts = maxAttempts - curAttempt;
                    System.out.println("Мое число меньше! У тебя осталось " + remainingAttempts + " попыток");
                }
            }
            System.out.println("Ты не угадал");
        }
    }

    public static void main(String[] args) throws Exception {
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
        playGame(number, maxAttempts);
    }

}

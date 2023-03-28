package hw1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Pell {

    private static int getPellNumberRecursively(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return 2 * getPellNumberRecursively(n - 1) + getPellNumberRecursively(n - 2);
    }

    private static int getPellNumberIteratively(int n) {
        List<Integer> pellNumbers = new ArrayList<>();
        pellNumbers.add(0);
        pellNumbers.add(1);
        for (int i = 2; i < n + 1; ++i) {
            pellNumbers.add(2 * pellNumbers.get(i - 1) + pellNumbers.get(i - 2));
        }
        return pellNumbers.get(n);
    }

    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            System.out.println(getPellNumberIteratively(n));
        }

    }
}

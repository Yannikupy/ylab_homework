package hw1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Pell {

    public static int getPellNumber(int n) {
        List<Integer> pellNumbers = new ArrayList<>();
        pellNumbers.add(0);
        pellNumbers.add(1);
        for (int i = 2; i < n + 1; ++i) {
            pellNumbers.add(2 * pellNumbers.get(i - 1) + pellNumbers.get(i - 2));
        }
        return pellNumbers.get(n);
    }

    public static void main(String[] args) throws Exception{
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            System.out.println(getPellNumber(n));
        }

    }
}

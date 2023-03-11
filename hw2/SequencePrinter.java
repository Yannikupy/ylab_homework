package hw2;

import java.util.function.Function;

public class SequencePrinter {
    public static void print(Function<Integer, Integer> function, int n) {
        for (int i = 1; i <= n; ++i) {
            System.out.print(function.apply(i) + " ");
        }
        System.out.println();
    }
}

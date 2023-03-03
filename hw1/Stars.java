package hw1;

import java.util.Scanner;

public class Stars {
    private static void drawFigure(int n, int m, String template) {
        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < m; ++j) {
                System.out.print(template + ' ');
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            drawFigure(n, m, template);
        }
    }
}

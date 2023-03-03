package hw1;

public class MultTable {

    private static void printMultTable() {
        for (int i = 1; i <= 9; ++i) {
            for (int j = 1; j <= 9; ++j) {
                System.out.println(i + " * " + j + " = " + i * j);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        printMultTable();
    }
}

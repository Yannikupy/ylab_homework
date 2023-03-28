package hw2;

import java.util.function.Function;
import java.util.stream.IntStream;

import static java.lang.Math.pow;

public class SequenceGeneratorImpl implements SequenceGenerator {

    private Function<Integer, Integer> generatingFunction;

    @Override
    public void a(int n) {
        generatingFunction = i -> 2 * i;
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void b(int n) {
        generatingFunction = i -> 2 * i - 1;
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void c(int n) {
        generatingFunction = i -> i * i;
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void d(int n) {
        generatingFunction = i -> i * i * i;
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void e(int n) {
        generatingFunction = i -> (int) pow(-1, i + 1);
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void f(int n) {
        generatingFunction = i -> (int) (i * pow(-1, i + 1));
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void g(int n) {
        generatingFunction = i -> (int) (i * i * pow(-1, i + 1));
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void h(int n) {
        generatingFunction = i -> (int) (0.25 * pow(-1, i) * (pow(-1, i) - 1) * (i + 1));
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void i(int n) {
        generatingFunction = i -> IntStream.rangeClosed(1, i).reduce(1, (x, y) -> x * y);
        SequencePrinter.print(generatingFunction, n);
    }

    @Override
    public void j(int n) {
        double phi = ((1 + Math.sqrt(5)) / 2);
        generatingFunction = i -> (int) ((pow(phi, i) - pow(-phi, -i)) / (2 * phi - 1));
        SequencePrinter.print(generatingFunction, n);
    }
}

package hw2;

public class ComplexNumber {

    private final double real;
    private double imagine;

    public ComplexNumber(double real) {
        this.real = real;
    }

    public ComplexNumber(double real, double imagine) {
        this.real = real;
        this.imagine = imagine;
    }

    public ComplexNumber add(ComplexNumber number) {
        return new ComplexNumber(real + number.real, imagine + number.imagine);
    }

    public ComplexNumber substract(ComplexNumber number) {
        return new ComplexNumber(real - number.real, imagine - number.imagine);
    }

    public ComplexNumber multiply(ComplexNumber number) {
        return new ComplexNumber(real * number.real - imagine * number.imagine,
                real * number.imagine + imagine * number.real);
    }

    public double abs() {
        return Math.sqrt(real * real + imagine * imagine);
    }

    @Override
    public String toString() {
        return "ComplexNumberImpl{" +
                "real=" + real +
                ", imagine=" + imagine +
                '}';
    }

}

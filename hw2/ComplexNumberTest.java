package hw2;

public class ComplexNumberTest {
    public static void main(String[] args) {
        ComplexNumber myNumber1 = new ComplexNumber(-2, 3);
        ComplexNumber myNumber2 = new ComplexNumber(1, 4);
        System.out.println(myNumber1);
        System.out.println(myNumber1.abs());
        System.out.println(myNumber1.add(myNumber2));
        System.out.println(myNumber1.substract(myNumber2));
        System.out.println(myNumber1.multiply(myNumber2));
    }
}

package hw2;

public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulator statsAccumulator = new StatsAccumulatorImpl();
        // Случай, когда пользователь еще не добавил числа.
        System.out.println(statsAccumulator.getMax());
        System.out.println(statsAccumulator.getMin());
        System.out.println(statsAccumulator.getCount());
        System.out.println(statsAccumulator.getAvg());

        statsAccumulator.add(1);
        System.out.println(statsAccumulator.getMax());
        System.out.println(statsAccumulator.getMin());
        System.out.println(statsAccumulator.getCount());
        System.out.println(statsAccumulator.getAvg());

        statsAccumulator.add(128);
        System.out.println(statsAccumulator.getMax());
        System.out.println(statsAccumulator.getMin());
        System.out.println(statsAccumulator.getCount());
        System.out.println(statsAccumulator.getAvg());

        statsAccumulator.add(-5);
        System.out.println(statsAccumulator.getMax());
        System.out.println(statsAccumulator.getMin());
        System.out.println(statsAccumulator.getCount());
        System.out.println(statsAccumulator.getAvg());
    }
}

package hw2;

public class StatsAccumulatorImpl implements StatsAccumulator {
    private int max;
    private int min;
    private int amount;
    private double avg;

    @Override
    public void add(int value) {
        if (amount == 0) {
            min = value;
            max = value;
        } else {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        amount++;
        avg = (avg * (amount - 1) + value) / amount;
    }

    @Override
    public int getMin() {
        if (amount == 0) {
            System.out.println("Вы еще не добавили числа");
        }
        return min;
    }

    @Override
    public int getMax() {
        if (amount == 0) {
            System.out.println("Вы еще не добавили числа");
        }
        return max;
    }

    @Override
    public int getCount() {
        if (amount == 0) {
            System.out.println("Вы еще не добавили числа");
        }
        return amount;
    }

    @Override
    public Double getAvg() {
        if (amount == 0) {
            System.out.println("Вы еще не добавили числа");
        }
        return avg;
    }
}

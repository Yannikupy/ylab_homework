package hw2;


public class RateLimitedPrinterImpl implements RateLimitedPrinter {
    private final long interval;
    private long lastCallTime;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCallTime > interval) {
            lastCallTime = currentTime;
            System.out.println(message);
        }
    }
}

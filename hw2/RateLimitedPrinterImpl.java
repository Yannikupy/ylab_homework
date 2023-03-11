package hw2;


public class RateLimitedPrinterImpl implements RateLimiterPrinter {
    private long interval;
    private long lastCallTime;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCallTime > interval) {
            System.out.println(message);
            lastCallTime = currentTime;
        }
    }
}

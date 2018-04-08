package paul.wintz.utils;

/**
 * @author PaulWintz The Timer class is designed to measure the amount of time
 *         passes. It cannot be paused. When it stops, the time
 */
public class Timer {
    long startTime;

    public Timer() {
        restartTimer();
    }

    public void restartTimer() {
        startTime = System.nanoTime();
    }

    private Long getElapsedTime() {
        return System.nanoTime() - startTime;
    }

    private int nanoToMilli(long nano) {
        return (int) (nano / 1_000_000);
    }

    private double getSeconds() {
        return getElapsedTime() / 1_000_000_000.0;
    }

    public String timeElapsedToString() {
        return String.valueOf(nanoToMilli(getElapsedTime()));
    }

    public void print(String description) {
        System.out.printf("%s: %.3f sec ", description, getSeconds());
    }

}

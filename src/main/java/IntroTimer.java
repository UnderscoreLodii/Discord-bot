public class IntroTimer {

    private long readyTime = 0;

    public static IntroTimer buildTheTimerAndStart(long timeInMillis) {
        IntroTimer timer = new IntroTimer();
        timer.startTimer(timeInMillis);
        return timer;
    }

    public boolean isReady() {
        return readyTime < System.currentTimeMillis();
    }

    public void startTimer(long timeInMillis) {
        readyTime = System.currentTimeMillis() + timeInMillis;
    }
}

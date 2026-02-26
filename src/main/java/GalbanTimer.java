public class GalbanTimer {

    private long readyTime = 0;

    public boolean isReady() {
        return readyTime < System.currentTimeMillis();
    }

    public void galbanJoined() {
        readyTime = System.currentTimeMillis() + 600000;
    }
}

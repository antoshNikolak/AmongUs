package Animation;

public class NewAnimationReturn {

    private AnimState animState;
    private String [] frames;
    private int speed;

    public NewAnimationReturn(AnimState direction, String[] frames, int speed) {
        this.animState = direction;
        this.frames = frames;
        this.speed = speed;
    }

    private NewAnimationReturn() {}

    public AnimState getAnimState() {
        return animState;
    }

    public String[] getFrames() {
        return frames;
    }

    public int getSpeed() {
        return speed;
    }
}

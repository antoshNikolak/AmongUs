package AnimationClient;


import Packet.Animation.NewAnimationReturn;

public class AnimationClient {
    //    private int index = 0;
    private final String[] frames;
    private String currentFrame;
    private final int indexesPerFrame;

    public AnimationClient(NewAnimationReturn newAnimationReturn) {
        this.frames = newAnimationReturn.getFrames();
        this.indexesPerFrame = newAnimationReturn.getSpeed();
        this.currentFrame = frames[0];
    }

    public void updateCurrentFrame(int index) {
        if (indexesPerFrame == 0) {
            this.currentFrame = frames[0];
        } else {
            this.currentFrame = frames[index / indexesPerFrame];
        }
    }


    public String getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(String currentFrame) {
        this.currentFrame = currentFrame;
    }

    public String[] getFrames() {
        return frames;
    }
}

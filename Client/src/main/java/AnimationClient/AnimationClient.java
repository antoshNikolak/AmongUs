package AnimationClient;


import Packet.Animation.NewAnimationReturn;

public class AnimationClient {
    private final String[] frames;//array of texture names
    private String currentFrame;//current texture being displayed
    private final int indexesPerFrame;//measure of how slow the animation plays

    public AnimationClient(NewAnimationReturn newAnimationReturn) {
        this.frames = newAnimationReturn.getFrames();
        this.indexesPerFrame = newAnimationReturn.getSpeed();
        this.currentFrame = frames[0];
    }

    public void updateCurrentFrame(int index) {
        if (indexesPerFrame == 0) {
            this.currentFrame = frames[0];
        } else {
            this.currentFrame = frames[index / indexesPerFrame];//set current frame corresponding to correct index per frame.
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

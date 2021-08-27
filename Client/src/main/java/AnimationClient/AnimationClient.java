package AnimationClient;


import Animation.NewAnimationReturn;

public class AnimationClient {
    private int index = 0;
    private final String[] frames;
    private String currentFrame;
    private int indexesPerFrame;

    public AnimationClient(NewAnimationReturn newAnimationReturn) {
        this.frames = newAnimationReturn.getFrames();
        this.indexesPerFrame = newAnimationReturn.getSpeed();
        if (frames.length == 0) {
            System.out.println(newAnimationReturn.getSpeed());
            this.currentFrame = "dead-green";
        } else {
            this.currentFrame = frames[0];//for now
        }
    }

    public void updateCurrentFrame(int index) {
        this.index = index;
        int currentFrameIndex = index / indexesPerFrame;
        this.currentFrame = frames[currentFrameIndex];
        System.out.println("index: "+ index);
//        System.out.println("current frame: " + currentFrame);

    }


    public String getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(String currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String[] getFrames() {
        return frames;
    }
}

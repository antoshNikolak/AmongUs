package Entity;

import Component.AnimationComp;
import Component.PosComp;

public class TaskBar extends Entity{

    private int progressBarWidth = 0;
    private final int widthWithoutFrame;

    public TaskBar() {
        this.widthWithoutFrame = 400 - 20;
        addComponent(new PosComp(10, 2, 400, 35));
        addComponent(getAnimationComp());

    }

    private AnimationComp getAnimationComp(){
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation("task-bar");
        return animationComp;
    }

    public boolean isFull(){
        return progressBarWidth >=widthWithoutFrame;
    }

    public int getProgressBarWidth() {
        return progressBarWidth;
    }

    public void incrementProgressBar(int width){
        this.progressBarWidth += width;
    }

    public void setProgressBarWidth(int progressBarWidth) {
        this.progressBarWidth = progressBarWidth;
    }
}

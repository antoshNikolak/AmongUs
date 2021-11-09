package Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import Animation.AnimState;
import Animation.NewAnimationReturn;

public class AnimationComp implements Component {
    private Animation currentAnimation;
    private AnimState currentAnimationState = AnimState.CONST;
    private final EnumMap<AnimState, Animation> directionAnimationMap = new EnumMap<>(AnimState.class);


    public void addAnimation(AnimState animState, String [] frames, int framesPerIndex) {
        directionAnimationMap.put(animState, new Animation(frames, framesPerIndex));
    }

    public void addAnimation(AnimState animState, String texture){
        directionAnimationMap.put(animState, new Animation(new String[]{texture}, 0));
    }

    public void addAnimation(String texture){
        directionAnimationMap.put(AnimState.CONST, new Animation(new String[]{texture}, 0));
    }

    public List<NewAnimationReturn> adaptToAllNewAnimations(){
        List <NewAnimationReturn> animationReturns = new ArrayList<>();
        directionAnimationMap.forEach((animState, animation) -> {
            animationReturns.add(new NewAnimationReturn(animState, animation.frames, animation.indexesPerFrame));
        });
        return animationReturns;
    }

    public void setCurrentAnimation(AnimState animState){
        this.currentAnimationState = animState;
        this.currentAnimation = directionAnimationMap.get(animState);
        this.currentAnimation.setIndex(0);
    }

    public AnimState getCurrentAnimationState() {
        return currentAnimationState;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public Set<AnimState> getAnimStates(){
        return directionAnimationMap.keySet();
//        return new ArrayList<>(directionAnimationMap.keySet());
    }
    public Animation getAnimation(AnimState animState){
        return directionAnimationMap.get(animState);
    }

//    public boolean isAnimationRunnable(AnimationCondition animationCondition) {
//        return animationCondition.returnLogic();
//    }
//
//    public boolean isAnimationResetable(AnimationCondition animationCondition) {
//        return animationCondition.returnLogic();
//    }


    public static class Animation {
        private final String[] frames;
        private final int indexesPerFrame;
        private int index = 0;


        public Animation(String [] frames, int framesPerIndex) {
            this.frames = frames;
            this.indexesPerFrame = framesPerIndex;
        }

        public void runAnimation() {
            index++;
            if (index > (indexesPerFrame * frames.length)-1) {
                index = 0;
            }
        }

        public String getCurrentFrame(){
            return frames[index];
        }

        public String[] getFrames() {
            return frames;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndexesPerFrame() {
            return indexesPerFrame;
        }
    }


}


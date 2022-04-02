package Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import Packet.Animation.AnimState;
import Packet.Animation.NewAnimationReturn;

public class AnimationComp implements Component {
    private AnimState currentAnimationState = AnimState.CONST;//stores animation type being displayed
    private final EnumMap<AnimState, Animation> directionAnimationMap = new EnumMap<>(AnimState.class);//maps each type of animation to the animation itself

    //overload methods for adding animation
    public void addAnimation(AnimState animState, String [] frames, int framesPerIndex) {
        directionAnimationMap.put(animState, new Animation(frames, framesPerIndex));
    }

    //overload methods for adding animation
    public void addAnimation(AnimState animState, String texture){
        directionAnimationMap.put(animState, new Animation(new String[]{texture}, 0));
    }

    //overload methods for adding animation
    public void addAnimation(String texture){
        directionAnimationMap.put(AnimState.CONST, new Animation(new String[]{texture}, 0));
    }

    public List<NewAnimationReturn> adaptToAllNewAnimations(){
        //convert each animation to a new animation return object which will be sent to clients
        List <NewAnimationReturn> animationReturns = new ArrayList<>();
        directionAnimationMap.forEach((animState, animation) -> {
            animationReturns.add(new NewAnimationReturn(animState, animation.frames, animation.indexesPerFrame));
        });
        return animationReturns;
    }

    public void setCurrentAnimation(AnimState animState){
        this.currentAnimationState = animState;
        directionAnimationMap.get(animState).setIndex(0);
    }

    public AnimState getCurrentAnimationState() {
        return currentAnimationState;
    }

    public Animation getCurrentAnimation() {
        return directionAnimationMap.get(currentAnimationState);
    }

    public Set<AnimState> getAnimStates(){
        return directionAnimationMap.keySet();
    }
    public Animation getAnimation(AnimState animState){
        return directionAnimationMap.get(animState);
    }

    public static class Animation {
        private final String[] frames;//array of texture
        private final int indexesPerFrame;//slowness of animation
        private int index = 0;//current index


        public Animation(String [] frames, int framesPerIndex) {
            this.frames = frames;
            this.indexesPerFrame = framesPerIndex;
        }

        public void runAnimation() {
            index++;//increment index
            if (index >= (indexesPerFrame * frames.length)) {//check all frames have been cycled
                index = 0;//resent index to show initial frame
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


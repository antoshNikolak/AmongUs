package AnimationClient;

import Packet.Animation.AnimState;
import Packet.Animation.NewAnimationReturn;
import Packet.EntityState.NewAnimatedEntityState;

import java.util.EnumMap;
import java.util.List;

public class AnimationManager {

    private final EnumMap<AnimState, AnimationClient> directionAnimationMap = new EnumMap<>(AnimState.class);
    //dictionary maps animation type to a animation client object which stores a list of textures.
    private AnimState currentState;//stores the current animation type being displayed

    public AnimationManager(NewAnimatedEntityState newAnimationEntityState) {
        loadAnimationMap(newAnimationEntityState.getNewAnimationReturns());
        this.currentState = newAnimationEntityState.getCurrentState();
    }

    private void loadAnimationMap(List<NewAnimationReturn> newAnimationReturns){
        //load each type of animation into the map
        for (NewAnimationReturn anim: newAnimationReturns){
            directionAnimationMap.put(anim.getAnimState(), new AnimationClient(anim));
        }
    }

    public AnimationClient getCurrentAnimation(){
        return directionAnimationMap.get(currentState);
    }

    public String getCurrentFrame(){
        return getCurrentAnimation().getCurrentFrame();
    }

    public void setCurrentFrame(String textureName){
        getCurrentAnimation().setCurrentFrame(textureName);
    }

    public void updateCurrentAnimationIndex(int index){
        getCurrentAnimation().updateCurrentFrame(index);
    }

    public AnimState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(AnimState currentState) {
        this.currentState = currentState;
    }
}

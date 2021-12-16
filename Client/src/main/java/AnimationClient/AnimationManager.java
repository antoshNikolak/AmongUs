package AnimationClient;

import Packet.Animation.AnimState;
import Packet.Animation.NewAnimationReturn;
import Packet.EntityState.NewAnimatedEntityState;

import java.util.EnumMap;
import java.util.List;

public class AnimationManager {

    private final EnumMap<AnimState, AnimationClient> directionAnimationMap = new EnumMap<>(AnimState.class);
    private AnimState currentState;

    public AnimationManager(NewAnimatedEntityState newAnimationEntityState) {
        loadAnimationMap(newAnimationEntityState.getNewAnimationReturns());
        determineCurrentState(newAnimationEntityState);
    }

    private void loadAnimationMap(List<NewAnimationReturn> newAnimationReturns){
        for (NewAnimationReturn anim: newAnimationReturns){
            directionAnimationMap.put(anim.getAnimState(), new AnimationClient(anim));
        }
    }

    private void determineCurrentState(NewAnimatedEntityState newEntityState){
        this.currentState = newEntityState.getCurrentState();
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

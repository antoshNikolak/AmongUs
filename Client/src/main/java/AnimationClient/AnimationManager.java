package AnimationClient;

import Animation.AnimState;
import Animation.NewAnimationReturn;
import Packet.Position.NewEntityState;

import java.util.EnumMap;
import java.util.List;

public class AnimationManager {

    private final EnumMap<AnimState, AnimationClient> directionAnimationMap = new EnumMap<>(AnimState.class);
    private AnimState currentState;

    public AnimationManager(NewEntityState newEntityState) {
        loadAnimationMap(newEntityState.getNewAnimationReturn());
        determineCurrentState(newEntityState);
    }

    private void loadAnimationMap(List<NewAnimationReturn> newAnimationReturns){
        for (NewAnimationReturn anim: newAnimationReturns){
            directionAnimationMap.put(anim.getAnimState(), new AnimationClient(anim));
        }
    }

    private void determineCurrentState(NewEntityState newEntityState){
        this.currentState = newEntityState.getAnimState();
        System.out.println(currentState);
//        if (directionAnimationMap.containsKey(AnimState.CONST)) {
//            this.currentState = AnimState.CONST;
//        }else {
//            this.currentState = AnimState.RIGHT;
//        }
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

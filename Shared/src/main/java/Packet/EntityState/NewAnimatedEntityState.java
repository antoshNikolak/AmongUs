package Packet.EntityState;

import Animation.AnimState;
import Animation.NewAnimationReturn;
import Position.Pos;

import java.util.List;

public class NewAnimatedEntityState extends NewEntityState{
    private Pos pos;
    private List<NewAnimationReturn> newAnimationReturns;
    private AnimState currentState;


    public NewAnimatedEntityState(int registrationID, Pos pos, List<NewAnimationReturn> newAnimationReturns, AnimState currentState) {
        super(registrationID);
        this.pos = pos;
        this.newAnimationReturns = newAnimationReturns;
        this.currentState = currentState;
    }

    public Pos getPos() {
        return pos;
    }

    public List<NewAnimationReturn> getNewAnimationReturns() {
        return newAnimationReturns;
    }

    public NewAnimatedEntityState(){
        super();
    }

    public AnimState getCurrentState() {
        return currentState;
    }
}

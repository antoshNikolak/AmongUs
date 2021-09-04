package Packet.Position;

import Animation.AnimState;
import Animation.NewAnimationReturn;
import Position.Pos;

import java.util.ArrayList;
import java.util.List;

public class NewEntityState extends EntityState{
    private List<NewAnimationReturn> newAnimationReturn;


    public NewEntityState(int registrationID, Pos pos, List<NewAnimationReturn> newAnimationReturn, AnimState currentState) {
        super(registrationID, pos, currentState, 0);
        this.newAnimationReturn = newAnimationReturn;
//        this.currentState = currentState;
    }


    private NewEntityState() {
        super();
    }

    public List<NewAnimationReturn> getNewAnimationReturn() {
        return newAnimationReturn;
    }

}

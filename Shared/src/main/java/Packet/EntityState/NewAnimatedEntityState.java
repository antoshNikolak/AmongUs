package Packet.EntityState;

import Packet.Animation.AnimState;
import Packet.Animation.NewAnimationReturn;
import Position.Pos;

import java.util.List;

public class NewAnimatedEntityState extends NewEntityState{
    private Pos pos;
    private List<NewAnimationReturn> newAnimationReturns;
    private AnimState currentState;
    private boolean scrollable = true;
    private String nameTag;


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

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }
}

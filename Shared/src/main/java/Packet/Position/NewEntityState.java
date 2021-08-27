package Packet.Position;

import Animation.NewAnimationReturn;
import Position.Pos;

import java.util.ArrayList;
import java.util.List;

public class NewEntityState extends EntityState{
    private List<NewAnimationReturn> newAnimationReturn;

    public NewEntityState(int registrationID, Pos pos, List<NewAnimationReturn> newAnimationReturn) {
        super(registrationID, pos);
        this.newAnimationReturn = newAnimationReturn;
    }


    private NewEntityState() {
        super();
    }

    public List<NewAnimationReturn> getNewAnimationReturn() {
        return newAnimationReturn;
    }
}

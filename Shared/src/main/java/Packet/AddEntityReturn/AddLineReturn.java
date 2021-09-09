package Packet.AddEntityReturn;

import Animation.AnimState;
import Animation.NewAnimationReturn;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Position.Pos;

import java.util.List;

public class AddLineReturn extends AddEntityReturn{

    public AddLineReturn(List<NewLineState> newEntityStates) {
        super(newEntityStates);
    }

    public AddLineReturn(NewLineState... entityStates) {
        super(entityStates);
    }

    public AddLineReturn() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<NewLineState> getNewEntityStates() {
        return (List<NewLineState>)super.getNewEntityStates();
    }
}

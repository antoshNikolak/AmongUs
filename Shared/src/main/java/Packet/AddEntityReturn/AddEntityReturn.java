package Packet.AddEntityReturn;

import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AddEntityReturn implements Packet {

    protected List<? extends NewEntityState> newEntityStates;

    public AddEntityReturn(List<? extends NewEntityState> newEntityStates) {
        this.newEntityStates = newEntityStates;
    }

    public AddEntityReturn(NewEntityState... entityStates) {
        this.newEntityStates = new ArrayList<>(Arrays.asList(entityStates));
//        this.newEntityStates.addAll(Arrays.asList(entityStates));
    }//todo can I add to a list when it has ? extends T

    public AddEntityReturn() { }

    @SuppressWarnings("unchecked")
    public List<? extends NewEntityState> getNewEntityStates() {
        return  newEntityStates;
    }
}

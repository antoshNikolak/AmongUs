package Packet.AddEntityReturn;

import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEntityReturn implements Packet {

    protected List<? extends NewEntityState> newEntityStates;

    public AddEntityReturn(List<? extends NewEntityState> newEntityStates) {
        this.newEntityStates = newEntityStates;
    }

    public AddEntityReturn(NewEntityState... entityStates) {
        this.newEntityStates = new ArrayList<>(Arrays.asList(entityStates));
    }

    private AddEntityReturn() { }

    @SuppressWarnings("unchecked")
    public List<? extends NewEntityState> getNewEntityStates() {
        return  newEntityStates;
    }
}

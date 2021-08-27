package Packet.Position;

import Packet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AddEntityReturn implements Packet {

    protected List<NewEntityState> newEntityStates;

    public AddEntityReturn(List<NewEntityState> newEntityStates) {
        this.newEntityStates = newEntityStates;
    }

    public AddEntityReturn(NewEntityState... entityStates) {
        this.newEntityStates = new ArrayList<>();
        this.newEntityStates.addAll(Arrays.asList(entityStates));
    }

    private AddEntityReturn() { }

    public List<NewEntityState> getNewEntityStates() {
        return newEntityStates;
    }
}

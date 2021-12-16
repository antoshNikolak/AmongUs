package Packet.Position;

import Packet.EntityState.ExistingEntityState;
import Packet.Packet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateReturn implements Packet {
    private Set<ExistingEntityState> existingEntityStates;
    private List<String> userTags= new ArrayList<>();

    public StateReturn(Set<ExistingEntityState> existingEntityStates) {
        this.existingEntityStates = existingEntityStates;
    }

    public StateReturn(ExistingEntityState existingEntityState){
        this.existingEntityStates = new HashSet<>();
        this.existingEntityStates.add(existingEntityState);
    }

    public Set<ExistingEntityState> getEntityStates() {
        return existingEntityStates;
    }

    private StateReturn() {
    }
}

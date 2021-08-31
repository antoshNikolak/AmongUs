package Packet.Position;

import Packet.Packet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateReturn implements Packet {
    private Set<EntityState> entityStates;

    public StateReturn(Set<EntityState> entityStates) {
        this.entityStates = entityStates;
    }

    public StateReturn(EntityState entityState){
        this.entityStates = new HashSet<>();
        this.entityStates.add(entityState);
    }

    public Set<EntityState> getEntityStates() {
        return entityStates;
    }

    private StateReturn() {
    }
}

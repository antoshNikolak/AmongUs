package Packet.Position;

import Packet.Packet;

import java.util.List;

public class AddStationaryEntityReturn extends AddEntityReturn {

    private AddStationaryEntityReturn() {}

    public AddStationaryEntityReturn(List<NewEntityState> newEntityStates) {
        super(newEntityStates);
    }

    public AddStationaryEntityReturn(NewEntityState... entityStates) {
        super(entityStates);
    }
}

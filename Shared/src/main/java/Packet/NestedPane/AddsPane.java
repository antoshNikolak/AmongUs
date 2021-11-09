package Packet.NestedPane;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;

import java.util.List;

public interface AddsPane {

    int getPaneWidth();
    int getPaneHeight();
    int getPaneX();
    int getPaneY();
    List<NewAnimatedEntityState> getNewEntityStates();
    List<NodeInfo> getNodes();
}

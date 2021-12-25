package Packet.NestedPane;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import javafx.scene.paint.Color;

import java.util.List;

public interface AddsPane {

    int getPaneWidth();
    int getPaneHeight();
    int getPaneX();
    int getPaneY();
    Color getColour();
    List<NewAnimatedEntityState> getNewEntityStates();
    List<NodeInfo> getNodes();
}

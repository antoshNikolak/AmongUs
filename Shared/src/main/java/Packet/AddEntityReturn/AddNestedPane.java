package Packet.AddEntityReturn;

import Packet.EntityState.EntityState;
import Packet.EntityState.NewEntityState;
import Packet.Packet;
import Position.Pos;

import java.util.ArrayList;
import java.util.List;

public class AddNestedPane extends AddEntityReturn {
    private Pos panePos;
    private double paneWidth, paneHeight;

    public AddNestedPane() {
    }

    public AddNestedPane(List<? extends NewEntityState> newEntityStates, Pos panePos, double paneWidth, double paneHeight) {
        super(newEntityStates);
        this.panePos = panePos;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
    }

    public Pos getPanePos() {
        return panePos;
    }

    public double getPaneWidth() {
        return paneWidth;
    }

    public double getPaneHeight() {
        return paneHeight;
    }

    //    public AddNestedPane(NewEntityState... entityStates) {
//        super(entityStates);
//    }


}

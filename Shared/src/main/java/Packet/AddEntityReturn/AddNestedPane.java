package Packet.AddEntityReturn;

import Packet.EntityState.EntityState;
import Packet.EntityState.NewEntityState;
import Packet.Packet;
import Position.Pos;

import java.util.ArrayList;
import java.util.List;

public class AddNestedPane extends AddEntityReturn {

    private int paneWidth, paneHeight ,paneX, paneY;

    public AddNestedPane() {
    }

    public AddNestedPane(List<? extends NewEntityState> newEntityStates, int paneX, int paneY, int paneWidth, int paneHeight) {
        super(newEntityStates);
        this.paneX = paneX;
        this.paneY = paneY;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
    }

    public int getPaneWidth() {
        return paneWidth;
    }

    public int getPaneHeight() {
        return paneHeight;
    }

    public int getPaneX() {
        return paneX;
    }

    public int getPaneY() {
        return paneY;
    }

    //    public AddNestedPane(NewEntityState... entityStates) {
//        super(entityStates);
//    }


}

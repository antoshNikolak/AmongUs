package Packet.EntityState;

import Animation.AnimState;
import Animation.NewAnimationReturn;
import Position.Pos;

import java.util.List;

public class NewLineState extends NewEntityState {

    private Pos startPos, finalPos;
    private int width;

//    public NewLineState(int registrationID,  Pos startPos, Pos finalPos, int width) {
//        super(registrationID);
//        this.startPos = startPos;
//        this.finalPos = finalPos;
//        this.width = width;
//    }

    public NewLineState( Pos startPos, Pos finalPos, int width) {
        super(-1);
        this.startPos = startPos;
        this.finalPos = finalPos;
        this.width = width;
    }

    public NewLineState() {
    }

    public Pos getStartPos() {
        return startPos;
    }

    public Pos getFinalPos() {
        return finalPos;
    }

    public int getWidth() {
        return width;
    }
}

package Packet.Position;

import Animation.AnimState;
import Animation.NewAnimationReturn;
import Packet.Packet;
import Position.Pos;

public  class EntityState implements Packet {
    private int registrationID;
    private Pos pos;

    private AnimState animState;
    private Integer animationIndex;

    public EntityState(int registrationID, Pos pos, AnimState animState, Integer animationIndex) {
        this.registrationID = registrationID;
        this.pos = pos;
        this.animState = animState;
        this.animationIndex = animationIndex;
    }

    public EntityState(int registrationID, Pos pos) {
        this.registrationID = registrationID;
        this.pos = pos;
    }


    public EntityState() {}

    public int getRegistrationID() {
        return registrationID;
    }

    public Pos getPos() {
        return pos;
    }

    public Integer getAnimationIndex() {
        return animationIndex;
    }

    public AnimState getAnimState() {
        return animState;
    }
}

package Packet.EntityState;

import Packet.Animation.AnimState;
import Position.Pos;

public class ExistingEntityState extends EntityState {
    //    private int registrationID;
    private Pos pos;

    //create add new line

    private AnimState animState;
    private Integer animationIndex;
    private String nameTag;

    public ExistingEntityState(int registrationID, Pos pos, AnimState animState, Integer animationIndex) {
        super(registrationID);
        this.pos = pos;
        this.animState = animState;
        this.animationIndex = animationIndex;
    }

    public ExistingEntityState(int registrationID, Pos pos) {
        super(registrationID);
        this.pos = pos;
    }


    private ExistingEntityState() {
        super();
    }

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

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }
}

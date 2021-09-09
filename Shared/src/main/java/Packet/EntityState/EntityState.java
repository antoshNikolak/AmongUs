package Packet.EntityState;

public class EntityState {
    protected int registrationID;

    public EntityState(int registrationID) {
        this.registrationID = registrationID;
    }

    public EntityState() {}

    public int getRegistrationID() {
        return registrationID;
    }
}

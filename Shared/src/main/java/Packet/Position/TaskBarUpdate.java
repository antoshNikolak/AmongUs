package Packet.Position;

import Packet.EntityState.EntityState;
import Packet.Packet;

public class TaskBarUpdate extends EntityState implements Packet {
    private int newWidth;

    public TaskBarUpdate(int registrationID, int newWidth) {
        super(registrationID);
        this.newWidth = newWidth;
    }



    private TaskBarUpdate() {}

    public int getNewWidth() {
        return newWidth;
    }

    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
    }


}

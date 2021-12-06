package Packet.GameStart;

import Packet.Packet;

public class RoleNotify implements Packet {
    private boolean isImpostor;

    public RoleNotify(boolean isImpostor) {
        this.isImpostor = isImpostor;
    }

    private RoleNotify() {}

    public boolean isImpostor() {
        return isImpostor;
    }

    public void setImpostor(boolean impostor) {
        isImpostor = impostor;
    }
}

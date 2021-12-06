package Packet.GameEnd;

import Packet.Packet;

public class GameEnd implements Packet {
    private String message;

    public GameEnd(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package Packet.CountDown;

import Packet.Packet;

public class RemoveCountDown implements Packet {
    public int timerID;

    public RemoveCountDown(int timerID) {
        this.timerID = timerID;
    }

    private RemoveCountDown() {}
}

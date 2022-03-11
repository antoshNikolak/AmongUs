package Packet.CountDown;

import Packet.Packet;

public class RemoveCountDown implements Packet {
    public int id;

    public RemoveCountDown(int id) {
        this.id = id;
    }

    //    public int timerID;
//    public CountDown countDown;
//
//    public RemoveCountDown(CountDown countDown) {
//        this.countDown = countDown;
//    }

    private RemoveCountDown() {}
}

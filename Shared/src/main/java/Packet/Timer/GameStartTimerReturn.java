package Packet.Timer;

import Packet.Packet;

public class GameStartTimerReturn implements Packet {
    private int countDownValue;

    public GameStartTimerReturn(int countDownValue) {
        this.countDownValue = countDownValue;
    }

    public int getCountDownValue() {
        return countDownValue;
    }

    private GameStartTimerReturn() {
    }
}

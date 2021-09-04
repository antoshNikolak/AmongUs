package Packet.Timer;

import Packet.Packet;

public abstract class Timer implements Packet {
    private int countDownValue;

    public Timer(int countDownValue) {
        this.countDownValue = countDownValue;
    }

    public int getCountDownValue() {
        return countDownValue;
    }

    public Timer() {
    }
}

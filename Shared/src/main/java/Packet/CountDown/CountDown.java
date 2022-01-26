package Packet.CountDown;

import Packet.Packet;

public class CountDown implements Packet {
    public int countDownValue;
    public int x,y, size, id;
//    public String name;


    public CountDown(int id, int countDownValue, int x, int y, int size) {
        this.id = id;
        this.countDownValue = countDownValue;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getCountDownValue() {
        return countDownValue;
    }

    private CountDown() {
    }
}

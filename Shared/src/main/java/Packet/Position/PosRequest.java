package Packet.Position;

import Packet.Packet;

public class PosRequest implements Packet {
    private boolean left= false;
    private boolean right= false;
    private boolean up= false;
    private boolean down= false;

    public PosRequest() {}

    public PosRequest(boolean left, boolean down, boolean up, boolean right) {
        this.left = left;
        this.down = down;
        this.up = up;
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}

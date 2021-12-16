package Packet.ScreenData;

import Packet.Packet;

public class ScreenInfo implements Packet {
    public int width, height;

    public ScreenInfo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ScreenInfo() {}

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

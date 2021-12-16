package Packet.Animation;

import Packet.Packet;
import Position.Pos;

public class AnimationDisplayReturn implements Packet {

    private String texture;
    private Pos pos;
    private int width, height;
    private int duration;


    public AnimationDisplayReturn(String texture, Pos pos, int width, int height, int duration) {
        this.texture = texture;
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    private AnimationDisplayReturn(){

    }

    public String getTexture() {
        return texture;
    }

    public Pos getPos() {
        return pos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDuration() {
        return duration;
    }
}

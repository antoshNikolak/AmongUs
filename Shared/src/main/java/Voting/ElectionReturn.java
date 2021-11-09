package Voting;

import Packet.Packet;

public class ElectionReturn implements Packet {
    private String texture;

    public ElectionReturn(String texture) {
        this.texture = texture;
    }

    private ElectionReturn() {
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}

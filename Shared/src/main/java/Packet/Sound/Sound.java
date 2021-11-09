package Packet.Sound;

import Packet.Packet;

public class Sound implements Packet {

    private byte [] bytes;

    public Sound(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Sound() {
    }

}

package Packet.Voting;

import Packet.Packet;

public class ChatMessageReturn implements Packet {
    public String message, username, texture;

    public ChatMessageReturn(String message, String username, String texture) {
        this.message = message;
        this.username = username;
        this.texture = texture;
    }

    public ChatMessageReturn() {}

}

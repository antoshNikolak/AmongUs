package Packet.Voting;

import Packet.Packet;

public class ChatMessage implements Packet {
    public String message;

    public ChatMessage(String message) {
        this.message = message;
    }

    private ChatMessage() {}


}

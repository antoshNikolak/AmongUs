package Packet.Voting;

import Packet.Packet;

public class ChatMessageRequest implements Packet {
    public String message;

    public ChatMessageRequest(String message) {
        this.message = message;
    }

    public ChatMessageRequest() {}


}

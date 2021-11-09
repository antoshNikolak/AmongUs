package Packet.NestedPane;

import Packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class RemoveVotingScreen implements Packet {

    private Map<String, String> playerVoteInfo = new HashMap<>();

    public RemoveVotingScreen(Map<String, String> playerVoteInfo) {
        this.playerVoteInfo = playerVoteInfo;
    }

    public RemoveVotingScreen() {
    }

    public Map<String, String> getPlayerVoteInfo() {
        return playerVoteInfo;
    }
}



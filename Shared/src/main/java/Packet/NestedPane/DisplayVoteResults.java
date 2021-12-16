package Packet.NestedPane;

import Packet.Packet;
import Packet.Voting.VoteOption;

import java.util.HashMap;
import java.util.Map;

public class DisplayVoteResults implements Packet {

    private Map<String, VoteOption> playerVoteInfo = new HashMap<>();

    public DisplayVoteResults(Map<String, VoteOption> playerVoteInfo) {
        this.playerVoteInfo = playerVoteInfo;
    }

    public DisplayVoteResults() {}

    public Map<String, VoteOption> getPlayerVoteInfo() {
        return playerVoteInfo;
    }
}



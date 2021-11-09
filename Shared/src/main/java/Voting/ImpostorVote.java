package Voting;

import Packet.Packet;

public class ImpostorVote implements Packet {
    private String voteTexture;

    private ImpostorVote() {
    }

    public ImpostorVote(String voteTexture) {
        this.voteTexture = voteTexture;
    }

    public String getCandidateTexture() {
        return voteTexture;
    }

    public void setVoteTexture(String voteTexture) {
        this.voteTexture = voteTexture;
    }
}

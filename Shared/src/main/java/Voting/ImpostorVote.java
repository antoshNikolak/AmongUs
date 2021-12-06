package Voting;

import Packet.Packet;

public class ImpostorVote implements Packet {

    private VoteOption voteOption;

    public ImpostorVote() {}

    public ImpostorVote(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }



    //    public ImpostorVote(String voteTexture) {
//        this.voteTexture = voteTexture;
//    }
//
//    public String getCandidateTexture() {
//        return voteTexture;
//    }
//
//    public void setVoteTexture(String voteTexture) {
//        this.voteTexture = voteTexture;
//    }
}

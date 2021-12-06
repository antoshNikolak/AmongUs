package VoteHandler;

import Animation.AnimState;
import Component.AliveComp;
import Component.AnimationComp;
import Component.ColourComp;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Packet.NestedPane.AddVotingPane;
import Packet.NestedPane.DisplayVoteResults;
import System.EmergencyTableSystem;
import TimerHandler.TimerStarter;
import Utils.CollectionUtils;
import Voting.VoteOption;

import java.util.*;
import java.util.stream.Collectors;

import static StartUpServer.AppServer.currentGame;

public class VoteHandler {

    private final Map<String, VoteOption> playerVoteMap = new HashMap<>();//todo rename voter suspect map
    private final EmergencyTableSystem emergencyTableSystem;


    public VoteHandler(EmergencyTableSystem emergencyTableSystem) {
        this.emergencyTableSystem = emergencyTableSystem;
    }

    private Optional<VoteOption> getVoteOptionWithMostVotes() {
        Map<VoteOption, Integer> playerVotes = getSuspectVoteCountMap();//maps vote option to num of votes
        if (playerVotes.size() == 0) return Optional.empty();
        int highestVotes = Collections.max(playerVotes.values());
        VoteOption winner = CollectionUtils.getKey(playerVotes, highestVotes);
        if (isDraw(playerVotes, winner, highestVotes)){
            return Optional.empty();//return empty if draw
        }
        return Optional.of(Objects.requireNonNull(winner));//return most popular candidate
    }

    private boolean isDraw(Map<VoteOption, Integer> playerVotes, VoteOption winner, int highestVotes){
        for (Map.Entry<VoteOption, Integer> suspectEntry : playerVotes.entrySet()){
            VoteOption voteOption = suspectEntry.getKey();
            Integer voteCount = suspectEntry.getValue();
            if (voteCount == highestVotes && voteOption!= winner){
                return true;
            }
        }
        return false;
    }

//    private VoteOption getVoteOptionWithMostVotes(){
//        playerVoteMap
//    }

    private Optional<Player> getPlayerFromVoteOption(VoteOption voteOption) {
        for (Player player : currentGame.getPlayers()) {
            String colour = player.getComponent(ColourComp.class).getColour();
            if (voteOption.name().toLowerCase().contains(colour)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    private Map<VoteOption, Integer> getSuspectVoteCountMap() {
        Map<VoteOption, Integer> suspectVoteCount = new HashMap<>();
        for (VoteOption suspect : playerVoteMap.values()) {
            if (!suspectVoteCount.containsKey(suspect)) {
                suspectVoteCount.put(suspect, 1);
            } else {//entry already exists
                suspectVoteCount.put(suspect, suspectVoteCount.get(suspect)+1);
            }
        }
        return suspectVoteCount;
    }

    private VoteOption getKeyWithLargestEntryValues(Map<Integer, VoteOption> playerVotes) {
        return playerVotes.get(Collections.max(playerVotes.keySet()));
//        VoteOption mostVotedPlayer = null;
//        int votesForPlayer = 0;
//        for (Map.Entry<VoteOption, Integer> playerVote : playerVotes.entrySet()) {
////            if (playerVote.getValue() > votesForPlayer && playerVote.getKey().getComponent(AliveComp.class).isAlive()) {
//            if (playerVote.getValue() > votesForPlayer) {
//                votesForPlayer = playerVote.getValue();
//                mostVotedPlayer = playerVote.getKey();
//            }
//        }
//        return mostVotedPlayer;
    }


    public void startVotingTimer() {
        TimerStarter.startTimer("VotingTimer", 5, () -> {//time for players to vote
            ConnectionServer.sendTCPToAllPlayers(new DisplayVoteResults(playerVoteMap));
            //client will remove voting pane once vote results displayed
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    emergencyTableSystem.onVoteTimerOver();//close and remove the emergency meeting system
                }
            }, 2000);//time to display
        });
    }

    //return texture to texture map
    //voter to suspect map

//    private Map<String, VoteOption> getVoterSuspectMap() {
//        Map<String, VoteOption> voterSuspectMap = new HashMap<>();
//        for (Map.Entry<String, VoteOption> voteEntry : playerVoteMap.entrySet()) {
////            String voter = getPlayerAnimationID(voteEntry.getKey());
//            String voter = voteEntry.getKey();
//            VoteOption suspect = voteEntry.getValue();
//            voterSuspectMap.put(voter, suspect);
//        }
//        return voterSuspectMap;
//    }


    public AddVotingPane createVotingPane() {
        return new AddVotingPane(getAllPlayerAnimationID(), 20, 20, 300, 300);
    }

    private String getPlayerAnimationID(Player player) {
        AnimationComp animationComp = player.getComponent(AnimationComp.class);
        if (player.getComponent(AliveComp.class).isAlive()) {
            return animationComp.getAnimation(AnimState.RIGHT).getFrames()[0];
        } else {
            return animationComp.getAnimation(AnimState.GHOST_RIGHT).getFrames()[0];
        }
    }


    private List<String> getAllPlayerAnimationID() {
        return currentGame.getPlayers().stream().
                map(this::getPlayerAnimationID).
                collect(Collectors.toList());
    }

    private String getPlayerAnimationID(String colour) {
        return getPlayerAnimationID(getPlayerFromColour(colour));
    }

    private Player getPlayerFromColour(String colour) {
        for (Player player : currentGame.getPlayers()) {
            if (player.getComponent(ColourComp.class).getColour().equals(colour)) {
                return player;
            }
        }
        throw new IllegalArgumentException("invalid colour input");
    }

    public void registerVote(Player voter, VoteOption suspect) {
        String animID = getPlayerAnimationID(voter);
        if (!animID.contains("ghosts")) {
            playerVoteMap.put(animID, suspect);
        }
    }

    public Optional<Player> getPlayerWithMostVotes() {
        Optional <VoteOption> voteOption = getVoteOptionWithMostVotes();
        return voteOption.isEmpty() ? Optional.empty() : getPlayerFromVoteOption(voteOption.get());
    }
}

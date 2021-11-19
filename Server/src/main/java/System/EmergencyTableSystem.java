package System;

import Animation.AnimState;
import AnimationFactory.AnimationFactory;
import Component.AliveComp;
import Component.AnimationComp;
import Component.PosComp;
import Component.RespawnComp;
import ConnectionServer.ConnectionServer;
import Entity.DeadPlayer;
import Entity.Player;
//import Packet.NestedPane.AddVotingPane;
import Packet.NestedPane.AddVotingPane;
import Packet.NestedPane.RemoveVotingScreen;
import Packet.Sound.CloseRecordHandler;
import Packet.Sound.OpenRecordHandler;
import Position.Pos;
import StartUpServer.AppServer;
import State.State;
import TimerHandler.TimerStarter;

import java.util.*;

public class EmergencyTableSystem extends BaseSystem {

    private DeadPlayer deadPlayer;
    private Map<Player, Player> playerVoteMap = new HashMap<>();


    @Override
    public void update() {


    }

    public void activate() {
        teleportPlayersToEmergencyTable();
        immobilisePlayers();
        broadcastDeadBodyReportedAnimation();
        broadcastVotingPanel();
        enableVoiceChat();
        startTimeToVoteTimer();
    }

    private void startTimeToVoteTimer(){
        TimerStarter.startTimer("VotingTimer", 5, ()->{
            ConnectionServer.sendTCPToAllPlayers(new RemoveVotingScreen(getPlayerAnimationMap(playerVoteMap)));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    onAnimationOver();
                }
            }, 2000);//however long the animation takes
        });
    }

    public void onAnimationOver(){
        Optional<Player> playerOptional = getAlivePlayerWithMostVotes();
        playerOptional.ifPresent(this::ejectPlayer);
        mobilisePlayers();
        disableVoiceChat();
        removeThisFromSystems();
    }

    public void removeThisFromSystems(){
        AppServer.currentGame.getStateManager().getCurrentState().removeSystem(getClass());
    }

    private Map<String, String> getPlayerAnimationMap(Map<Player, Player> playerMap){
        Map<String, String> playerAnimationMap = new HashMap<>();
        for (Map.Entry<Player, Player> playerEntry : playerMap.entrySet()){
            playerAnimationMap.put(getPlayerAnimationID(playerEntry.getKey()), getPlayerAnimationID(playerEntry.getValue()));
        }
        return playerAnimationMap;
    }

    private void ejectPlayer(Player player){
        ImposterActionsSystem killHandler = AppServer.currentGame.getStateManager().getCurrentState().getSystem(ImposterActionsSystem.class);
        killHandler.setGhostAttributes(player);
        killHandler.updateGhostForClients(player);
    }

    private Optional<Player> getAlivePlayerWithMostVotes(){//will return null iff no one voted. Votes for dead players dont count
        Map<Player, Integer> playerVotes = getPlayerVoteMap();
        return Optional.ofNullable(getKeyWithLargestEntryValues(playerVotes)); //find most voted player based on map
    }

    private Player getKeyWithLargestEntryValues(Map<Player, Integer> playerVotes){
        Player mostVotedPlayer = null;
        int votesForPlayer = 0;
        for (Map.Entry<Player, Integer> playerVote : playerVotes.entrySet()){
            if (playerVote.getValue() > votesForPlayer && playerVote.getKey().getComponent(AliveComp.class).isAlive()){
                votesForPlayer = playerVote.getValue();
                mostVotedPlayer = playerVote.getKey();
            }
        }
        return mostVotedPlayer;
    }

    private Map<Player, Integer> getPlayerVoteMap(){
        Map<Player, Integer> playerVotes = new HashMap<>();
        for (Player player: playerVoteMap.keySet()){
            if (!playerVotes.containsKey(player)){
                playerVotes.put(player, 1);
            }else {
                playerVotes.put(player, playerVotes.get(player) +1);
            }
        }
        return playerVotes;
    }


    private void teleportPlayersToEmergencyTable() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            Pos respawnPos = player.getComponent(RespawnComp.class).getPos();
            player.getComponent(PosComp.class).setPos(respawnPos);
            AppServer.currentGame.getEntityReturnBuffer().putEntity(player);
        }
    }

    private void immobilisePlayers() {//todo show of ease of ECS
        AppServer.currentGame.getStateManager().getCurrentState().removeSystem(PhysicsSystem.class);
    }

    private void mobilisePlayers() {//todo show of ease of ECS
        State currentState =  AppServer.currentGame.getStateManager().getCurrentState();
        currentState.addSystem(new PhysicsSystem(currentState.getEntities()));
    }

    private void broadcastDeadBodyReportedAnimation() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            ConnectionServer.sendTCP(AnimationFactory.createAnimationDisplayReturn("dead-body-reported"), player.getConnectionID());
        }
    }


    private void broadcastVotingPanel() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            ConnectionServer.sendTCP(new AddVotingPane(getPlayerVoteAnimations(), 20, 20, 300, 300), player.getConnectionID());
        }
    }

    private List<String> getPlayerVoteAnimations() {
        List<String> playerAnims = new ArrayList<>();
        for (Player player : AppServer.currentGame.getPlayers()) {
            playerAnims.add(getPlayerAnimationID(player));
        }
        return playerAnims;
    }


    private void enableVoiceChat() {
        ConnectionServer.sendTCPToAllPlayers(new OpenRecordHandler());
    }

    private void disableVoiceChat() {
        ConnectionServer.sendTCPToAllPlayers(new CloseRecordHandler());
    }

    public DeadPlayer getDeadPlayer() {
        return deadPlayer;
    }

    public void setDeadPlayer(DeadPlayer deadPlayer) {
        this.deadPlayer = deadPlayer;
    }

    public void processVote(Player voter, String candidateTexture) {
        Player candidate = getPlayerFromTexture(candidateTexture);
        playerVoteMap.put(voter, candidate);
    }

    private Player getPlayerFromTexture(String texture) {
        for (Player player : AppServer.currentGame.getPlayers()) {
            if (getPlayerAnimationID(player).equals(texture)) {
                return player;
            }
        }
        throw new IllegalArgumentException();
    }

    private String getPlayerAnimationID(Player player) {
        AnimationComp animationComp = player.getComponent(AnimationComp.class);
        if (player.getComponent(AliveComp.class).isAlive()) {
            return animationComp.getAnimation(AnimState.RIGHT).getFrames()[0];
        } else {
            return animationComp.getAnimation(AnimState.GHOST_RIGHT).getFrames()[0];
        }
    }
}

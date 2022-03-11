package ConnectionServer;

import Packet.Animation.AnimState;
import Packet.Animation.AnimationDisplayReturn;
import Packet.Animation.AnimationOver;
import Client.Client;
import Entity.Player;
import Packet.AddEntityReturn.*;
import Packet.Animation.NewAnimationReturn;
import Packet.Camera.ScrollingEnableReturn;
import Packet.CountDown.RemoveCountDown;
import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.ImpostorWin;
import Packet.GameStart.RoleNotify;
import Packet.GameStart.StartGameRequest;
import Packet.GameStart.StartGameReturn;
import Packet.LeaderBoard.RequestLeaderBoard;
import Packet.NestedPane.*;
import Packet.Packet;
import Packet.Position.*;
import Packet.Registration.LoginRequest;
import Packet.Registration.LogoutRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
import Packet.Sound.Sound;
import Packet.SudokuPacket.SudokuFailedReturn;
import Packet.CountDown.CountDown;
//import Packet.Timer.VotingCountDown;
import Packet.Voting.*;
import Position.Pos;
import StartUpServer.AppServer;
import Packet.SudokuPacket.VerifySudokuRequest;
import Packet.SudokuPacket.VerifySudokuReturn;
import Packet.UserData.UserData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import Packet.LeaderBoard.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import static StartUpServer.AppServer.currentGame;

public final class ConnectionServer {
    private static final Server server = new Server(20000, 20000);
    private static final PacketListenerServer packetListener = new PacketListenerServer();

    public static void sendUDP(Packet packet, int connectionID) {
        server.sendToUDP(connectionID, packet);
    }

    public static void sendTCP(Packet packet, int connectionID) {
        server.sendToTCP(connectionID, packet);
    }



    public static void sendUDPToAllPlayers(Packet packet){
        sendUDP(packet, getClientConnectionIDs());
    }

    public static void sendTCPToAllPlayers(Packet packet){
        sendTCP(packet, getPlayerConnectionIDs());
    }

    public static void sendTCP(Packet packet, List<Integer> connectionIDs){
        for (Integer connectionID: connectionIDs){
            ConnectionServer.sendTCP(packet, connectionID);
        }
    }

    public static void sendUDP(Packet packet, List<Integer> connectionIDs){
        for (Integer connectionID: connectionIDs){
            ConnectionServer.sendUDP(packet, connectionID);
        }
    }


    public static void sendUDPToAllExcept(Packet packet, int connectionID) {
        server.sendToAllExceptUDP(connectionID, packet);
    }


    public static void sendTCPToAllExcept(Packet packet, int connectionID) {
        server.sendToAllExceptTCP(connectionID, packet);
    }

    public static List<Integer> getClientConnectionIDs(){
        return getClientConnectionIDs(AppServer.getClients());
    }

    public static List<Integer> getClientConnectionIDs(List<Client> clients){
        return clients.stream().
                map(Client::getConnectionID).
                collect(Collectors.toList());
    }

    public static List<Integer> getPlayerConnectionIDs(){
        return getPlayerConnectionIDs(currentGame.getPlayers());
    }

    public static List<Integer> getPlayerConnectionIDs(List<Player> players){
        return players.stream().
                map(Player::getConnectionID).
                collect(Collectors.toList());
    }






    //    public boolean checkConnected(int connectionID){
//        for (Connection connection: server.getConnections()){
//            if (connection.getID() == connectionID){
//                return true;
//            }
//        }
//        return false;
//    }

    public static void start(){
        startConnection();
        registerPackets();
        server.addListener(packetListener);
    }

    private static void startConnection() {
        try {
            server.start();
            server.bind(49158, 65521);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<Client> getClientFromConnectionID(List<Client> clients, int connectionID) {//should this return optional
        for (Client client: clients){
            if (client.getConnectionID() == connectionID){
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    public static Optional<Client> getClientFromConnectionID(int connectionID){
        return getClientFromConnectionID(AppServer.getClients(), connectionID);
    }



    public static Optional<Player> getPlayerFromConnectionID(List<Player> players, int connectionID) {//should this return optional
        for (Player player: players){
            if (player.getConnectionID() == connectionID){
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static Optional<Player> getPlayerFromConnectionID( int connectionID) {//should this return optional
        for (Player player: currentGame.getPlayers()){
            if (player.getConnectionID() == connectionID){
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static int getConnectionIDFromPlayer(Player player){
        for (Client client: AppServer.getClients()){
            if (player == client.getPlayer()){
                return client.getConnectionID();
            }
        }
        return -1;
    }

    private static void registerPackets() {
        Kryo kryo = server.getKryo();
        kryo.register(LoginRequest.class);
        kryo.register(SignupRequest.class);
        kryo.register(RegistrationConfirmation.class);
        kryo.register(UserData.class);
        kryo.register(StartGameRequest.class);
        kryo.register(StartGameReturn.class);
        kryo.register(StateReturn.class);
        kryo.register(InputRequest.class);
        kryo.register(Pos.class);
//        kryo.register(AddStationaryEntityReturn.class);
        kryo.register(ExistingEntityState.class);
        kryo.register(ArrayList.class);
        kryo.register(AddLocalEntityReturn.class);
//        kryo.register(AddChangingEntityReturn.class);
        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(NewEntityState.class);
        kryo.register(String[].class);
        kryo.register(NewAnimationReturn[].class);
        kryo.register(NewAnimationReturn.class);
        kryo.register(AnimState.class);
        kryo.register(CountDown.class);
        kryo.register(ClearEntityReturn.class);
//        kryo.register(GameStartCountDown.class);
//        kryo.register(KillCoolDownCountDown.class);
        kryo.register(ScrollingEnableReturn.class);
        kryo.register(NewAnimatedEntityState.class);
        kryo.register(AddLineReturn.class);
        kryo.register(NewLineState.class);
        kryo.register(AddNestedPane.class);
        kryo.register(RemoveNestedScreen.class);
        kryo.register(AddsPane.class);
        kryo.register(AddSudokuPane.class);
        kryo.register(int[][].class);
        kryo.register(int[].class);
        kryo.register(VerifySudokuReturn.class);
        kryo.register(VerifySudokuRequest.class);
        kryo.register(Integer[][].class);
        kryo.register(Integer[].class);
        kryo.register(Integer.class);
        kryo.register(AnimationDisplayReturn.class);
        kryo.register(NodeInfo.class);
        kryo.register(NodeType.class);
        kryo.register(Sound.class);
        kryo.register(AddVotingPane.class);
        kryo.register(byte[].class);
        kryo.register(ImpostorVote.class);
//        kryo.register(VotingCountDown.class);
        kryo.register(ElectionReturn.class);
        kryo.register(DisplayVoteResults.class);
        kryo.register(HashMap.class);
        kryo.register(AnimationOver.class);
        kryo.register(TaskBarUpdate.class);
        kryo.register(AddEntityReturn.class);
        kryo.register(VoteOption.class);
        kryo.register(LogoutRequest.class);
        kryo.register(RoleNotify.class);
        kryo.register(CrewWin.class);
        kryo.register(ImpostorWin.class);
        kryo.register(ScreenInfo.class);
        kryo.register(SudokuFailedReturn.class);
        kryo.register(ChatMessageReturn.class);
        kryo.register(ChatMessageRequest.class);
        kryo.register(LeaderBoardReturn.class);
        kryo.register(RequestLeaderBoard.class);
        kryo.register(TreeMap.class);
        kryo.register(LinkedHashMap.class);
        kryo.register(RemoveCountDown.class);

    }


    public static Semaphore getServerSemaphor() {
        return packetListener.getSemaphore();
    }
}

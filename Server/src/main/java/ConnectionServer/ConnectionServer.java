package ConnectionServer;

import Animation.AnimState;
import Client.Client;
import Entity.Player;
import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.GameStart.StartGameRequest;
import Packet.GameStart.StartGameReturn;
import Packet.Packet;
import Packet.Position.*;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Packet.Timer.Timer;
import Position.Pos;
import StartUpServer.AppServer;
import UserData.UserData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public final class ConnectionServer {
    private static final Server server = new Server(20000, 20000);

    public static void sendUDP(Packet packet, int connectionID) {
        server.sendToUDP(connectionID, packet);
    }

    public static void sendUDPToAllPlayers(Packet packet){
        sendUDPTo(packet, getClientConnectionIDs());
//        for (Client client: AppServer.currentGame.getClients()){
//            ConnectionServer.sendUDP(packet, client.getConnectionID());
//        }
    }

    public static void sendTCPToAllPlayers(Packet packet){
        sendTCPTo(packet, getClientConnectionIDs());
//        for (Client client: AppServer.currentGame.getClients()){
//            ConnectionServer.sendTCP(packet, client.getConnectionID());
//        }

    }

    public static void sendTCPTo(Packet packet, List<Integer> connectionIDs){
        for (Integer connectionID: connectionIDs){
            ConnectionServer.sendTCP(packet, connectionID);
        }
    }

    public static void sendUDPTo(Packet packet, List<Integer> connectionIDs){
        for (Integer connectionID: connectionIDs){
            ConnectionServer.sendUDP(packet, connectionID);
        }
    }


    public static void sendUDPToAllExcept(Packet packet, int connectionID) {
        server.sendToAllExceptUDP(connectionID, packet);
    }

    public static void sendTCP(Packet packet, int connectionID) {
        server.sendToTCP(connectionID, packet);
    }

    public static void sendTCPToAllExcept(Packet packet, int connectionID) {
        server.sendToAllExceptTCP(connectionID, packet);
    }

    public static List<Integer> getClientConnectionIDs(){
        return getClientConnectionIDs(AppServer.currentGame.getClients());
    }

    public static List<Integer> getClientConnectionIDs(List<Client> clients){
        return clients.stream().
                map(Client::getConnectionID).
                collect(Collectors.toList());
    }

    public static List<Integer> getPlayerConnectionIDs(){
        return getPlayerConnectionIDs(AppServer.currentGame.getPlayers());
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
        server.addListener(new PacketListenerServer());
    }

    private static void startConnection() {
        try {
            server.start();
            server.bind(49158, 65529);
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

    public static int getConnectionIDFromPlayer(Player player){
        for (Client client: AppServer.currentGame.getClients()){
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
        kryo.register(PosRequest.class);
        kryo.register(Pos.class);
        kryo.register(AddStationaryEntityReturn.class);
        kryo.register(ExistingEntityState.class);
        kryo.register(ArrayList.class);
        kryo.register(AddLocalEntityReturn.class);
        kryo.register(AddChangingEntityReturn.class);
        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(NewEntityState.class);
        kryo.register(String[].class);
        kryo.register(Animation.NewAnimationReturn[].class);
        kryo.register(Animation.NewAnimationReturn.class);
        kryo.register(AnimState.class);
        kryo.register(Timer.class);
        kryo.register(ClearEntityReturn.class);
        kryo.register(GameStartTimer.class);
        kryo.register(KillCoolDownTimer.class);
        kryo.register(ScrollingEnableReturn.class);
        kryo.register(NewAnimatedEntityState.class);
        kryo.register(AddLineReturn.class);
        kryo.register(NewLineState.class);
        kryo.register(AddNestedPane.class);




    }


}

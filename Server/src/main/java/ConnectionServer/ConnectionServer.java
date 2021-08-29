package ConnectionServer;

import Animation.AnimState;
import Client.Client;
import Packet.GameStart.StartGameRequest;
import Packet.GameStart.StartGameReturn;
import Packet.Packet;
import Packet.Position.*;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.Timer.GameStartTimerReturn;
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

public final class ConnectionServer {
    private static final Server server = new Server();

    public static void sendUDP(Packet packet, int connectionID) {
        server.sendToUDP(connectionID, packet);
    }

    public static void sendUDPToAllPlayers(Packet packet){
        for (Client client: AppServer.currentGame.getClients()){
            ConnectionServer.sendUDP(packet, client.getConnectionID());
        }
    }

    public static void sendTCPToAllPlayers(Packet packet){
        for (Client client: AppServer.currentGame.getClients()){
            ConnectionServer.sendTCP(packet, client.getConnectionID());
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
            server.bind(49159, 65528);
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
        kryo.register(EntityState.class);
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
        kryo.register(GameStartTimerReturn.class);
        kryo.register(ClearWorldReturn.class);

    }


}

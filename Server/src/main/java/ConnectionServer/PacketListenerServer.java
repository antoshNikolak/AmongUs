package ConnectionServer;

import Packet.GameStart.StartGameRequest;
import Packet.Packet;
import Packet.Position.PosRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.SignupRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class PacketListenerServer extends Listener {
    private final PacketControllerServer packetController = new PacketControllerServer();
    @Override
    public void connected(Connection connection) {
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object packet) {
        if (packet instanceof LoginRequest){
           packetController.handleLogin((LoginRequest) packet, connection.getID());
        }else if (packet instanceof SignupRequest){
            packetController.handleSignup((SignupRequest)packet, connection.getID());
        }else if (packet instanceof StartGameRequest){
            packetController.handleStartGameRequest(connection.getID());
        }else if (packet instanceof PosRequest){ ;
            packetController.handlePosRequest((PosRequest)packet, connection.getID());
        }
    }
}

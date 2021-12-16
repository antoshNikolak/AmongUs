package ConnectionServer;

import Packet.Animation.AnimationOver;
import Packet.GameStart.StartGameRequest;
import Packet.Position.PosRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.LogoutRequest;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
import Packet.Sound.Sound;
import StartUpServer.AppServer;
import Packet.SudokuPacket.VerifySudokuRequest;
import Packet.Voting.ImpostorVote;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
        } else if (packet instanceof LogoutRequest){
            packetController.handleLogout(connection.getID());
        }else if (packet instanceof ScreenInfo){
            packetController.handleScreenInfo((ScreenInfo) packet);
        }
        if (AppServer.currentGame == null)return;
        if (packet instanceof PosRequest){ ;
            packetController.handlePosRequest((PosRequest)packet, connection.getID());
        }else if(packet instanceof VerifySudokuRequest){
            packetController.handleVerifySudokuRequest((VerifySudokuRequest)packet, connection.getID());
        }else if (packet instanceof Sound){
            packetController.handleVoiceChat((Sound) packet, connection.getID());
        }else if (packet instanceof ImpostorVote){
            packetController.handleImpostorVote((ImpostorVote) packet, connection.getID());
        }else if (packet instanceof AnimationOver){
//            packetController.handleAnimationOver((AnimationOver) packet, connection.getID());
        }
    }
}

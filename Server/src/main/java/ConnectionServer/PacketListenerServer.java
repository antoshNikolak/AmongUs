package ConnectionServer;

import Client.Client;
import Component.ImpostorComp;
import EndGameHandler.EndGameHandler;
import Packet.Animation.AnimationOver;
import Packet.GameStart.StartGameRequest;
import Packet.LeaderBoard.RequestLeaderBoard;
import Packet.Position.InputRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.LogoutRequest;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
import Packet.Sound.Sound;
import Packet.Voting.ChatMessageRequest;
import Registry.RegistryHandler;
import StartUpServer.AppServer;
import Packet.SudokuPacket.VerifySudokuRequest;
import Packet.Voting.ImpostorVote;
import State.GameState;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.concurrent.Semaphore;

public class PacketListenerServer extends Listener {
    private final PacketControllerServer packetController = new PacketControllerServer();
    private final Semaphore semaphore = new Semaphore(1);

    @Override
    public void disconnected(Connection connection) {//method invoked when client disconnects
        Client client = ConnectionServer.getClientFromConnectionID(connection.getID());
        AppServer.getClients().remove(client);//remove as a client

        if (client.getPlayer() != null) {
            AppServer.currentGame.getPlayers().remove(client.getPlayer());//remove player from game server
            RegistryHandler.entityRegistryServer.removeEntity(client.getPlayer());//de register entity, remove on client side
        }

        if (AppServer.currentGame != null && AppServer.currentGame.getStateManager().hasState(GameState.class)) {
            EndGameHandler endGameHandler = AppServer.currentGame.getStateManager().getState(GameState.class).getEndGameHandler();
            if (endGameHandler != null) {
                if (client.getPlayer().hasComponent(ImpostorComp.class)) {//check if the game has been won
                    endGameHandler.handleCrewWin();
                } else {
                    endGameHandler.checkImpostorWin();
                }
            }
        }
//        }


    }

    @Override
    public void received(Connection connection, Object packet) {//method invoked when when packet for client received
        try {
            this.semaphore.acquire();
            handlePacket(connection, packet);
            this.semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void handlePacket(Connection connection, Object packet) {
        if (packet instanceof LoginRequest) {
            packetController.handleLogin((LoginRequest) packet, connection.getID());
        } else if (packet instanceof SignupRequest) {
            packetController.handleSignup((SignupRequest) packet, connection.getID());
        } else if (packet instanceof StartGameRequest) {
            packetController.handleStartGameRequest(connection.getID());
        } else if (packet instanceof LogoutRequest) {
            packetController.handleLogout(connection.getID());
        } else if (packet instanceof ScreenInfo) {
            packetController.handleScreenInfo((ScreenInfo) packet);
        } else if (packet instanceof RequestLeaderBoard) {
            packetController.handleLeaderBoardRequest(connection.getID());
        }
        if (AppServer.currentGame == null) return;
        if (packet instanceof InputRequest) {
            packetController.handleKeyBoardInput((InputRequest) packet, connection.getID());
        } else if (packet instanceof VerifySudokuRequest) {
            packetController.handleVerifySudokuRequest((VerifySudokuRequest) packet, connection.getID());
        } else if (packet instanceof ImpostorVote) {
            packetController.handleImpostorVote((ImpostorVote) packet, connection.getID());
        }else if (packet instanceof ChatMessageRequest) {
            packetController.handleChatMessageRequest((ChatMessageRequest) packet, connection.getID());
        }
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

}

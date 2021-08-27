package State;

import Client.Client;
import Component.ColourComp;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Packet.Position.AddLocalEntityReturn;
import Packet.Position.AddChangingEntityReturn;
import Packet.Position.NewEntityState;
import StartUpServer.AppServer;
import System.TextureSystem;

public class LobbyState extends GameState{

    public LobbyState() {
        super();
    }

    public void handleNewPlayerJoin(Client client){
        sendPlayerData(client);
        sendWorldData(client.getConnectionID());
        AppServer.currentGame.getClients().add(client);
    }

    private void sendPlayerData( Client client){
        sendExistingPlayersToClient(client.getConnectionID());
        sendNewPlayerToAll(client);
    }

    private void sendExistingPlayersToClient(int connectionID) {
        for (Client client: AppServer.currentGame.getClients()){
            Player player = client.getPlayer();
            ConnectionServer.sendTCP(player.adaptToEntityState(), connectionID);
        }
    }

    private void sendNewPlayerToAll(Client client){
        NewEntityState playerState = client.getPlayer().adaptToNewEntityState();
        ConnectionServer.sendTCP(new AddLocalEntityReturn(playerState), client.getConnectionID());
        ConnectionServer.sendTCPToAllExcept(new AddChangingEntityReturn(playerState), client.getConnectionID());
    }
}

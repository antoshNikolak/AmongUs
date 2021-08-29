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
import World.World;

public class LobbyState extends GameState{

    public LobbyState() {
        super();
    }

    public void handleNewPlayerJoin(Client client){
        sendPlayerData(client);
        sendWorldData(client.getConnectionID());
        AppServer.currentGame.getClients().add(client);
    }

    @Override
    public void init() {
        startSystems();
        createWord();
    }

    @Override
    protected void createWord() {
        this.world = new World("World/lobby.txt");
    }

    private void sendPlayerData(Client client){
        sendExistingPlayersToClient(client.getConnectionID());
        sendNewPlayerToAll(client);
    }

    private void sendExistingPlayersToClient(int connectionID) {
        for (Client client: AppServer.currentGame.getClients()){
            ConnectionServer.sendTCP(new AddChangingEntityReturn(client.getPlayer().adaptToNewEntityState()), connectionID);
        }
    }

    private void sendNewPlayerToAll(Client client){
        NewEntityState playerState = client.getPlayer().adaptToNewEntityState();
        ConnectionServer.sendTCP(new AddLocalEntityReturn(playerState), client.getConnectionID());
        ConnectionServer.sendTCPToAllExcept(new AddChangingEntityReturn(playerState), client.getConnectionID());
    }
}

package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Packet.AddEntityReturn.AddLocalEntityReturn;
import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import StartUpServer.AppServer;
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
            ConnectionServer.sendTCP(new AddChangingEntityReturn(client.getPlayer().adaptToNewAnimatedEntityState()), connectionID);
        }
    }

    private void sendNewPlayerToAll(Client client){
        NewAnimatedEntityState playerState = client.getPlayer().adaptToNewAnimatedEntityState();
        ConnectionServer.sendTCP(new AddLocalEntityReturn(playerState), client.getConnectionID());
        ConnectionServer.sendTCPToAllExcept(new AddChangingEntityReturn(playerState), client.getConnectionID());
    }
}

package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Packet.AddEntityReturn.AddEntityReturn;
import Packet.AddEntityReturn.AddLocalEntityReturn;
//import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import StartUpServer.AppServer;
import World.World;
import System.*;

public class LobbyState extends PlayingState{

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
        createWorld();
    }

    @Override
    protected void startSystems() {
        this.addSystem(new PhysicsSystem(entities));
        this.addSystem(new TextureSystem());
        this.addSystem(new ImposterActionsSystem());
    }

    @Override
    protected void createWorld() {
        this.world = new World("World/lobby.txt");
    }

    private void sendPlayerData(Client client){
        sendExistingPlayersToClient(client.getConnectionID());
        sendNewPlayerToAll(client);
        client.setInGame(true);
    }

    private void sendExistingPlayersToClient(int connectionID) {
        for (Client client: AppServer.currentGame.getClients()){
//            if (isClientInAGame(client)) {
                ConnectionServer.sendTCP(new AddEntityReturn(client.getPlayer().adaptToNewAnimatedEntityState(true)), connectionID);

        }
    }

    private boolean isClientInAGame(Client client){
        return client.getPlayer() != null;
    }

    private void sendNewPlayerToAll(Client client){
        NewAnimatedEntityState playerState = client.getPlayer().adaptToNewAnimatedEntityState(true);
        ConnectionServer.sendTCP(new AddLocalEntityReturn(playerState), client.getConnectionID());
        for (Client client1: AppServer.currentGame.getClients())
            if (client1.getConnectionID() != client.getConnectionID() && client1.isInGame())
        ConnectionServer.sendTCPToAllExcept(new AddEntityReturn(playerState), client.getConnectionID());
    }

    @Override
    public void removeClient(Client client) {

    }
}

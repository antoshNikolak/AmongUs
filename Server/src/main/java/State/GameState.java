package State;

import Client.Client;
import Component.PosComp;
import Component.RenderComp;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Packet.Position.ClearWorldReturn;
import Packet.Position.EntityState;
import Packet.Position.AddStationaryEntityReturn;
import Packet.Position.NewEntityState;
import StartUpServer.AppServer;
import System.PhysicsSystem;
import World.World;
import System.*;
import Entity.*;

import java.util.ArrayList;
import java.util.List;

public class GameState extends State {

    protected World world;

    public GameState() {
        super();
    }

    @Override
    protected void startSystems() {
        this.addSystem(new PhysicsSystem());
        this.addSystem(new TextureSystem());
    }

    @Override
    public void init() {
        startSystems();
        addPlayersAsEntities();
        createWord();
        sendWorldDataToAllPlayers();
    }

    protected void createWord(){
        this.world = new World("World/game-map.txt");
    }

    private void addPlayersAsEntities(){
        AppServer.currentGame.getClients().stream().
                map(client -> client.getPlayer()).
                forEach(player ->{
                    System.out.println("adding player");
                    entities.add(player);
                } );
    }

    public void close(){
        clearWorldData();
    }

    private void clearWorldData(){
        ConnectionServer.sendTCPToAllPlayers(new ClearWorldReturn(world.getTileIDs()));
        //todo deregister tiles
    }

    private void sendWorldDataToAllPlayers(){
        List<NewEntityState> newEntities = createEntityStatesFromTiles();
        ConnectionServer.sendTCPToAllPlayers(new AddStationaryEntityReturn(newEntities));
    }

    protected void sendWorldData(int connectionID){
        List<NewEntityState> newEntities = createEntityStatesFromTiles();
        ConnectionServer.sendTCP(new AddStationaryEntityReturn(newEntities), connectionID);
    }

    private List<NewEntityState> createEntityStatesFromTiles(){
        List<NewEntityState> newEntities = new ArrayList<>();
        for (Tile tile : world.getTiles()) {
            newEntities.add( tile.adaptToNewEntityState());
        }
        return newEntities;
    }




    @Override
    public void removeClient(Client client) {
        //todo
    }

    public World getWorld() {
        return world;
    }

}

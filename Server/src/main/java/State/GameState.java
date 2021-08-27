package State;

import Client.Client;
import Component.PosComp;
import Component.RenderComp;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Packet.Position.EntityState;
import Packet.Position.AddStationaryEntityReturn;
import Packet.Position.NewEntityState;
import System.PhysicsSystem;
import World.World;
import System.*;

import java.util.ArrayList;
import java.util.List;

public class GameState extends State {

//    private final List<Entity> entities = new ArrayList<>();
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
        this.world = new World("World/lobby.txt");
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

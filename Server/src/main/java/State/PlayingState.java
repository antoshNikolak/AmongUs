package State;

import ConnectionServer.ConnectionServer;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Packet.AddEntityReturn.AddEntityReturn;
//import Packet.AddEntityReturn.AddStationaryEntityReturn;
import Packet.EntityState.NewEntityState;
import Packet.Position.ClearEntityReturn;
import World.World;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayingState extends State {

    protected World world;

    public void close() {
        clearWorldData();
    }

    private void clearWorldData() {
        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(world.getTileIDs()));
        for (Integer ID: world.getTileIDs()) {
            EntityRegistryServer.unregisterEntity(ID);
        }
    }

    protected void sendWorldDataToAllPlayers() {
        List<NewEntityState> newEntities = createEntityStatesFromTiles();
        ConnectionServer.sendTCPToAllPlayers(new AddEntityReturn(newEntities));
    }

    protected void sendWorldData(int connectionID) {
        List<NewEntityState> newEntities = createEntityStatesFromTiles();
        ConnectionServer.sendTCP(new AddEntityReturn(newEntities), connectionID);
    }

    private List<NewEntityState> createEntityStatesFromTiles() {
        List<NewEntityState> newEntities = new ArrayList<>();
        for (Tile tile : world.getTiles()) {
            newEntities.add(tile.adaptToNewAnimatedEntityState(true));
        }
        return newEntities;
    }

    protected abstract void createWorld();

    public World getWorld() {
        return world;
    }

    //    protected void createWord() {
//        this.world = new World("World/game-map.txt");
//    }

}

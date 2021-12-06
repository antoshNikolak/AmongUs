package State;

import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import Entity.EntityRegistryServer;
import Entity.Player;
import Entity.Tile;
import Packet.AddEntityReturn.AddEntityReturn;
//import Packet.AddEntityReturn.AddStationaryEntityReturn;
import Packet.EntityState.NewEntityState;
import Packet.Position.ClearEntityReturn;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;
import World.World;
import System.*;

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


//    @Override
//    public void processPlayingSystems(Player player, PosRequest packet) {
//        super.processPlayingSystems(player, packet);
//        if (hasSystem(ImposterActionsSystem.class)) {
//            getSystem(ImposterActionsSystem.class).handleSpecialActions(player, packet);
//        }
//        if (hasSystem(CrewMateActionSystem.class)) {
//            getSystem(CrewMateActionSystem.class).handleSpecialAction(player, packet);
//        }
//        if (hasSystem(ReportBodySystem.class)) {
//            getSystem(ReportBodySystem.class).handleReport(player, packet);
//        }
//        if (hasSystem(TaskSystem.class)) {
//            getSystem(TaskSystem.class).handleTaskAction(player, packet);
//        }
//        if (packet.isEmergencyMeetingKey()) {
//            if (!hasSystem(EmergencyTableSystem.class)) {
//                double distance = DistanceFinder.getDistanceBetweenEntities(player, AppServer.currentGame.getStateManager().getCurrentState().getWorld().getMainTable()).getDistance();
//                if (distance < 100) {
//                    EmergencyTableSystem emergencyTableSystem = new EmergencyTableSystem();
//                    addSystem(emergencyTableSystem);
//                    emergencyTableSystem.activate();
//                }
//            }
//        }
//    }

    protected abstract void createWorld();

    public World getWorld() {
        return world;
    }

    //    protected void createWord() {
//        this.world = new World("World/game-map.txt");
//    }

}

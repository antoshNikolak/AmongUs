package State;

import Client.Client;
import Component.ColourComp;
import Component.ImposterComp;
import Component.RoleComp;
import ConnectionServer.ConnectionServer;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Packet.Position.ClearEntityReturn;
import Packet.Position.AddStationaryEntityReturn;
import Packet.Position.NewEntityState;
import StartUpServer.AppServer;
import System.PhysicsSystem;
import World.World;
import System.*;
import Entity.*;
import Component.RoleComp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        selectImpostor();
    }

    private void selectImpostor() {
        Player imposter = getRandomPlayer();
        for (Player player: AppServer.currentGame.getPlayers()){
            if (player == imposter){
                player.addComponent(new ImposterComp());
                System.out.println(player.getComponent(ColourComp.class).getColour() + "is the impostor");
            }
//            else {
//                player.addComponent(new RoleComp(Role.CREWMATE));
//            }
        }
    }

    private Player getRandomPlayer() {
        List<Player> players = AppServer.currentGame.getPlayers();
        int index = new Random().nextInt(players.size());
        return players.get(index);
    }

    protected void createWord() {
        this.world = new World("World/game-map.txt");
    }

    private void addPlayersAsEntities() {
        entities.addAll(AppServer.currentGame.getPlayers());
//        AppServer.currentGame.getClients().stream().
//                map(client -> client.getPlayer()).
//                forEach(player ->{
//                    System.out.println("adding player");
//                    entities.add(player);
//                } );
    }

    public void close() {
        clearWorldData();
    }

    private void clearWorldData() {
        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(world.getTileIDs()));
        for (Integer ID: world.getTileIDs()) {
            EntityRegistryServer.removeEntity(ID);
        }
    }

    private void sendWorldDataToAllPlayers() {
        List<NewEntityState> newEntities = createEntityStatesFromTiles();
        ConnectionServer.sendTCPToAllPlayers(new AddStationaryEntityReturn(newEntities));
    }

    protected void sendWorldData(int connectionID) {
        List<NewEntityState> newEntities = createEntityStatesFromTiles();
        ConnectionServer.sendTCP(new AddStationaryEntityReturn(newEntities), connectionID);
    }

    private List<NewEntityState> createEntityStatesFromTiles() {
        List<NewEntityState> newEntities = new ArrayList<>();
        for (Tile tile : world.getTiles()) {
            newEntities.add(tile.adaptToNewEntityState());
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

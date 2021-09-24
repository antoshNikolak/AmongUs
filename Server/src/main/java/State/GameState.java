package State;

import Client.Client;
import Component.ColourComp;
import Component.ImposterComp;
import Component.PosComp;
import ConnectionServer.ConnectionServer;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Packet.Position.ClearEntityReturn;
import Packet.AddEntityReturn.AddStationaryEntityReturn;
import Packet.EntityState.NewEntityState;
import Packet.Camera.ScrollingEnableReturn;
import Position.Pos;
import StartUpServer.AppServer;
import System.PhysicsSystem;
import World.World;
import System.*;
import Entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState extends State {

    protected World world;

    public GameState() {
        super(false);
    }

    @Override
    protected void startSystems() {
        this.addSystem(new PhysicsSystem(entities));
        this.addSystem(new TextureSystem());
        this.addSystem(new TaskSystem());
        this.addSystem(new ImposterActionsSystem());
    }

    @Override
    public void init() {
        startSystems();
        addPlayersAsEntities();
        createWord();
        sendWorldDataToAllPlayers();
        selectImpostor();
        enableClientScreenScrolling();
    }

    private void enableClientScreenScrolling(){
        for (Client client: AppServer.currentGame.getClients()){
            Pos pos = client.getPlayer().getComponent(PosComp.class).getPos();
            ConnectionServer.sendTCP(new ScrollingEnableReturn(pos), client.getConnectionID());
        }
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
        System.out.println("world created");
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
            newEntities.add(tile.adaptToNewAnimatedEntityState());
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

package State;

import Client.Client;
import Component.AnimationComp;
import Component.ColourComp;
import Component.ImposterComp;
import Component.PosComp;
import ConnectionServer.ConnectionServer;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Packet.AddEntityReturn.AddEntityReturn;
import Packet.Position.ClearEntityReturn;
//import Packet.AddEntityReturn.AddStationaryEntityReturn;
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
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState extends PlayingState {
    private TaskBar taskBar;

    public GameState() {
        super();
    }

    @Override
    protected void startSystems() {
        this.addSystem(new PhysicsSystem(entities));
        this.addSystem(new TextureSystem());
        this.addSystem(new TaskSystem());
        this.addSystem(new ImposterActionsSystem());
        this.addSystem(new ReportBodySystem());
    }

    @Override
    public void init() {
        startSystems();
        addPlayersAsEntities();
        createWorld();
        sendWorldDataToAllPlayers();
        selectImpostor();
        enableClientScreenScrolling();
        addTaskBar();
    }

    private void addTaskBar(){
        this.taskBar = new TaskBar();
        this.entities.add(taskBar);
        ConnectionServer.sendTCPToAllPlayers(new AddEntityReturn(taskBar.adaptToNewAnimatedEntityState(false)));
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
        }
    }

    private Player getRandomPlayer() {
        List<Player> players = AppServer.currentGame.getPlayers();
        int index = new Random().nextInt(players.size());
        return players.get(index);
    }



    private void addPlayersAsEntities() {
        entities.addAll(AppServer.currentGame.getPlayers());
    }

//    public void close() {
//        clearWorldData();
//    }
//
//    private void clearWorldData() {
//        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(world.getTileIDs()));
//        for (Integer ID: world.getTileIDs()) {
//            EntityRegistryServer.removeEntity(ID);
//        }
//    }

//    private void sendWorldDataToAllPlayers() {
//        List<NewEntityState> newEntities = createEntityStatesFromTiles();
//        ConnectionServer.sendTCPToAllPlayers(new AddStationaryEntityReturn(newEntities));
//    }
//
//    protected void sendWorldData(int connectionID) {
//        List<NewEntityState> newEntities = createEntityStatesFromTiles();
//        ConnectionServer.sendTCP(new AddStationaryEntityReturn(newEntities), connectionID);
//    }

//    private List<NewEntityState> createEntityStatesFromTiles() {
//        List<NewEntityState> newEntities = new ArrayList<>();
//        for (Tile tile : world.getTiles()) {
//            newEntities.add(tile.adaptToNewAnimatedEntityState());
//        }
//        return newEntities;
//    }

    @Override
    protected void createWorld() {
        this.world = new World("World/game-map.txt");
    }

    public TaskBar getTaskBar() {
        return taskBar;
    }

    public void setTaskBar(TaskBar taskBar) {
        this.taskBar = taskBar;
    }

    @Override
    public void removeClient(Client client) {
        //todo
    }



}

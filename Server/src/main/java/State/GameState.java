package State;

import Client.Client;
import Component.ColourComp;
import Component.ImposterComp;
import Component.PosComp;
import ConnectionServer.ConnectionServer;
import Packet.AddEntityReturn.AddEntityReturn;
//import Packet.AddEntityReturn.AddStationaryEntityReturn;
import Packet.Camera.ScrollingEnableReturn;
import Packet.GameStart.RoleNotify;
import Packet.Position.PosRequest;
import Position.Pos;
import StartUpServer.AppServer;
import System.PhysicsSystem;
import World.World;
import System.*;
import Entity.*;

import java.util.List;
import java.util.Random;

import static StartUpServer.AppServer.currentGame;

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

    private void addTaskBar() {
        this.taskBar = new TaskBar();
        this.entities.add(taskBar);
        ConnectionServer.sendTCPToAllPlayers(new AddEntityReturn(taskBar.adaptToNewAnimatedEntityState(false)));
    }

    private void enableClientScreenScrolling() {
        for (Client client : AppServer.getClients()) {
            Pos pos = client.getPlayer().getComponent(PosComp.class).getPos();
            ConnectionServer.sendTCP(new ScrollingEnableReturn(pos), client.getConnectionID());
        }
    }

    private void selectImpostor() {
        Player imposter = getRandomPlayer();
        imposter.addComponent(new ImposterComp());
        System.out.println(imposter.getComponent(ColourComp.class).getColour() + "is the impostor");
        ConnectionServer.sendTCP(new RoleNotify(true), imposter.getConnectionID());
        ConnectionServer.sendTCPToAllExcept(new RoleNotify(false), imposter.getConnectionID());

//        for (Player player : currentGame.getPlayers()) {
//            if (player == imposter) {
//                player.addComponent(new ImposterComp());
//                System.out.println(player.getComponent(ColourComp.class).getColour() + "is the impostor");
//            }
//        }
    }

    private Player getRandomPlayer() {
        List<Player> players = currentGame.getPlayers();
        int index = new Random().nextInt(players.size());
        return players.get(index);
    }


    private void addPlayersAsEntities() {
        entities.addAll(currentGame.getPlayers());
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
//            getSystem(TaskSystem.class).handleTaskAction(player, packet);//maybe use polymorphism to have a handle method in each system
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


//    @Override
//    public void processPlayingSystems(Player player, PosRequest packet) {
//        super.processPlayingSystems(player, packet);
//        synchronized (this) {
//            if (packet.isEmergencyMeetingKey()) {
//                if (!hasSystem(EmergencyTableSystem.class)) {
//                    EmergencyTableSystem emergencyTableSystem = new EmergencyTableSystem();
//                    addSystem(emergencyTableSystem);
//                    emergencyTableSystem.handleAction(player, packet);
//                }
//            }
//        }
//    }


    @Override
    public void processPlayingSystems(Player player, PosRequest packet) {
        super.processPlayingSystems(player, packet);
        if (packet.isEmergencyMeetingKey() && !hasSystem(EmergencyTableSystem.class)) {
            new EmergencyTableSystem().handleAction(player, packet);
        }
    }

    @Override
        protected void createWorld () {
            this.world = new World("World/game-map.txt");
        }

        public TaskBar getTaskBar () {
            return taskBar;
        }

        public void setTaskBar (TaskBar taskBar){
            this.taskBar = taskBar;
        }

        @Override
        public void removeClient (Client client){
            //todo
        }


    }

package State;

import Component.ColourComp;
import Component.ImpostorComp;
import Component.PosComp;
import ConnectionServer.ConnectionServer;
import EndGameHandler.EndGameHandler;
import Packet.AddEntityReturn.AddEntityReturn;
//import Packet.AddEntityReturn.AddStationaryEntityReturn;
import Packet.Camera.ScrollingEnableReturn;
import Packet.GameStart.RoleNotify;
import Position.Pos;
import System.PhysicsSystem;
import TimerHandler.StopWatch;
import World.World;
import System.*;
import Entity.*;

import java.util.List;
import java.util.Random;

import static StartUpServer.AppServer.currentGame;

public class GameState extends PlayingState {
    private TaskBar taskBar;
    private StopWatch gameDurationStopWatch;
    private final EndGameHandler endGameHandler = new EndGameHandler();

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
        startGameDurationTimer();
    }

    private void startGameDurationTimer() {
        this.gameDurationStopWatch = new StopWatch(StopWatch.Precision.MILLIS);
        this.gameDurationStopWatch.start();
    }

    public double stopGameDurationTimer() {
        return gameDurationStopWatch.stop();
    }

    private void addTaskBar() {
        this.taskBar = new TaskBar();
        this.entities.add(taskBar);
        ConnectionServer.sendTCPToAllPlayers(new AddEntityReturn(taskBar.adaptToNewAnimatedEntityState(false)));
    }

    private void enableClientScreenScrolling() {
        for (Player player : currentGame.getPlayers()) {
            Pos pos = player.getComponent(PosComp.class).getPos();
            ConnectionServer.sendTCP(new ScrollingEnableReturn(pos), player.getConnectionID());
        }
    }

    private void selectImpostor() {
        Player imposter = getRandomPlayer();
        imposter.addComponent(new ImpostorComp());
        System.out.println(imposter.getComponent(ColourComp.class).getColour() + "is the impostor");
        ConnectionServer.sendTCP(new RoleNotify(true), imposter.getConnectionID());
        ConnectionServer.sendTCPToAllExcept(new RoleNotify(false), imposter.getConnectionID());
    }

    private Player getRandomPlayer() {
        System.out.println(currentGame.getPlayers().size());
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


//    @Override
//    public void processPlayingSystems(Player player, PosRequest packet) {
//        super.processPlayingSystems(player, packet);
////        if (packet.isEmergencyMeetingKey() && !hasSystem(EmergencyTableSystem.class)) {
////            new EmergencyTableSystem().handleAction(player, packet);
////        }
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

    public EndGameHandler getEndGameHandler() {
        return endGameHandler;
    }
}

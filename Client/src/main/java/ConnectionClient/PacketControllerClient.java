package ConnectionClient;

import Animation.AnimationDisplayReturn;
import EntityClient.*;
import Game.Game;
import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.EntityState.*;
import Packet.GameEnd.GameEnd;
import Packet.GameStart.RoleNotify;
import Packet.GameStart.StartGameReturn;
import Packet.NestedPane.AddNestedPane;
import Packet.NestedPane.AddSudokuPane;
import Packet.NestedPane.AddVotingPane;
import Packet.NestedPane.DisplayVoteResults;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Sound.Sound;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Packet.Timer.VotingTimer;
import Screen.*;
import StartUp.AppClient;
import SudokuHandler.SudokuHandler;
import SudokuPacket.VerifySudokuReturn;
import TaskBar.TaskBarHandler;
import javafx.application.Platform;
import ScreenCounter.*;
import SudokuHandler.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class PacketControllerClient {


    public void handleRegistrationConfirmation(RegistrationConfirmation packet) {
        if (packet.isAuthorized()) {
            Platform.runLater(() -> ScreenManager.activate(MenuScreen.class));
        } else {
            System.out.println("client not authorized");
        }//todo unnecessary?
    }

    public void handleStartGameReturn(StartGameReturn packet) {//handle new stationary entity came first once, why?
        if (packet.isAuthorizedToStartGame()) {
            AppClient.currentGame = new Game();
            System.out.println("Game started");
        } else {
            throw new IllegalStateException("client not registered to start game");
        }
    }

//    public void handleNewStationaryEntityReturn(AddStationaryEntityReturn packet) {
//        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
//            if (newEntityState instanceof NewAnimatedEntityState) {
//                ScreenManager.getScreen(GameScreen.class).getEntities().add(new Entity((NewAnimatedEntityState) newEntityState));
//            }
//        }//todo re use code, make stationary class and changing class same thing
//    }
//
//    public void handleAddChangingEntityReturn(AddChangingEntityReturn packet) {
//        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
////            if (newEntityState == null) {//todo temp
////                System.out.println("null new entity state");
////                continue;
////            }
//            if (newEntityState instanceof NewAnimatedEntityState) {
//                ScreenManager.getScreen(GameScreen.class).getEntities().add(new Entity((NewAnimatedEntityState) newEntityState));
//                System.out.println("added new entity state");
//            }
//
//        }
//    }

    public void handleAddEntityReturn(AddEntityReturn packet) {
        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
            if (newEntityState instanceof NewAnimatedEntityState) {
                Entity entity = new Entity((NewAnimatedEntityState) newEntityState);
                entity.setScrollable(((NewAnimatedEntityState) newEntityState).isScrollable());
                ScreenManager.getScreen(GameScreen.class).getEntities().add(entity);
            }

        }
    }

    public void handleAddLineReturn(AddLineReturn packet) {
        for (NewLineState newLineState : packet.getNewEntityStates()) {
            Platform.runLater(() -> {
                Line line = new Line(newLineState.getStartPos().getX(), newLineState.getStartPos().getY(), newLineState.getFinalPos().getX(), newLineState.getFinalPos().getY());
                ScreenManager.getCurrentScreen().getPane().getChildren().add(line);
            });
        }
    }

    public void handleAddLocalEntityReturn(AddLocalEntityReturn packet) {
        NewAnimatedEntityState entityState = packet.getNewEntityState();
        AppClient.currentGame.handleLocalPlayer(new LocalPlayer(entityState));
    }


    public void handleStateReturn(StateReturn packet) {
        Entity.calculateTimeDiffBetweenPackets();
        for (ExistingEntityState existingEntityState : packet.getEntityStates()) {
            Entity entity = EntityRegistryClient.getEntity(existingEntityState.getRegistrationID());
            if (entity == null) continue;
            entity.changeAttributes(existingEntityState.getAnimState(), existingEntityState.getAnimationIndex(), existingEntityState.getPos());
        }
    }

    private final Counter gameStartCounter = new Counter(300, 200, 50);

    public void handleGameStartTimerReturn(GameStartTimer packet) {
        Platform.runLater(() -> gameStartCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

//    private KillCoolDownCounter killCoolDownCounter = new KillCoolDownCounter();

    private final Counter killCoolDownCounter = new Counter(500, 350, 50);

    public void handleKillCoolDownTimer(KillCoolDownTimer packet) {
        Platform.runLater(() -> killCoolDownCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

    public void handleClearEntityReturn(ClearEntityReturn packet) {
        for (int tileID : packet.getRegistrationIDs()) {
            Entity entity = EntityRegistryClient.getEntity(tileID);
            EntityRegistryClient.removeEntity(tileID);
            ScreenManager.getScreen(GameScreen.class).getEntities().remove(entity);
            AppClient.currentGame.getEntites().remove(entity);
        }
    }

    public void handleScrollingEnableReturn(ScrollingEnableReturn packet) {
        AppClient.currentGame.getMyPlayer().setScrollingEnabled(packet.isScrollingEnabled());
    }

    public void handleAddNestedPane(AddNestedPane packet) {
        Platform.runLater(() -> NestedScreenHandler.createGameScreen(packet));
    }

    public void removeNestedScreen() {
        Platform.runLater(() -> ScreenManager.getScreen(GameScreen.class).removeNestedScreen());
    }

    public void handleAddSudokuPane(AddSudokuPane packet) {
        Platform.runLater(() -> SudokuHandler.addSudokuToScreen(packet));
    }

    public void handleVerifySudokuReturn(VerifySudokuReturn packet) {
        if (packet.isSudokuComplete()) removeNestedScreen();
    }

    public void handleAnimationDisplayReturn(AnimationDisplayReturn packet) {
        ImageView imageView = new ImageView(TextureManager.getTexture(packet.getTexture()));
        imageView.setX(packet.getPos().getX());
        imageView.setY(packet.getPos().getY());
        imageView.setFitWidth(packet.getWidth());
        imageView.setFitHeight(packet.getHeight());
        Platform.runLater(() -> ScreenManager.getCurrentScreen().addNode(imageView));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ScreenManager.getCurrentScreen().removeNode(imageView);
                });
            }
        }, packet.getDuration());

    }


    public void handleSound(Sound packet) {
        if (AppClient.currentGame.getRecordHandler().isOn()) {
            AppClient.currentGame.getRecordHandler().produceSound(packet.getBytes());
        }
    }

    public void handleCloseMicAndSpeaker() {
        AppClient.currentGame.getRecordHandler().setOn(false);
    }

    public void handleOpenMicAndSpeaker() {
        AppClient.currentGame.getRecordHandler().startRecording();
        AppClient.currentGame.getRecordHandler().setOn(true);
    }


    private VotingPaneHandler votingPaneHandler = new VotingPaneHandler();

    public void handleAddVotingPane(AddVotingPane packet) {
        Platform.runLater(() -> {
            killCoolDownCounter.setTimerOn(false);
            votingPaneHandler = new VotingPaneHandler();
            votingPaneHandler.addVotingPane(packet);
        });

    }

    public void handleRemoveVotingScreen(DisplayVoteResults packet) {
        votingPaneHandler.showVotes(packet.getPlayerVoteInfo());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                killCoolDownCounter.setTimerOn(true);
                removeNestedScreen();
            }
        }, 2000);
    }

    private final Counter votingTimerCounter = new Counter(250, 250, 50);

    public void handleVotingTimer(VotingTimer packet) {
        Platform.runLater(() -> votingTimerCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

    //add as a node
//    private Rectangle taskBarProgress = new Rectangle(0, 0);
    public void handleTaskBarUpdate(TaskBarUpdate packet) {
        Entity taskBar = EntityRegistryClient.getEntity(packet.getRegistrationID());
        TaskBarHandler.updateTaskBar(taskBar, packet.getNewWidth());


//        Entity taskBar = EntityRegistryClient.getEntity(packet.getRegistrationID());
//        double fillStartX = taskBar.getPos().getX() + 7;//add 10 to compensate for the frame of the bar
//        double fillEndX = taskBar.getPos().getX() + 7 + packet.getNewWidth();
//        double taskBarHeight = TextureManager.getTexture("task-bar").getHeight();
//        this.taskBarProgress = new Rectangle(fillEndX - fillStartX, taskBarHeight - 18);
//
////        Rectangle rectangle = new Rectangle(fillEndX - fillStartX, taskBarHeight - 18);
//        taskBarProgress.setFill(Color.GREEN);
//        taskBarProgress.setX(fillStartX);
//        taskBarProgress.setY(taskBar.getPos().getY() + 10);
//        if (taskBarProgress.getWidth() ==50) {
//            Platform.runLater(() -> ScreenManager.getCurrentScreen().addNode(taskBarProgress));
//        }
    }

    public void handleRoleNotify(RoleNotify packet) {
        String role = packet.isImpostor() ? "impostor" : "crew";
        Text text = new Text(200, 150, "ROLE: "+role);
        text.setFont(Font.font(50));
        Platform.runLater(()->ScreenManager.getCurrentScreen().addNode(text));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->ScreenManager.getCurrentScreen().removeNode(text));
            }
        }, 5000);
        System.out.println("ROLE: " + role);
    }

    public void handleCrewWin() {
        ScreenManager.activate(CrewWinScreen.class);
        AppClient.currentGame.setRunning(false);
        revertToMenu();
    }

    public void handleImpostorWin() {
        ScreenManager.activate(ImpostorWinScreen.class);
        AppClient.currentGame.setRunning(false);
        revertToMenu();
    }

    public void revertToMenu() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ScreenManager.activate(MenuScreen.class);
            }
        }, 3000);
    }

    public void handleGameEnd(GameEnd packet) {

    }

//    public void handleElectionReturn(ElectionReturn packet) {
//
//    }
}

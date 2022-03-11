package ConnectionClient;

import AlertBox.AlertBox;
import CounterHandler.CounterHandler;
import Packet.Animation.AnimationDisplayReturn;
import EntityClient.*;
import Game.Game;
import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.CountDown.RemoveCountDown;
import Packet.EntityState.*;
import Packet.GameEnd.GameEnd;
import Packet.GameStart.RoleNotify;
import Packet.GameStart.StartGameReturn;
import Packet.LeaderBoard.*;
import Packet.NestedPane.AddNestedPane;
import Packet.NestedPane.AddSudokuPane;
import Packet.NestedPane.AddVotingPane;
import Packet.NestedPane.DisplayVoteResults;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
//import Packet.Timer.CountDown;
import Packet.CountDown.CountDown;
import Packet.Voting.ChatMessageReturn;
import Position.Pos;
import Screen.*;
import StartUp.AppClient;
import SudokuHandler.SudokuHandler;
import Packet.SudokuPacket.SudokuFailedReturn;
import Packet.SudokuPacket.VerifySudokuReturn;
import TaskBar.TaskBarHandler;
import javafx.application.Platform;
import SudokuHandler.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PacketControllerClient {


    public void handleRegistrationConfirmation(RegistrationConfirmation packet) {
        if (packet.isAuthorized()) {
            Platform.runLater(() -> ScreenManager.activate(MenuScreen.class));
        } else {
            Platform.runLater(() -> AlertBox.display(packet.failMessage));
        }
    }

    public void handleStartGameReturn(StartGameReturn packet) {
        if (packet.isAuthorizedToStartGame()) {
            AppClient.currentGame = new Game();
        } else {
            throw new IllegalStateException("client not registered to start game");
        }
    }

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

//    private final Counter gameStartCounter = new Counter(300, 200, 50);
//
//    public void handleGameStartTimerReturn(GameStartTimer packet) {
//        Platform.runLater(() -> gameStartCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
//    }
//
//
//    private final Counter killCoolDownCounter = new Counter(500, 350, 50);
//
//    public void handleKillCoolDownTimer(KillCoolDownTimer packet) {
//        Platform.runLater(() -> killCoolDownCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
//    }

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
        Platform.runLater(() -> {
            if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
                ScreenManager.getScreen(GameScreen.class).removeNestedScreen();
            }
        });
    }

    private SudokuHandler sudokuHandler;

    public void handleAddSudokuPane(AddSudokuPane packet) {
        this.sudokuHandler = new SudokuHandler();
        Platform.runLater(() -> sudokuHandler.addSudokuToScreen(packet));
    }

    public void handleVerifySudokuReturn(VerifySudokuReturn packet) {
        if (packet.isSudokuComplete()) removeNestedScreen();//todo maybe no need to check if sudoku is complete
    }

    public void handleAnimationDisplayReturn(AnimationDisplayReturn packet) {//todo delete
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


//    public void handleSound(Sound packet) {
//        AppClient.currentGame.getRecordHandler().produceSound(packet.getBytes());
//    }
//
//    public void handleCloseMicAndSpeaker() {
//        AppClient.currentGame.getRecordHandler().setOn(false);
//    }
//
//    public void handleOpenMicAndSpeaker() {
//        AppClient.currentGame.getRecordHandler().startRecording();
//        AppClient.currentGame.getRecordHandler().setOn(true);
//    }


    private VotingPaneHandler votingPaneHandler = new VotingPaneHandler();

    public void handleAddVotingPane(AddVotingPane packet) {
        Platform.runLater(() -> {
//            killCoolDownCounter.setTimerOn(false);
            votingPaneHandler = new VotingPaneHandler();
            votingPaneHandler.createVotingPane(packet);
        });

    }

    public void handleRemoveVotingScreen(DisplayVoteResults packet) {
        votingPaneHandler.showVotes(packet.getPlayerVoteInfo());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                killCoolDownCounter.setTimerOn(true);//un-pause timer
                if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
                    removeNestedScreen();
                }
            }
        }, 2000);
    }

    public void handleTimer(CountDown packet) {
        CounterHandler.addCountDown(packet);
//        final AtomicInteger atomicInteger = new AtomicInteger(packet.countDownValue);
//        final Text text = new Text();
//        text.setText(String.valueOf(atomicInteger.get()));
//        text.setLayoutX(packet.x);
//        text.setLayoutY(packet.y);
//        text.setFont(Font.font(packet.size));
//        GameScreen gameScreen = ScreenManager.getScreen(GameScreen.class);
//        Platform.runLater(() -> {
//            if (gameScreen.getNestedScreen() != null) {//at the time this is called, this is null
//                gameScreen.getNestedScreen().addNode(text);
//            } else {
//                gameScreen.addNode(text);
//            }
//        });
//
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> text.setText(String.valueOf(atomicInteger.decrementAndGet())));
//                if (atomicInteger.get() == 0) {
//                    Platform.runLater(() -> gameScreen.removeNode(text));
//                    cancel();
//                }
//            }
//        }, 1000, 1000);

    }


//    private final Counter votingTimerCounter = new Counter(ScreenManager.STAGE_WIDTH - 80, ScreenManager.STAGE_HEIGHT - 80, 50);
//
//    public void handleVotingTimer(VotingTimer packet) {
//        Platform.runLater(() -> votingTimerCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
//    }

    public void handleTaskBarUpdate(TaskBarUpdate packet) {
        Entity taskBar = EntityRegistryClient.getEntity(packet.getRegistrationID());
        TaskBarHandler.updateTaskBar(taskBar, packet.getNewWidth());
    }

    public void handleRoleNotify(RoleNotify packet) {
        String role = packet.isImpostor() ? "impostor" : "crew";
        Text text = new Text(200, 150, "ROLE: " + role);
        text.setFont(Font.font(50));
        Platform.runLater(() -> ScreenManager.getScreen(GameScreen.class).addNode(text));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> ScreenManager.getScreen(GameScreen.class).removeNode(text));
            }
        }, 5000);
    }

    public void handleCrewWin() {
//        CounterHandler.stopAllCountDowns();
        if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
            removeNestedScreen();
        }
        ScreenManager.activate(CrewWinScreen.class);
        AppClient.currentGame.setRunning(false);
        revertToMenu();
    }

    public void handleImpostorWin() {
//        CounterHandler.stopAllCountDowns();

        if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
            removeNestedScreen();
        }
        ScreenManager.activate(ImpostorWinScreen.class);
        AppClient.currentGame.setRunning(false);
        revertToMenu();
    }

    public void revertToMenu() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> ScreenManager.activate(MenuScreen.class));
            }
        }, 3000);
    }

    public void handleSudokuFailedReturn(SudokuFailedReturn packet) {
        sudokuHandler.unhighlightAllErrors();
        for (Pos pos : packet.errorsFound) {
            sudokuHandler.highlightError(pos);
        }
    }

    public void handleChatMessageReturn(ChatMessageReturn packet) {
        votingPaneHandler.handleChat(packet);
    }

    public void handleLeaderBoardReturn(LeaderBoardReturn packet) {
        ScreenManager.activate(LeaderBoardScreen.class);
        VBox content = (VBox) ScreenManager.getScreen(LeaderBoardScreen.class).getNode("content");
        Platform.runLater(() -> content.getChildren().clear());
        int counter = 1;
        for (Map.Entry<String, Double> scoreEntry : packet.userTimeMap.entrySet()) {
            String username = scoreEntry.getKey();
            if (username == null) username = "";
            double score = scoreEntry.getValue();
            Text text = (new Text(counter + ") " + username + " -> " + score));
            text.setFont(Font.font(15));
            text.setStroke(Color.LIGHTGREEN);
            Platform.runLater(() -> content.getChildren().add(text));
            counter++;
        }
    }

    public void handleRemoveCountDown(RemoveCountDown packet) {
        CounterHandler.stopCountDown(packet.id);
    }

//    public void handleElectionReturn(ElectionReturn packet) {
//
//    }
}

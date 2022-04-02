package ConnectionClient;

import AlertBox.AlertBox;
import CounterHandler.CounterHandler;
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
            //user valid, proceed to menu
        } else {
            Platform.runLater(() -> AlertBox.display(packet.failMessage));
            //display rejection alert box
        }
    }

    public void handleStartGameReturn(StartGameReturn packet) {
        //handles packet game start packet from server, notifying this client to create a game object
        if (packet.isAuthorizedToStartGame()) {
            AppClient.currentGame = new Game();
        } else {
            AlertBox.display("game already in progress");
//            throw new IllegalStateException("client not registered to start game");
        }
    }

    public void handleAddEntityReturn(AddEntityReturn packet) {
        //adds new entity object into the game, making them visible on the screen
        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
            if (newEntityState instanceof NewAnimatedEntityState) {
                Entity entity = new Entity((NewAnimatedEntityState) newEntityState);
                entity.setScrollable(((NewAnimatedEntityState) newEntityState).isScrollable());
                ScreenManager.getScreen(GameScreen.class).getEntities().add(entity);
            }

        }
    }

    public void handleAddLineReturn(AddLineReturn packet) {
        //add a line onto the screen
        for (NewLineState newLineState : packet.getNewEntityStates()) {
            Platform.runLater(() -> {
                Line line = new Line(newLineState.getStartPos().getX(), newLineState.getStartPos().getY(), newLineState.getFinalPos().getX(), newLineState.getFinalPos().getY());
                ScreenManager.getCurrentScreen().getPane().getChildren().add(line);
            });
        }
    }

    public void handleAddLocalEntityReturn(AddLocalEntityReturn packet) {
        //initialised local player, so each client knows which entity is local.
        NewAnimatedEntityState entityState = packet.getNewEntityState();
        AppClient.currentGame.handleLocalPlayer(new LocalPlayer(entityState));
    }


    public void handleStateReturn(StateReturn packet) {
        //change player position an animation state based on server response.
        Entity.calculateTimeDiffBetweenPackets();
        for (ExistingEntityState existingEntityState : packet.getEntityStates()) {
            Entity entity = EntityRegistryClient.getEntity(existingEntityState.getRegistrationID());
            if (entity == null) continue;
            entity.changeAttributes(existingEntityState.getAnimState(), existingEntityState.getAnimationIndex(), existingEntityState.getPos());
        }
    }

    public void handleClearEntityReturn(ClearEntityReturn packet) {
        //remove all entities on the screen
        for (int tileID : packet.getRegistrationIDs()) {
            Entity entity = EntityRegistryClient.getEntity(tileID);
            EntityRegistryClient.removeEntity(tileID);
            ScreenManager.getScreen(GameScreen.class).getEntities().remove(entity);
            AppClient.currentGame.getEntites().remove(entity);
        }
    }

    public void handleScrollingEnableReturn(ScrollingEnableReturn packet) {
        //set boolean to determine if entities position on screen is dependant on player offset.
        AppClient.currentGame.getMyPlayer().setScrollingEnabled(packet.isScrollingEnabled());
    }

    public void handleAddNestedPane(AddNestedPane packet) {
        //create a pane within current screen
        Platform.runLater(() -> NestedScreenHandler.createGameScreen(packet));
    }

    public void removeNestedScreen() {
        //remove nested screen of the screen
        Platform.runLater(() -> {
            if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
                ScreenManager.getScreen(GameScreen.class).removeNestedScreen();
            }
        });
    }

    private SudokuHandler sudokuHandler;

    public void handleAddSudokuPane(AddSudokuPane packet) {
        //display sudoku on screen
        this.sudokuHandler = new SudokuHandler();
        Platform.runLater(() -> sudokuHandler.addSudokuToScreen(packet));
    }

    public void handleVerifySudokuReturn(VerifySudokuReturn packet) {
        if (packet.isSudokuComplete()) removeNestedScreen();
    }

//    public void handleAnimationDisplayReturn(AnimationDisplayReturn packet) {
//        ImageView imageView = new ImageView(TextureManager.getTexture(packet.getTexture()));
//        imageView.setX(packet.getPos().getX());
//        imageView.setY(packet.getPos().getY());
//        imageView.setFitWidth(packet.getWidth());
//        imageView.setFitHeight(packet.getHeight());
//        Platform.runLater(() -> ScreenManager.getCurrentScreen().addNode(imageView));
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    ScreenManager.getCurrentScreen().removeNode(imageView);
//                });
//            }
//        }, packet.getDuration());
//
//    }


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
        //adds voting pane as a nested screen
        Platform.runLater(() -> {
            votingPaneHandler = new VotingPaneHandler();
            votingPaneHandler.createVotingPane(packet);
        });

    }

    public void handleRemoveVotingScreen(DisplayVoteResults packet) {
        //remove voting pane of the screen
        votingPaneHandler.showVotes(packet.getPlayerVoteInfo());//display who voted for who as an animation
        new Timer().schedule(new TimerTask() {//set timer so each player can look at the scores before the pane is removed
            @Override
            public void run() {
                if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {//check it nested screen hasn't been removed elsewhere in code
                    removeNestedScreen();//remove voting pane
                }
            }
        }, 2000);
    }

    public void handleTimer(CountDown packet) {//add count down timer to screen
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


    public void handleTaskBarUpdate(TaskBarUpdate packet) {
        //update progress bar at the top of screen displaying showing portion of tasks left to do
        Entity taskBar = EntityRegistryClient.getEntity(packet.getRegistrationID());
        TaskBarHandler.updateTaskBar(taskBar, packet.getNewWidth());
    }

    public void handleRoleNotify(RoleNotify packet) {//displays role of player (impostor or crewmate) on screen
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
        //process crew mates winning the game
        if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
            removeNestedScreen();//remove nested screen if there is one.
        }
        ScreenManager.activate(CrewWinScreen.class);//show crew mate win screen
        AppClient.currentGame.setRunning(false);//stop game loop
        revertToMenu();//display menu after timer
    }

    public void handleImpostorWin() {
        //process impostor winning the game
        if (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null) {
            removeNestedScreen();//remove nested screen if there is one.
        }
        ScreenManager.activate(ImpostorWinScreen.class);//show impostor win screen
        AppClient.currentGame.setRunning(false);//stop game loop
        revertToMenu();//display menu after timer
    }

    public void revertToMenu() {//wait for 3 second and activate menu screen
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> ScreenManager.activate(MenuScreen.class));
            }
        }, 3000);
    }

    public void handleSudokuFailedReturn(SudokuFailedReturn packet) {
        //highlight in red each cell that is input by user that opposes one of the constraints of the sudoku puzzle
        sudokuHandler.unhighlightAllErrors();
        for (Pos pos : packet.errorsFound) {
            sudokuHandler.highlightError(pos);
        }
    }

    public void handleChatMessageReturn(ChatMessageReturn packet) {
        //display chat and user name of the player who input the message
        votingPaneHandler.handleChat(packet);
    }

    public void handleLeaderBoardReturn(LeaderBoardReturn packet) {
        //display leaderboard, with player server returns
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
        //remove count down from screen
        CounterHandler.stopCountDown(packet.id);
    }

}

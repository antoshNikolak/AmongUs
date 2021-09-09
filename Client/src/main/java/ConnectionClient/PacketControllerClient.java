package ConnectionClient;

import EntityClient.*;
import Game.Game;
import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.EntityState.*;
import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Screen.GameScreen;
import Screen.MenuScreen;
import Screen.ScreenManager;
import StartUp.AppClient;
import javafx.application.Platform;
import ScreenCounter.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class PacketControllerClient {

    public void handleRegistrationConfirmation(RegistrationConfirmation packet) {
        if (packet.isAuthorized()) {
            ScreenManager.activate(MenuScreen.class);
        } else {
            System.out.println("client not authorized");
        }
    }

    public void handleStartGameReturn(StartGameReturn packet) {//handle new stationary entity came first once, why?
        if (packet.isAuthorizedToStartGame()) {
            AppClient.currentGame = new Game();
        } else {
            throw new IllegalStateException("client not registered to start game");
        }
    }

    public void handleNewStationaryEntityReturn(AddStationaryEntityReturn packet) {
        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
            if (newEntityState instanceof NewAnimatedEntityState) {
                new Entity((NewAnimatedEntityState) newEntityState);
            }
//            else if(newEntityState instanceof NewLineState){
//                new Entity((NewLineState) newEntityState);
//            }
        }//todo re use code, make stationary class and changing class same thing
    }

    public void handleAddChangingEntityReturn(AddChangingEntityReturn packet) {
        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
            if (newEntityState instanceof NewAnimatedEntityState) {
                new ChangingEntity((NewAnimatedEntityState) newEntityState);
            }
//            else if(newEntityState instanceof NewLineState){
//                new Entity((NewLineState) newEntityState);
//            }
        }
    }

    public void handleAddLineReturn(AddLineReturn packet) {
        for (NewLineState newLineState : packet.getNewEntityStates()) {
            System.out.println("creating a line");
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
        ChangingEntity.calculateTimeDiffBetweenPackets();
        for (ExistingEntityState existingEntityState : packet.getEntityStates()) {
            ((ChangingEntity) EntityRegistryClient.getEntity(existingEntityState.getRegistrationID())).
                    changeAttributes(existingEntityState.getAnimState(), existingEntityState.getAnimationIndex(), existingEntityState.getPos());
        }
    }

    private GameStartCounter gameStartCounter = new GameStartCounter();

    public void handleGameStartTimerReturn(GameStartTimer packet) {
        Platform.runLater(() -> gameStartCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

    private KillCoolDownCounter killCoolDownCounter = new KillCoolDownCounter();

    public void handleKillCoolDownTimer(KillCoolDownTimer packet) {
        Platform.runLater(() -> killCoolDownCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

    public void handleClearEntityReturn(ClearEntityReturn packet) {
        for (int tileID : packet.getRegistrationIDs()) {
            Entity entity = EntityRegistryClient.getEntity(tileID);
            EntityRegistryClient.removeEntity(tileID);
            ScreenManager.getScreen(GameScreen.class).getEntities().remove(entity);
            if (entity instanceof ChangingEntity) AppClient.currentGame.getChangingEntities().remove(entity);
        }
    }

    public void handleScrollingEnableReturn(ScrollingEnableReturn packet) {
        AppClient.currentGame.getMyPlayer().setScrollingEnabled(packet.isScrollingEnabled());
    }

    public void handleAddNestedPane(AddNestedPane packet) {
        Pane pane = new Pane();
        int paneX = packet.getPaneX();
        int paneY = packet.getPaneY();
        int paneWidth = packet.getPaneWidth();
        int paneHeight = packet.getPaneHeight();

        pane.setLayoutX(paneX);
        pane.setLayoutY(paneY);
        pane.setPrefWidth(paneWidth);
        pane.setPrefHeight(paneHeight);

        GameScreen gameScreen = new GameScreen(pane);
        gameScreen.setClearBoundaries(paneX, paneY, paneX+ paneWidth, paneY + paneWidth);
        System.out.println("adding nested screen");

        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
            if (newEntityState instanceof NewAnimatedEntityState) {
                gameScreen.getEntities().add(new Entity((NewAnimatedEntityState) newEntityState));
            } else if (newEntityState instanceof NewLineState) {
                NewLineState lineState = (NewLineState) newEntityState;
                Line line = new Line(lineState.getStartPos().getX(), lineState.getStartPos().getY(), lineState.getFinalPos().getX(), lineState.getFinalPos().getY());
                line.setStrokeWidth(lineState.getWidth());
                gameScreen.getPane().getChildren().add(line);
            }
        }

        Platform.runLater(() -> ScreenManager.getScreen(GameScreen.class).setNestedScreen(gameScreen));

//        ScreenManager.getCurrentScreen().addNode(new Pane();

    }
}

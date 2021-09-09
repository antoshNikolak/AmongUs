package ConnectionClient;

import EntityClient.*;
import Game.Game;
import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.AddEntityReturn.AddLineReturn;
import Packet.AddEntityReturn.AddLocalEntityReturn;
import Packet.AddEntityReturn.AddStationaryEntityReturn;
import Packet.Camera.ScrollingEnableReturn;
import Packet.EntityState.*;
import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Screen.MenuScreen;
import Screen.ScreenManager;
import StartUp.AppClient;
import javafx.application.Platform;
import ScreenCounter.*;
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

    public void handleAddLineReturn(AddLineReturn packet){
        for (NewLineState newLineState: packet.getNewEntityStates()){
            System.out.println("creating a line");
            Platform.runLater(()->{
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
            AppClient.currentGame.getEntities().remove(entity);
            if (entity instanceof ChangingEntity) AppClient.currentGame.getChangingEntities().remove(entity);
        }
    }

    public void handleScrollingEnableReturn(ScrollingEnableReturn packet) {
        AppClient.currentGame.getMyPlayer().setScrollingEnabled(packet.isScrollingEnabled());
    }

    public void handleAddNestedPane() {


    }
}

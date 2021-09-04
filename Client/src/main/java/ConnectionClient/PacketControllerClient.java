package ConnectionClient;

import EntityClient.*;
import Game.Game;
import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Screen.MenuScreen;
import ScreenCounter.ScreenCounter;
import Screen.ScreenManager;
import StartUp.AppClient;
import javafx.application.Platform;
import ScreenCounter.*;

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
        for (NewEntityState entityState : packet.getNewEntityStates()) {
            new Entity(entityState);;
        }
    }

    public void handleAddChangingEntityReturn(AddChangingEntityReturn packet) {
        for (NewEntityState newEntityState : packet.getNewEntityStates()) {
            new ChangingEntity(newEntityState);
        }
    }

    public void handleAddLocalEntityReturn(AddLocalEntityReturn packet) {
        NewEntityState entityState = packet.getNewEntityState();
        AppClient.currentGame.handleLocalPlayer(new LocalPlayer(entityState));
    }


    public void handleStateReturn(StateReturn packet) {
        ChangingEntity.calculateTimeDiffBetweenPackets();
        for (EntityState entityState : packet.getEntityStates()) {
            ((ChangingEntity)EntityRegistryClient.getEntity(entityState.getRegistrationID())).
                    changeAttributes(entityState.getAnimState(), entityState.getAnimationIndex(), entityState.getPos());
        }
    }

    private GameStartCounter gameStartCounter = new GameStartCounter();
    public void handleGameStartTimerReturn(GameStartTimer packet) {
        Platform.runLater(()->gameStartCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

    private KillCoolDownCounter killCoolDownCounter = new KillCoolDownCounter();
    public void handleKillCoolDownTimer(KillCoolDownTimer packet) {
        Platform.runLater(()->killCoolDownCounter.updateCounterValue(String.valueOf(packet.getCountDownValue())));
    }

    public void handleClearEntityReturn(ClearEntityReturn packet) {
        for (int tileID: packet.getRegistrationIDs()){
            Entity entity = EntityRegistryClient.getEntity(tileID);
            EntityRegistryClient.removeEntity(tileID);
            AppClient.currentGame.getEntities().remove(entity);
            if (entity instanceof ChangingEntity)AppClient.currentGame.getChangingEntities().remove(entity);
        }


    }

}

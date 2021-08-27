package ConnectionClient;

import EntityClient.*;
import Game.Game;
import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Screen.MenuScreen;
import Screen.ScreenManager;
import StartUp.AppClient;

public class PacketControllerClient {

    public void handleRegistrationConfirmation(RegistrationConfirmation packet) {
        if (packet.isAuthorized()) {
            ScreenManager.activate(MenuScreen.class);
        } else {
            System.out.println("client not authorized");
        }
    }

    public void handleStartGameReturn(StartGameReturn packet) {
        if (packet.isAuthorizedToStartGame()) {
            AppClient.currentGame = new Game();
        } else {
            throw new IllegalStateException("client not registered to start game");
        }
    }

    public void handleNewStationaryEntityReturn(AddStationaryEntityReturn packet) {
        for (NewEntityState entityState : packet.getNewEntityStates()) {
            AppClient.currentGame.getEntities().add(new Entity(entityState));
        }
    }

    public void handleAddChangingEntityReturn(AddChangingEntityReturn packet) {
        for (NewEntityState newEntityState: packet.getNewEntityStates()){
            ChangingEntity changingEntity = new ChangingEntity(newEntityState);
            AppClient.currentGame.getEntities().add(changingEntity);
        }
    }

    public void handleAddLocalEntityReturn(AddLocalEntityReturn packet) {
        NewEntityState entityState = packet.getNewEntityState();
        LocalPlayer localPlayer = new LocalPlayer(entityState);
        AppClient.currentGame.handleLocalPlayer(localPlayer);
    }


    public void handleStateReturn(StateReturn packet) {
        ChangingEntity.calculateTimeDiffBetweenPackets();
        for (EntityState entityState : packet.getEntityStates()){
            EntityRegistryClient.getChangingEntity(entityState.getRegistrationID()).
                    changeAttributes(entityState.getAnimState(), entityState.getAnimationIndex(), entityState.getPos());
        }
    }

}

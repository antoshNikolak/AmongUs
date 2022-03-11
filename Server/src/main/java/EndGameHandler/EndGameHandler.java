package EndGameHandler;

import Component.ImpostorComp;
import ConnectionServer.ConnectionServer;
import DataBase.DataBaseUtil;
import Entity.Entity;
import Entity.Player;
import Packet.CountDown.CountDown;
import Packet.CountDown.RemoveCountDown;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.ImpostorWin;
import Registry.CountDownRegistryServer;
import Registry.Registry;
import Registry.RegistryHandler;
import StartUpServer.AppServer;
import State.GameState;
import System.*;

import java.util.List;

public class EndGameHandler {

    public void checkImpostorWin() {
        int numOfPlayerAlive = getPlayersAlive();//players that arent ghosts
        if (numOfPlayerAlive <= 2) {
            new Thread(() -> {
                stopAllTimers();//interrupts all client side count downs
                recordImpostorWin();//record win in data base
                ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());//broadcast impostor win
                AppServer.currentGame.stopGame();//bring players back to menu, close game
            }).start();
        }
    }

    private int getPlayersAlive() {
        return AppServer.currentGame.getStateManager().getState(GameState.class).getSystem(ImposterActionsSystem.class).
                getAlivePlayers().size();
    }

    public void recordImpostorWin() {
        double time = AppServer.currentGame.getStateManager().getState(GameState.class).stopGameDurationTimer();
        String userName = getImpostorUserName();
        DataBaseUtil.updateImpostorWinTime(userName, time / 1000);
    }

    private String getImpostorUserName() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            if (player.hasComponent(ImpostorComp.class)) {
                return player.getNameTag();
            }
        }
        return null;
    }

    public void handleCrewWin() {
        new Thread(() -> {
            stopAllTimers();//interrupts all client side count downs
            ConnectionServer.sendTCPToAllPlayers(new CrewWin());//broadcast crew win
            AppServer.currentGame.stopGame();//bring players back to menu, close game
        }).start();
    }

    private void stopAllTimers() {
        CountDownRegistryServer registry = RegistryHandler.countDownRegistryServer;
        for (CountDown countDown : registry.getCountDowns()) {
            registry.stopCountDown(countDown);
            registry.removeEntity(countDown);
        }
//        registry.getCountDowns().forEach(registry::stopCountDown);

    }
}

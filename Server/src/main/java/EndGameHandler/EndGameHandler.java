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

public class EndGameHandler{

    public  void checkImpostorWin() {                                              //todo remember to stop all client server timers
        int numOfPlayerAlive = getPlayersAlive();
        if (numOfPlayerAlive <= 2) {//todo change this to <=
            new Thread(()->{
                stopAllTimers();
                recordImpostorWin();
                ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
                AppServer.currentGame.stopGame();
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

    private  String getImpostorUserName() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            if (player.hasComponent(ImpostorComp.class)) {
                return player.getNameTag();
            }
        }
        return null;
    }

    public void handleCrewWin() {
        new Thread(()->{
            stopAllTimers();
            ConnectionServer.sendTCPToAllPlayers(new CrewWin());
            AppServer.currentGame.stopGame();
        }).start();
    }

    private void stopAllTimers(){
        CountDownRegistryServer registry = RegistryHandler.countDownRegistryServer;
        for (CountDown countDown : registry.getCountDowns()){
            registry.stopCountDown(countDown);
            registry.removeEntity(countDown);
        }
//        registry.getCountDowns().forEach(registry::stopCountDown);

    }
}

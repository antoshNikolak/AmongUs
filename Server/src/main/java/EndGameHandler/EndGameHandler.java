package EndGameHandler;

import Component.ImpostorComp;
import ConnectionServer.ConnectionServer;
import DataBase.DataBaseUtil;
import Entity.Player;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.ImpostorWin;
import StartUpServer.AppServer;
import State.GameState;
import System.*;

public class EndGameHandler{

    public  void checkImpostorWin() {                                              //todo remember to stop all client server timers
        int numOfPlayerAlive = getPlayersAlive();
        if (numOfPlayerAlive == 2) {
            new Thread(()->{
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
            ConnectionServer.sendTCPToAllPlayers(new CrewWin());
            AppServer.currentGame.stopGame();
        }).start();
    }

//    public enum Winner{
//        CREW_WIN{
//            @Override
//            public void handleWin() {
//                ConnectionServer.sendTCPToAllPlayers(new CrewWin());
//                AppServer.currentGame.stopGame();
//            }
//        },
//        IMPOSTOR_WIN{
//            @Override
//            public void handleWin() {
//                recordImpostorWin();
//                ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
//                AppServer.currentGame.stopGame();
//            }
//        };
//
//        public abstract void handleWin();
//    }

}

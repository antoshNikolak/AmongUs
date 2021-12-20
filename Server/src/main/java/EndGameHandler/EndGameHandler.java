package EndGameHandler;

import ConnectionServer.ConnectionServer;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.ImpostorWin;
import StartUpServer.AppServer;
import State.GameState;
import System.*;

public class EndGameHandler {
    //todo ideas
    //server sends exact frames to each client every time period
    //client stores position of all of these

//    public static void checkImpostorWin(int alivePlayers) {
//        if (alivePlayers == 2) {
//            recordImpostorWin();
//            ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
//            AppServer.currentGame.stopGame();
//        }
//    }

    public static void checkImpostorWin() {//todo remember to stop all client server timers
        int numOfPlayerAlive = getPlayersAlive();
        if (numOfPlayerAlive == 2) {
            recordImpostorWin();
            ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
            AppServer.currentGame.stopGame();
        }
    }

    private static int getPlayersAlive(){
        return AppServer.currentGame.getStateManager().getState(GameState.class).getSystem(ImposterActionsSystem.class).
                getAlivePlayers().size();
    }

    private static void recordImpostorWin(){

    }

    public static void handleCrewWin(){
        ConnectionServer.sendTCPToAllPlayers(new CrewWin());
        AppServer.currentGame.stopGame();
    }

}

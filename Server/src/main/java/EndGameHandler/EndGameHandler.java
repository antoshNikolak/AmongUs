package EndGameHandler;

import ConnectionServer.ConnectionServer;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.ImpostorWin;
import StartUpServer.AppServer;

public class EndGameHandler {
    //todo ideas
    //server sends exact frames to each client every time period
    //client stores position of all of these

    public static void checkImpostorWin(int alivePlayers) {
        if (alivePlayers == 2) {
            recordImpostorWin();
            ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
            AppServer.currentGame.stopGame();
        }
    }

    private static void recordImpostorWin(){

    }

    public static void handleCrewWin(){
        ConnectionServer.sendTCPToAllPlayers(new CrewWin());
        AppServer.currentGame.stopGame();
    }
}

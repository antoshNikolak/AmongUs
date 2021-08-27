package Packet.GameStart;

import Packet.Packet;

public class StartGameReturn implements Packet {
    boolean authorizedToStartGame;

    public StartGameReturn(boolean authorizedToStartGame) {
        this.authorizedToStartGame = authorizedToStartGame;
    }

    public StartGameReturn() {
    }

    public boolean isAuthorizedToStartGame() {
        return authorizedToStartGame;
    }
}

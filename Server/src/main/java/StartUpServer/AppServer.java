package StartUpServer;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Game.Game;

import java.util.ArrayList;
import java.util.List;

public class AppServer {

    public static Game currentGame;
    private static List<Client> clients = new ArrayList<>();

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        ConnectionServer.start();
    }


    public static List<Client> getClients() {
        return clients;
    }

    public static void setClients(List<Client> clients) {
        AppServer.clients = clients;
    }
}

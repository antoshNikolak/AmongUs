package Game;

import Camera.Camera;
import EntityClient.Entity;
//import EntityClient.Entity;
import EntityClient.LocalPlayer;
import StartUp.AppClient;
import javafx.animation.AnimationTimer;
import Screen.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    private final List<Entity> changingEntities = new CopyOnWriteArrayList<>();
    private LocalPlayer myPlayer;


    public Game() {
        ScreenManager.getScreen(GameScreen.class).setCamera(new Camera());
    }

    private void init() {
        ScreenManager.activate(GameScreen.class);
    }

    public void start() {
        init();
        startTimer();
    }


    public void startTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                myPlayer.sendInput();
                interpolate(ScreenManager.getScreen(GameScreen.class));
                ScreenManager.getScreen(GameScreen.class).render();
            }
        }.start();
    }


    private void interpolate(GameScreen gameScreen) {//todo doc recursion
        for (Entity entity : gameScreen.getEntities()) {
            entity.interpolate();
        }
        if (gameScreen.getNestedScreen() != null){
            interpolate(gameScreen.getNestedScreen());
        }

//        do {
//            for (Entity entity : ScreenManager.getScreen(GameScreen.class).getEntities()) {
//                entity.interpolate();
//            }
//        } while (ScreenManager.getScreen(GameScreen.class).getNestedScreen() != null);


        //todo remember to include innder pane interp


//        for (Entity entity : changingEntities){
//            entity.interpolate();
//        }

    }

    public void handleLocalPlayer(LocalPlayer localPlayer) {
        ScreenManager.getScreen(GameScreen.class).getEntities().add(localPlayer);
        this.myPlayer = localPlayer;
        start();
    }

    public LocalPlayer getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(LocalPlayer myPlayer) {
        this.myPlayer = myPlayer;
    }

    public List<Entity> getChangingEntities() {
        return changingEntities;
    }


}

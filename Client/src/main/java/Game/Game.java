package Game;

import Camera.Camera;
import EntityClient.Entity;
//import EntityClient.Entity;
import EntityClient.LocalPlayer;
import Node.SpaceButton;
import StartUp.AppClient;
import TaskBar.TaskBarHandler;
//import VoiceChat.RecordHandler;
import javafx.animation.AnimationTimer;
import Screen.*;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Skinnable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    private final List<Entity> changingEntities = new CopyOnWriteArrayList<>();
    private LocalPlayer myPlayer;
//    private RecordHandler recordHandler = new RecordHandler();
    private AnimationTimer animationTimer;
    private boolean running = true;


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

    public void setRunning(boolean running) {
        this.running = running;
        if (!running) {
            stopGame();
        }
    }

    public void startTimer() {
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    myPlayer.sendInput();
                    interpolate(ScreenManager.getScreen(GameScreen.class));
                    ScreenManager.getScreen(GameScreen.class).render();
                } else {
                    this.stop();
                }

            }
        };
        this.animationTimer.start();
    }

    private void stopGame() {
        ScreenManager.getScreen(GameScreen.class).getEntities().clear();
        TaskBarHandler.getTaskBarProgress().setWidth(0);
        this.changingEntities.clear();
        myPlayer = null;
    }


    private void interpolate(GameScreen gameScreen) {//todo doc recursion
        for (Entity entity : gameScreen.getEntities()) {
            entity.interpolate();
        }
        if (gameScreen.getNestedScreen() != null) {
            interpolate(gameScreen.getNestedScreen());
        }
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

    public List<Entity> getEntites() {
        return changingEntities;
    }

//    public RecordHandler getRecordHandler() {
//        return recordHandler;
//    }
//
//    public void setRecordHandler(RecordHandler recordHandler) {
//        this.recordHandler = recordHandler;
//    }
}

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
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {//game loop, runs at 60 fps
                if (running) {
                    myPlayer.sendInput();
                    interpolate(ScreenManager.getScreen(GameScreen.class));
                    ScreenManager.getScreen(GameScreen.class).render();
                } else {
                    this.stop();
                }

            }
        };
        animationTimer.start();
    }

    private void stopGame() {
        ScreenManager.getScreen(GameScreen.class).getEntities().clear();//remove entities of screen
        TaskBarHandler.getTaskBarProgress().setWidth(0);//reset progress bar
        this.changingEntities.clear();//remove entity objects from the list, so they are garbage collected
        myPlayer = null;
    }

    private void interpolate(GameScreen gameScreen) {
        //interpolate between entity positions to show smooth transition
        for (Entity entity : gameScreen.getEntities()) {
            entity.interpolate();
        }
        if (gameScreen.getNestedScreen() != null) {
            interpolate(gameScreen.getNestedScreen());//perform interpolation on entities inside of nested pane
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

}

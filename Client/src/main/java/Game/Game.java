package Game;

import Camera.Camera;
import EntityClient.ChangingEntity;
import EntityClient.Entity;
import EntityClient.LocalPlayer;
import Screen.Screen;
import StartUp.AppClient;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import Screen.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
//    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final List<ChangingEntity> changingEntities = new CopyOnWriteArrayList<>();
    private LocalPlayer myPlayer;

    private final Camera camera = new Camera();

    public Game() {
    }

    private void init() {
        ScreenManager.activate(GameScreen.class);
    }

    public void start() {
        init();
        GraphicsContext gc = getGraphicsContext();
        startTimer(gc);
    }

    private GraphicsContext getGraphicsContext() {
//        Canvas canvas = (Canvas) ScreenManager.getNode(GameScreen.class, "canvas");
        Canvas canvas = (Canvas) ScreenManager.getScreen(GameScreen.class).getNode("canvas");

        return canvas.getGraphicsContext2D();
    }

    public void startTimer(GraphicsContext gc) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                myPlayer.sendInput();
                interpolate();
                render(gc);
            }
        }.start();
    }


    private void interpolate() {
        for (ChangingEntity changingEntity: changingEntities){
            changingEntity.interpolate();
        }
    }

    private void render(GraphicsContext gc) {
//        gc.clearRect(0, 0, 600, 400);
        ScreenManager.getScreen(GameScreen.class).render(gc);
//        for (Entity entity : entities) {
//            entity.render(gc);
//        }
    }

    public void handleLocalPlayer(LocalPlayer localPlayer) {
        this.myPlayer = localPlayer;
        start();
    }

//    public List<Entity> getEntities() {
//        return entities;
//    }

    public LocalPlayer getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(LocalPlayer myPlayer) {
        this.myPlayer = myPlayer;
    }

    public List<ChangingEntity> getChangingEntities() {
        return changingEntities;
    }

    public Camera getCamera() {
        return camera;
    }


}

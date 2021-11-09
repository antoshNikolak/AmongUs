package EntityClient;

import Animation.AnimState;
import AnimationClient.AnimationManager;
import Camera.Camera;
import Packet.EntityState.NewAnimatedEntityState;
import Position.Pos;
import Screen.TextureManager;
import StartUp.AppClient;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.LinkedList;

public class Entity {
    private final LinkedList<Pos> positionReturns = new LinkedList<>();
    private static double timeBetweenPackets = 0.0066;//default value
    private static double timeLastPacketArrived;
    protected Pos pos;
    protected AnimationManager animationManager;
    private boolean scrollable = true;

    public Entity(NewAnimatedEntityState newEntityState) {
        if (newEntityState.getRegistrationID() != -1) {
            EntityRegistryClient.addEntity(newEntityState.getRegistrationID(), this);
        }
        this.animationManager = new AnimationManager(newEntityState);
        this.pos = newEntityState.getPos();
        try {
            AppClient.currentGame.getEntites().add(this);
        }catch (NullPointerException e){
            System.out.println("problem adding Entity: "+ newEntityState.getCurrentState());
            System.out.println(AppClient.currentGame);
            System.out.println(AppClient.currentGame.getEntites());
            System.out.println(this);

        }
    }

    public void render(GraphicsContext gc, Camera camera) {
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
        gc.drawImage(image, pos.getX() - camera.getOffsetX(), pos.getY() - camera.getOffsetY());
    }

    public void render(GraphicsContext gc) {
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
//        System.out.println(animationManager.getCurrentFrame());
//
//        if (animationManager.getCurrentFrame().contains("task")) {
//            System.out.println(animationManager.getCurrentFrame());
//        }
//        if (image == null){
//            System.out.println("image is null");
//        }
        gc.drawImage(image, pos.getX(), pos.getY());
    }


    public void interpolate() {
        if (positionReturns.size() != 2) return;
        Pos oldPos = positionReturns.get(0);
        Pos newPos = positionReturns.get(1);
        if (oldPos == null){
            System.out.println("position returns contents:");
            positionReturns.forEach(pos -> {
                System.out.println(pos.getX());
                System.out.println(pos.getY());
            });
        }
//        System.out.println("old pos: "+oldPos.getX());
//        System.out.println("new pos: "+newPos.getX());
        double timeSinceLastPacket = getTimeSinceLastPacket();
        Pos currentPos = getCurrentPos(oldPos, newPos, timeSinceLastPacket, timeBetweenPackets);
        pos.setPos(currentPos);
//        System.out.println("current pos: "+currentPos.getX());
    }

    private Pos getCurrentPos(Pos oldPos, Pos newPos, double timeSinceLastPacket, double timeBetweenPackets) {
        Pos currentPos = new Pos(oldPos);
        if (timeSinceLastPacket < timeBetweenPackets) {
            currentPos = lerpPos(currentPos, newPos, timeSinceLastPacket / timeBetweenPackets);
        } else {
            currentPos.setPos(newPos);
        }
        return currentPos;
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private Pos lerpPos(Pos oldPos, Pos newPos, double time) {
        double x = lerp(oldPos.getX(), newPos.getX(), time);
        double y = lerp(oldPos.getY(), newPos.getY(), time);
        return new Pos(x, y);
    }

    private double getTimeSinceLastPacket() {
        return (System.nanoTime() - positionReturns.getLast().getTimeStamp()) / 10e9;
    }

    public void updatePositionReturns(Pos pos) {
        positionReturns.add(pos);
        if (positionReturns.size() > 2) {
            positionReturns.removeFirst();
        }
    }

    public void setCurrentPosition(Pos pos) {
        pos.setTimeStamp(System.nanoTime());
        updatePositionReturns(pos);
    }

    public void changeAttributes(AnimState animState, int index, Pos pos) {
        this.animationManager.setCurrentState(animState);
        this.animationManager.updateCurrentAnimationIndex(index);
        setCurrentPosition(pos);
    }

    public static void calculateTimeDiffBetweenPackets() {
        if (timeLastPacketArrived != 0) timeBetweenPackets = (System.nanoTime() - timeLastPacketArrived) / 10e9;
        timeLastPacketArrived = System.nanoTime();
    }

    public Pos getPos() {
        return pos;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}

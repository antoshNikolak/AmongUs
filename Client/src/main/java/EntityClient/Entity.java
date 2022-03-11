package EntityClient;

import Packet.Animation.AnimState;
import AnimationClient.AnimationManager;
import Camera.Camera;
import Packet.EntityState.NewAnimatedEntityState;
import Position.Pos;
import Screen.TextureManager;
import StartUp.AppClient;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Entity {
    private final LinkedList<Pos> positionReturns = new LinkedList<>();
    private static double timeBetweenPackets = 0.0066;//default value
    private static double timeLastPacketArrived;
    protected Pos pos;
    protected AnimationManager animationManager;
    private boolean scrollable = true;
    private final String nameTag;

    public Entity(NewAnimatedEntityState newEntityState) {
        if (newEntityState.getRegistrationID() != -1) {
            EntityRegistryClient.addEntity(newEntityState.getRegistrationID(), this);
        }
        this.animationManager = new AnimationManager(newEntityState);
        this.pos = newEntityState.getPos();
        this.nameTag = newEntityState.getNameTag();//null maybe>
        try {
            AppClient.currentGame.getEntites().add(this);
        } catch (NullPointerException e) {
            System.out.println("problem adding Entity: " + newEntityState.getCurrentState());
            System.out.println(AppClient.currentGame);
            System.out.println(AppClient.currentGame.getEntites());
            System.out.println(this);

        }
    }

    public void render(GraphicsContext gc, Camera camera) {
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
        gc.drawImage(image, pos.getX() - camera.getOffsetX(), pos.getY() - camera.getOffsetY());
        if (nameTag != null) {
            gc.strokeText(nameTag, pos.getX() - camera.getOffsetX(), pos.getY() - 10 - camera.getOffsetY());
        }
    }

    public void render(GraphicsContext gc) {
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
        gc.drawImage(image, pos.getX(), pos.getY());
        if (nameTag != null) {
            gc.strokeText(nameTag, pos.getX(), pos.getY() - 10);
        }

    }

    public void interpolate() {
        Pos oldPos;
        Pos newPos;
        synchronized (this) {
            if (positionReturns.size() != 2) return;
            oldPos = positionReturns.get(0);//position of player 2 server updates ago
            newPos = positionReturns.get(1);//position of player 1 server update ago (most recent)
        }
        double timeSinceLastPacket = getTimeSinceLastPacket();//time since most recent update was sent
        Pos currentPos = getCurrentPos(oldPos, newPos, timeSinceLastPacket, timeBetweenPackets);
        //get position between old pos and new pos using linear interpolation
        pos.setPos(currentPos);//updates player's pos so can be displayed
    }

    private Pos getCurrentPos(Pos oldPos, Pos newPos, double timeSinceLastPacket, double timeBetweenPackets) {
        Pos currentPos = new Pos(oldPos);
        if (timeSinceLastPacket < timeBetweenPackets) {//estimate if position has not expired
            currentPos = lerpPos(currentPos, newPos, timeSinceLastPacket / timeBetweenPackets);
            //use linear interpolation
        } else {//position has expired
            currentPos.setPos(newPos);//set current position to the new pos
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

    //todo document synchronization
    public void updatePositionReturns(Pos pos) {
        if (pos == null) return;//LATEST ADDITION
        synchronized (this) {
            positionReturns.add(pos);
            if (positionReturns.size() > 2) {
                positionReturns.removeFirst();
            }
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

package EntityClient;

import Animation.AnimState;
import Packet.Position.NewEntityState;
import Position.Pos;
import StartUp.AppClient;

import java.util.LinkedList;

public class ChangingEntity extends Entity {
    private final LinkedList<Pos> positionReturns = new LinkedList<>();
    private static double timeBetweenPackets = 0.0066;//default value
    private static double timeLastPacketArrived;

    public ChangingEntity(NewEntityState entityState) {
        super(entityState);
        AppClient.currentGame.getChangingEntities().add(this);
    }

    public void interpolate() {
        if (positionReturns.size() != 2) return;
        Pos oldPos = positionReturns.get(0);
        Pos newPos = positionReturns.get(1);
        double timeSinceLastPacket = getTimeSinceLastPacket();
        Pos currentPos = getCurrentPos(oldPos, newPos, timeSinceLastPacket, timeBetweenPackets);
        pos.setPos(currentPos);
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

}

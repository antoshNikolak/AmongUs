package System;

import Component.AliveComp;
import DistanceFinder.DistanceFinder;
import Entity.DeadBody;
import Entity.Player;
import Packet.Position.InputRequest;
import Registry.RegistryHandler;
import StartUpServer.AppServer;
import State.MeetingState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportBodySystem extends BaseSystem{
    private final List<DeadBody> deadBodies = new ArrayList<>();

    @Override
    public void update() {}

    @Override
    public void handleAction(Player bodyFinder, InputRequest packet) {
        if (packet.isReportKey() && bodyFinder.getComponent(AliveComp.class).isAlive()){
            Optional<DeadBody> deadBodyOptional = getDeadBody(bodyFinder);
            deadBodyOptional.ifPresent(deadBody ->{
                callMeeting();
            });
        }
    }

    private void callMeeting(){
        AppServer.currentGame.getStateManager().pushState(new MeetingState());
    }

    public void removeDeadBodies(){
        RegistryHandler.entityRegistryServer.removeEntities(deadBodies);
        this.deadBodies.clear();
    }

    private Optional<DeadBody> getDeadBody(Player bodyFinder){
        return DistanceFinder.getClosestEntity(bodyFinder, deadBodies, 100);
    }

    public List<DeadBody> getDeadBodies() {
        return deadBodies;
    }
}

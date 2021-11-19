package System;

import Component.AliveComp;
import DistanceFinder.DistanceFinder;
import Entity.DeadPlayer;
import Entity.EntityRegistryServer;
import Entity.Player;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class ReportBodySystem extends BaseSystem{
    private List<DeadPlayer> deadBodies = new ArrayList<>();

    @Override
    public void update() {

    }

    public void handleReport(Player bodyFinder, PosRequest packet) {
        if (packet.isReportKey() && bodyFinder.getComponent(AliveComp.class).isAlive()){
            Optional<DeadPlayer> deadBodyOptional = getDeadBody(bodyFinder);
            deadBodyOptional.ifPresent(deadBody ->{
                removeReportedBody(deadBody);
                callMeeting();
            });
        }
    }

    private void callMeeting(){
        EmergencyTableSystem emergencyTableSystem = new EmergencyTableSystem();
        AppServer.currentGame.getStateManager().getCurrentState().addSystem(emergencyTableSystem);
        emergencyTableSystem.activate();
    }
    //use booleans to see if emergency table is activated

    //ad emergency table system to state wheverever meeting is called


    private void removeReportedBody(DeadPlayer deadPlayer){
        this.deadBodies.remove(deadPlayer);
        EntityRegistryServer.removeEntity(deadPlayer);
    }

    private Optional<DeadPlayer> getDeadBody(Player bodyFinder){
        return DistanceFinder.getClosestEntity(bodyFinder, deadBodies, 100);//todo add a range
    }

    public List<DeadPlayer> getDeadBodies() {
        return deadBodies;
    }
}

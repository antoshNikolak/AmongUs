package System;

import Component.AliveComp;
import DistanceFinder.DistanceFinder;
import Entity.DeadBody;
import Entity.Entity;
import Entity.EntityRegistryServer;
import Entity.Player;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportBodySystem extends BaseSystem{
    private List<DeadBody> deadBodies = new ArrayList<>();

    @Override
    public void update() {}

    @Override
    public void handleAction(Player bodyFinder, PosRequest packet) {
        if (packet.isReportKey() && bodyFinder.getComponent(AliveComp.class).isAlive()){
            Optional<DeadBody> deadBodyOptional = getDeadBody(bodyFinder);
            deadBodyOptional.ifPresent(deadBody ->{
                removeReportedBody(deadBody);
                callMeeting();
            });
        }
    }

//    public void handleReport(Player bodyFinder, PosRequest packet) {
//        if (packet.isReportKey() && bodyFinder.getComponent(AliveComp.class).isAlive()){
//            Optional<DeadBody> deadBodyOptional = getDeadBody(bodyFinder);
//            deadBodyOptional.ifPresent(deadBody ->{
//                removeReportedBody(deadBody);
//                callMeeting();
//            });
//        }
//    }

    private void callMeeting(){
        EmergencyTableSystem emergencyTableSystem = new EmergencyTableSystem();
//        System.out.println("adding emergency system from report body system");
//        AppServer.currentGame.getStateManager().getCurrentState().addSystem(emergencyTableSystem);
        emergencyTableSystem.activate();
    }
    //use booleans to see if emergency table is activated

    //ad emergency table system to state wheverever meeting is called


    private void removeReportedBody(DeadBody deadBody){
        this.deadBodies.remove(deadBody);
        EntityRegistryServer.removeEntity(deadBody);
    }

    private Optional<DeadBody> getDeadBody(Player bodyFinder){
        return DistanceFinder.getClosestEntity(bodyFinder, deadBodies, 100);//todo add a range
    }

    public List<DeadBody> getDeadBodies() {
        return deadBodies;
    }
}

package System;

import Component.AliveComp;
import DistanceFinder.DistanceFinder;
import Entity.DeadBody;
import Entity.Player;
import Packet.Position.PosRequest;
import Registry.RegistryHandler;
import StartUpServer.AppServer;
import State.MeetingState;

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
        AppServer.currentGame.getStateManager().pushState(new MeetingState());
//        EmergencyTableSystem emergencyTableSystem = new EmergencyTableSystem();
//        emergencyTableSystem.activate();

//        System.out.println("adding emergency system from report body system");
//        AppServer.currentGame.getStateManager().getCurrentState().addSystem(emergencyTableSystem);
    }
    //use booleans to see if emergency table is activated

    //ad emergency table system to state wheverever meeting is called


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

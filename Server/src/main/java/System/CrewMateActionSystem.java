package System;

import Entity.Player;
import Packet.Packet;
import Packet.Position.PosRequest;

public class CrewMateActionSystem extends BaseSystem{
    @Override
    public void update() {

    }

    public void handleSpecialAction(Player crewMate, PosRequest packet){
        if (packet.isReportKey()){
//            CrewMa

        }
    }
}

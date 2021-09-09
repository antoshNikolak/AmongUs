package Packet.AddEntityReturn;


import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;

import java.util.ArrayList;
import java.util.List;

public class AddLocalEntityReturn extends AddEntityReturn {



    private AddLocalEntityReturn() {
        super();
    }


    public AddLocalEntityReturn(NewAnimatedEntityState localPlayer) {
        super();
        List<NewAnimatedEntityState> localPlayers = new ArrayList<>();
        localPlayers.add(localPlayer);
        this.newEntityStates = new ArrayList<>(localPlayers);
    }

    public NewAnimatedEntityState getNewEntityState(){
        return (NewAnimatedEntityState) newEntityStates.get(0);
    }



}

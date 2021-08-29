package Packet.Position;


import java.util.List;

public class AddChangingEntityReturn extends AddEntityReturn{


    public AddChangingEntityReturn() {
        super();
    }


    public AddChangingEntityReturn(List<NewEntityState> newEntityStates) {
        super(newEntityStates);
    }


    public AddChangingEntityReturn(NewEntityState... entityStates) {
        super(entityStates);
    }



}

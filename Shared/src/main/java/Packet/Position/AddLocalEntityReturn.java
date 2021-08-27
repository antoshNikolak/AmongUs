package Packet.Position;


public class AddLocalEntityReturn extends AddChangingEntityReturn {



    private AddLocalEntityReturn() {
        super();
    }


    public AddLocalEntityReturn(NewEntityState entityState) {
        super();
        newEntityStates.add(entityState);
    }

    public NewEntityState getNewEntityState(){
        return newEntityStates.get(0);
    }



}

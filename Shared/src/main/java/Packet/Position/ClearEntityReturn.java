package Packet.Position;

import Packet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearEntityReturn implements Packet {

    private List<Integer> registrationIDs;


    public ClearEntityReturn(List<Integer> registrationIDs) {
        this.registrationIDs = registrationIDs;
    }

    public ClearEntityReturn(Integer... IDs){
        registrationIDs = new ArrayList<>();
        registrationIDs.addAll(Arrays.asList(IDs));
    }

    private ClearEntityReturn() {
    }

    public List<Integer> getRegistrationIDs() {
        return registrationIDs;
    }
}

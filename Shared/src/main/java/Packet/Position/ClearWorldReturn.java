package Packet.Position;

import Packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class ClearWorldReturn implements Packet {

    private List<Integer> tileIDs;

    public ClearWorldReturn(List<Integer> tileIDs) {
        this.tileIDs = tileIDs;
    }

    private ClearWorldReturn() {
    }

    public List<Integer> getTileIDs() {
        return tileIDs;
    }
}

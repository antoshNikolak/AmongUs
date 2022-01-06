package Packet.LeaderBoard;

import Packet.Packet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LeaderBoardReturn implements Packet {
    public LinkedHashMap<String, Double> userTimeMap;

    public LeaderBoardReturn(LinkedHashMap<String, Double> userTimeMap) {
        this.userTimeMap = userTimeMap;
    }

    public LeaderBoardReturn() {}




}

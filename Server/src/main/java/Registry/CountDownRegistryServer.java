package Registry;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Packet.CountDown.CountDown;
import Packet.CountDown.RemoveCountDown;

import java.util.*;

public class CountDownRegistryServer extends Registry<CountDown> {

    private final Map<Integer, List<Integer>> countDownClientMap = new HashMap<>();
    //maps count down id with connection ids it was sent to

    @Override
    public void removeEntity(int countDownID) {
        if (!IDMap.containsKey(countDownID)) return;
        countDownClientMap.remove(countDownID);
        super.removeEntity(countDownID);
    }

    public void stopCountDown(CountDown countDown){
        Integer countDownID = getItemID(countDown);
        ConnectionServer.sendTCP(new RemoveCountDown(countDown.id), countDownClientMap.get(countDownID));
    }


    public void addEntity(CountDown entity, List<Integer> connectionIDs) {
        super.addEntity(entity);
        countDownClientMap.put(getItemID(entity), connectionIDs);
    }

    @Override
    public void addEntity(CountDown entity) {
        super.addEntity(entity);
        countDownClientMap.put(getItemID(entity), new ArrayList<>());

    }

    public HashSet<CountDown> getCountDowns(){
        return new HashSet<CountDown>(IDMap.values());
    }

    public List<Integer> getCountDownConnections(CountDown countDown){
        return countDownClientMap.get(getItemID(countDown));
    }

    //    public Map<Integer, CountDown> getIDMap(){
//        return IDMap;
//    }

}

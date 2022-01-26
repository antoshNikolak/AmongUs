package Registry;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Packet.CountDown.CountDown;
import Packet.CountDown.RemoveCountDown;

import java.util.*;

public class CountDownRegistryServer extends Registry<CountDown> {
    private final Map<Integer, List<Integer>> countDownClientMap = new HashMap<>();//todo what happens when a value becomes null, does it get removed
    //maps count down id with connection ids it was sent to

    @Override
    public void removeEntity(int countDownID) {
        if (!IDMap.containsKey(countDownID)) return;//here, we need to remove
//        ConnectionServer.sendTCP(new RemoveCountDown(countDownID), countDownClientMap.get(countDownID));//removes entity on the client side //todo only send to appropriate clients
        countDownClientMap.remove(countDownID);//todo useless
        super.removeEntity(countDownID);
    }

    public void stopCountDown(CountDown countDown){
        int countDownID = getItemID(countDown);
        ConnectionServer.sendTCP(new RemoveCountDown(countDownID), countDownClientMap.get(countDownID));
    }

//    public void removeEntity


    public void addEntity(CountDown entity, List<Integer> connectionID) {
        super.addEntity(entity);
        countDownClientMap.put(getItemID(entity), connectionID);

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

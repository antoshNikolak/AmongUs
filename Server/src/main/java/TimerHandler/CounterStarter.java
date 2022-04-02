package TimerHandler;

import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Packet.CountDown.CountDown;
import Registry.RegistryHandler;

import java.util.*;

public class CounterStarter {

    public static void startCountDown(int countDownValue, int x, int y, int size, CountDownFinish onCountDownFinished) {
        Integer [] connectionIds =  ConnectionServer.getClientConnectionIDs().toArray(new Integer[0]);//size 0, because we need to parse empty array
        startCountDown(countDownValue, x, y, size, onCountDownFinished, connectionIds);
    }

    public static void startCountDown(int countDownValue, int x, int y, int size, CountDownFinish onCountDownFinished, Integer... connectionIDs) {
        CountDown countDown = new  CountDown(RegistryHandler.countDownRegistryServer.getFreeID(), countDownValue, x, y, size);
        RegistryHandler.countDownRegistryServer.addEntity(countDown, Arrays.asList(connectionIDs));//register count down
        ConnectionServer.sendTCP(countDown, Arrays.asList(connectionIDs));//send countdown to appropriate users
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {//when timer finished
                onCountDownFinished.execute();//execute input interface implementation
                if (RegistryHandler.countDownRegistryServer.getItemID(countDown) != null) {//check it has not been removed somewhere else in the code
                    RegistryHandler.countDownRegistryServer.removeEntity(countDown);//deregister count down
                    cancel();
                }
            }
        },  countDown.countDownValue * 1000L);
    }
}



package TimerHandler;

import ConnectionServer.ConnectionServer;
import Entity.GameClientHandler;
import StartUpServer.AppServer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerStarter {

    public  static void startTimer(String timerString,int counter, TimerFinish onTimerFinished){
        AtomicInteger atomicCounter = new AtomicInteger(counter);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int counter = atomicCounter.decrementAndGet();
                ConnectionServer.sendTCPToAllPlayers(TimerFactory.getTimerPacket(timerString, counter));
                if (counter == 0){
                    onTimerFinished.execute();
                    cancel();
                }
            }
        }, 0, 1000);
    }

    public  static void startTimer(String timerString, int counter, TimerFinish onTimerFinished,  Integer ... connectionIDs){
        AtomicInteger atomicCounter = new AtomicInteger(counter);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int counter = atomicCounter.decrementAndGet();
                ConnectionServer.sendTCPTo(TimerFactory.getTimerPacket(timerString, counter), new ArrayList<>(Arrays.asList(connectionIDs)));
                if (counter == 0){
                    onTimerFinished.execute();
                    cancel();
                }
            }
        }, 0, 1000);
    }
}



package TimerHandler;

import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Packet.Timer.Timer;
import Packet.Timer.VotingTimer;

public class TimerFactory {

    public static Timer createTimerPacket(String timerString, int seconds){
        try {
            if (timerString.equalsIgnoreCase("GameStartTimer")){
                return new GameStartTimer(seconds);
            }else if (timerString.equalsIgnoreCase("KillCoolDownTimer")){
                return new KillCoolDownTimer(seconds);
            }else if (timerString.equalsIgnoreCase("VotingTimer")){
                return new VotingTimer(seconds);
            } else {
                throw new IllegalArgumentException("Timer Factory received an illegal argument");
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return null;
        }
    }
}

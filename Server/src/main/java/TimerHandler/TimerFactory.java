package TimerHandler;

//import Packet.Timer.GameStartCountDown;
//import Packet.Timer.KillCoolDownCountDown;
//import Packet.Timer.CountDown;
//import Packet.Timer.VotingCountDown;

//public class TimerFactory {
//
//    public static CountDown createTimerPacket(String timerString, int seconds){
//        try {
//            if (timerString.equalsIgnoreCase("GameStartTimer")){
//                return new GameStartCountDown(seconds);
//            }else if (timerString.equalsIgnoreCase("KillCoolDownTimer")){
//                return new KillCoolDownCountDown(seconds);
//            }else if (timerString.equalsIgnoreCase("VotingTimer")){
//                return new VotingCountDown(seconds);
//            } else {
//                throw new IllegalArgumentException("Timer Factory received an illegal argument");
//            }
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//}

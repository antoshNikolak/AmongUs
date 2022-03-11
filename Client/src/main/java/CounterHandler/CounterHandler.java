package CounterHandler;

import Packet.CountDown.CountDown;
import Screen.GameScreen;
import Screen.ScreenManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterHandler {

            private final static Map<Integer, CountDownTimerTask> countDownMap = new HashMap<>();
//    private final static Map<CountDown, CountDownTimerTask> countDownMap = new HashMap<>();


    public static void addCountDown(CountDown packet) {
        final AtomicInteger atomicInteger = new AtomicInteger(packet.countDownValue);
        final GameScreen gameScreen = ScreenManager.getScreen(GameScreen.class);
        final Label text = createText(packet, gameScreen);
        final GameScreen correctScreen = addTextToScreen(gameScreen, text);
        startTimer(text, atomicInteger, correctScreen, packet);
    }


    public static GameScreen addTextToScreen(GameScreen gameScreen, Label text) {
        GameScreen correctScreen;
        if (gameScreen.getNestedScreen() != null) {
            correctScreen = gameScreen.getNestedScreen();
        } else {
            correctScreen = gameScreen;
        }
        Platform.runLater(() -> {
            correctScreen.addNode(text);
        });
        return correctScreen;
    }


    public static void stopCountDown(int id) {
        Platform.runLater(() -> {
            CountDownTimerTask timerTask = countDownMap.get(id);
            timerTask.cancel();//invoke cancel method of timer task
            countDownMap.remove(id);//deregister countdown on client side
        });
    }

//    public static void stopCountDown(CountDown countDown) {
//        Platform.runLater(() -> {
//            CountDownTimerTask timerTask = countDownMap.get(countDown);
//            timerTask.cancel();//invoke cancel method of timer task
//            countDownMap.remove(countDown);//deregister countdown on client side
//        });
//    }
//
//    private static CountDown getCountDown(int id) {
//        for (CountDown countDown : countDownMap.keySet()) {
//            if (countDown.id == id) {
//                return countDown;
//            }
//        }
//        return null;
//    }

    private static void startTimer(Label text, AtomicInteger atomicInteger, GameScreen gameScreen, CountDown packet) {
        CountDownTimerTask countDownTimerTask = new CountDownTimerTask(text, atomicInteger, gameScreen);
        new Timer().scheduleAtFixedRate(countDownTimerTask, 1000, 1000);//decrement counter
        countDownMap.put(packet.id, countDownTimerTask);//register count down, and map it to the correct timer

    }

    private static Label createText(CountDown packet, GameScreen gameScreen) {
        Label text = new Label();
        text.setText(String.valueOf(packet.countDownValue));
        text.setLayoutX(packet.x);
        text.setLayoutY(packet.y);
        text.setFont(Font.font(packet.size));
        return text;
    }
}

class CountDownTimerTask extends TimerTask {
    private final Label text;
    private final AtomicInteger atomicInteger;
    private final GameScreen gameScreen;

    public CountDownTimerTask(Label text, AtomicInteger atomicInteger, GameScreen gameScreen) {
        this.text = text;
        this.atomicInteger = atomicInteger;
        this.gameScreen = gameScreen;
    }


    @Override
    public void run() {
        Platform.runLater(() -> {
            atomicInteger.getAndDecrement();//decrement counter
            text.setText(String.valueOf(atomicInteger.get()));
            if (atomicInteger.get() == 0) {//check count down finished
                cancel();//removes timer task from timer queue, invoke overridden method
            }
        });
    }

    @Override
    public boolean cancel() {
        gameScreen.removeNode(text);
        return super.cancel();
    }
}




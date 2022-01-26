package CounterHandler;

import Packet.CountDown.CountDown;
import Screen.GameScreen;
import Screen.ScreenManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterHandler {

    //    private final static Map<CountDown, Timer> countDownMap = new HashMap<>();
    private final static Map<CountDown, CountDownTimerTask> countDownMap = new HashMap<>();


    public static void addCountDown(CountDown packet) {
        final AtomicInteger atomicInteger = new AtomicInteger(packet.countDownValue);
        final GameScreen gameScreen = ScreenManager.getScreen(GameScreen.class);
        final Text text = createText(packet, gameScreen);
        final GameScreen correctScreen = addTextToScreen(gameScreen, text);
        startTimer(text, atomicInteger, correctScreen, packet);

    }


    public static GameScreen addTextToScreen(GameScreen gameScreen, Text text) {
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
        CountDown countDown = getCountDown(id);
        Platform.runLater(() -> {
            CountDownTimerTask timerTask = countDownMap.get(countDown);
            timerTask.removeCountDown();
            timerTask.cancel();//invoke cancel method of timer task
            countDownMap.remove(countDown);//deregister countdown on client side
        });

    }

    private static CountDown getCountDown(int id) {
        for (CountDown countDown : countDownMap.keySet()) {
            if (countDown.id == id) {
                return countDown;
            }
        }
        return null;
    }

    private static void startTimer(Text text, AtomicInteger atomicInteger, GameScreen gameScreen, CountDown packet) {
        CountDownTimerTask timerTask = new CountDownTimerTask(text, atomicInteger, gameScreen);
        new Timer().scheduleAtFixedRate(timerTask, 1000, 1000);//decrement counter
        countDownMap.put(packet, timerTask);//register count down, and map it to the correct timer
    }

    private static Text createText(CountDown packet, GameScreen gameScreen) {
        Text text = new Text();
        text.setText(String.valueOf(packet.countDownValue));
        text.setLayoutX(packet.x);
        text.setLayoutY(packet.y);
        text.setFont(Font.font(packet.size));
        return text;
    }
}

class CountDownTimerTask extends TimerTask {
    private final Text text;
    private final AtomicInteger atomicInteger;
    private final GameScreen gameScreen;

    public CountDownTimerTask(Text text, AtomicInteger atomicInteger, GameScreen gameScreen) {
        this.text = text;
        this.atomicInteger = atomicInteger;
        this.gameScreen = gameScreen;
    }

//    private void startPointerChecker() {
//        new Thread(() -> {
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            while (!text.getText().equals("0")){
//                if (!gameScreen.getPane().getChildren().contains(text)){
//                    System.out.println("ERROR, game screen doesnt contain text");
//                    for (Node node : gameScreen.getPane().getChildren()) {
//                        if (node instanceof Text) {
//                            System.out.println("node in list: " + node);
//                        }
//                    }
//                }
//            }
//        }).start();
//    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            atomicInteger.getAndDecrement();//decrement counter
            text.setText(String.valueOf(atomicInteger.get()));
            if (atomicInteger.get() == 0) {//check count down finished
                removeCountDown();
                cancel();//removes timer task from timer queue, invoke overridden method
            }
        });
    }

//    @Override
//    public boolean cancel() {
////        System.out.println("----------------------------");
////        for (Node node : gameScreen.getPane().getChildren()) {
////            if (node instanceof Text) {
////                if ((((Text) node).getText().equals("20"))) {
////                    System.out.println("stop");
////                }
////                System.out.println("node in list: " + node);
////            }
////        }
////        System.out.println("text removed: " + gameScreen.removeNode(text));//remove from screen
////        System.out.println("text I have: " + text);
////        System.out.println("-----------------------------");
//        return super.cancel();
//    }

    public void removeCountDown(){
        System.out.println("----------------------------");
        for (Node node : gameScreen.getPane().getChildren()) {
            if (node instanceof Text) {
//                if ((((Text) node).getText().equals("20"))) {
//                    System.out.println("stop");
//                }
                System.out.println("node in list: " + node);
            }
        }
        System.out.println("text removed: " + gameScreen.removeNode(text));//remove from screen
        System.out.println("text I have: " + text);
        System.out.println("-----------------------------");
    }

}


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

    //    private final static Map<CountDown, Timer> countDownMap = new HashMap<>();
//    private final static Map<CountDown, TimerTask> countDownMap = new HashMap<>();
    private final static Map<CountDown, FixedIntervalScheduler> countDownMap = new HashMap<>();
    private static List<TestObj> testObjs = new ArrayList<>();


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
        CountDown countDown = getCountDown(id);
//        Platform.runLater(() -> {
        FixedIntervalScheduler timerTask = countDownMap.get(countDown);
        timerTask.cancel();//invoke cancel method of timer task
        countDownMap.remove(countDown);//deregister countdown on client side
//        });

    }

    private static CountDown getCountDown(int id) {
        for (CountDown countDown : countDownMap.keySet()) {
            if (countDown.id == id) {
                return countDown;
            }
        }
        return null;
    }


    private static void startTimer(Label text, AtomicInteger atomicInteger, GameScreen gameScreen, CountDown packet) {
        FixedIntervalScheduler fixedIntervalScheduler = createTimerTask(gameScreen, atomicInteger, text, new TestObj(packet.countDownValue));
        fixedIntervalScheduler.schedule(1000);
//        new Timer().scheduleAtFixedRate(timerTask, 1000, 1000);//decrement counter
        countDownMap.put(packet, fixedIntervalScheduler);//register count down, and map it to the correct timer

//        onCancelMap.put(timerTask, () -> {
//            System.out.println("----------------------------");
//            for (Node node : gameScreen.getPane().getChildren()) {
//                if (node instanceof Text) {
//                    System.out.println("node in list: " + node);
//                }
//            }
//            System.out.println("text removed: " + gameScreen.removeNode(text));//remove from screen
//            System.out.println("text I have: " + text);
//            System.out.println("-----------------------------");
//        });
    }
////        return timer;
//    }

    private static FixedIntervalScheduler createTimerTask(GameScreen gameScreen, AtomicInteger atomicInteger, Label text, TestObj testObj) {
        testObjs.add(testObj);
        System.out.println("creating new test obj: " + testObj);
        return new CountDownScheduler(testObj, testObjs);
//        return new  FixedIntervalScheduler() {
//            @Override
//            public void run() {
//                testObj.setValue(testObj.getValue() - 1);
//                if (testObj.getValue() == 0) {//check count down finished
//                    cancel();//removes timer task from timer queue, invoke overridden method
//                }
//            }
//
//            @Override
//            public void cancel() {
//                System.out.println("----------------------------");
//                for (TestObj testObj1 : testObjs) {
//                    System.out.println("test obj in list: " + testObj1);
//
//                }
//                System.out.println("test obj removed: " + testObjs.remove(testObj));//remove from screen
//                System.out.println("test obj I have: " + testObj);
//                System.out.println("-----------------------------");
//                super.cancel();
//            }
//        };
//        return new TimerTask() {
//            @Override
//            public void run() {
////                Platform.runLater(() -> {
//                testObj.setValue(testObj.getValue() - 1);
//                if (testObj.getValue() == 0) {//check count down finished
//                    cancel();//removes timer task from timer queue, invoke overridden method
//                }
////                    atomicInteger.getAndDecrement();//decrement counter
////                    text.setText(String.valueOf(atomicInteger.get()));
////                    if (atomicInteger.get() == 0) {//check count down finished
////                        cancel();//removes timer task from timer queue, invoke overridden method
////                    }
////                });
//            }
//
//            @Override
//            public boolean cancel() {
//                System.out.println("----------------------------");
//                for (TestObj testObj1 : testObjs) {
//                    System.out.println("test obj in list: " + testObj1);
//
//                }
//                System.out.println("test obj removed: " + testObjs.remove(testObj));//remove from screen
//                System.out.println("test obj I have: " + testObj);
//                System.out.println("-----------------------------");
//                return super.cancel();
//
////                System.out.println("----------------------------");
////                for (Node node : gameScreen.getPane().getChildren()) {
////                    if (node instanceof Text) {
////                        System.out.println("node in list: " + node);
////                    }
////                }
////                System.out.println("text removed: " + gameScreen.removeNode(text));//remove from screen
////                System.out.println("text I have: " + text);
////                System.out.println("-----------------------------");
////                return super.cancel();
//            }
//        };
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

class CountDownScheduler extends FixedIntervalScheduler{
    private final TestObj testObj;
    private final List<TestObj> testObjs;

    public CountDownScheduler(TestObj testObj, List<TestObj> testObjs) {
        this.testObj = testObj;
        this.testObjs = testObjs;
    }

    @Override
    public void run() {
        testObj.setValue(testObj.getValue() - 1);
        if (testObj.getValue() == 0) {//check count down finished
            cancel();//removes timer task from timer queue, invoke overridden method
        }
    }

    @Override
    public void cancel() {
        System.out.println("----------------------------");
        for (TestObj testObj1 : testObjs) {
            System.out.println("test obj in list: " + testObj1);

        }
        System.out.println("test obj removed: " + testObjs.remove(testObj));//remove from screen
        System.out.println("test obj I have: " + testObj);
        System.out.println("-----------------------------");
        super.cancel();
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

    public void removeCountDown() {
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

//public class TestObj{
//    private int value;
//
//    public TestObj(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }
//}


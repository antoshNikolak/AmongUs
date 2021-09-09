package KeyManager;

import Screen.ScreenManager;
import javafx.scene.input.KeyCode;

public class KeyManager {

    private static boolean rightKeyPressed = false;
    private static boolean leftKeyPressed = false;
    private static boolean upKeyPressed = false;
    private static boolean downKeyPressed = false;
    private static boolean killKeyPressed = false;
    private static boolean taskKeyPressed = false;



    static {
        ScreenManager.getScene().setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.A) {
                leftKeyPressed = true;
            }
            if (keyCode == KeyCode.D) {
                rightKeyPressed = true;
            }
            if (keyCode == KeyCode.W) {
                upKeyPressed = true;
            }
            if (keyCode == KeyCode.S){
                downKeyPressed = true;
            }
            if (keyCode == KeyCode.K){
                killKeyPressed = true;
            }
            if (keyCode == KeyCode.T){
                taskKeyPressed = true;
            }
        });
        ScreenManager.getScene().setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.A) {
                leftKeyPressed = false;
            }
            if (keyCode == KeyCode.D) {
                rightKeyPressed = false;
            }
            if (keyCode == KeyCode.W) {
                upKeyPressed = false;
            }
            if (keyCode == KeyCode.S) {
                downKeyPressed = false;
            }
            if (keyCode == KeyCode.K) {
                killKeyPressed = false;
            }
            if (keyCode == KeyCode.T){
                taskKeyPressed = false;
            }
        });
    }

    public static boolean isRightKeyPressed() {
        return rightKeyPressed;
    }

    public static boolean isLeftKeyPressed() {
        return leftKeyPressed;
    }

    public static boolean isUpKeyPressed() {
        return upKeyPressed;
    }

    public static boolean isDownKeyPressed() {
        return downKeyPressed;
    }

    public static boolean isKillKeyPressed() {
        return killKeyPressed;
    }

    public static boolean isTaskKeyPressed() {
        return taskKeyPressed;
    }
}

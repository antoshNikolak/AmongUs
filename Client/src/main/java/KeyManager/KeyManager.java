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
    private static boolean reportKeyPressed = false;
    private static boolean emergencyMeetingKeyPressed = false;



    static {
        //listen for key board input
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
            if (keyCode == KeyCode.R){
                reportKeyPressed = true;
            }
            if (keyCode == KeyCode.E){
                emergencyMeetingKeyPressed = true;
            }

        });
        //listen for keys being released
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
            if (keyCode == KeyCode.R){
                reportKeyPressed = false;
            }
            if (keyCode == KeyCode.E){
                emergencyMeetingKeyPressed = false;
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

    public static boolean isReportKeyPressed() {
        return reportKeyPressed;
    }

    public static boolean isEmergencyMeetingKeyPressed() {
        return emergencyMeetingKeyPressed;
    }
}

package ScreenCounter;

import Screen.ScreenManager;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class ScreenCounter {
    private Text previousText;

    public  void updateCounterValue(String value) {
        if (value.equals("0")) {
            ScreenManager.getCurrentScreen().removeNode(previousText);
        } else {
            addCountDownValueToScreen(value);
        }
    }

    private void addCountDownValueToScreen(String value) {
        Text text = getConfigText(value);
        if (previousText != null)ScreenManager.getCurrentScreen().removeNode(previousText);
        ScreenManager.getCurrentScreen().addNode(text);
        previousText = text;
    }

    public abstract Text getConfigText(String value);
//    private static Text getConfigText(String value){
//        Text text = new Text(300, 200, value);
//        text.setFont(Font.font(50));
//        return text;
//    }
}

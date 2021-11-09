package ScreenCounter;

import Screen.ScreenManager;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Counter {
    private Text previousText;
    private final int x, y, size;
    private boolean timerOn = true;

    public Counter(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void updateCounterValue(String value) {
        if (value.equals("0")) {
            ScreenManager.getCurrentScreen().removeNode(previousText);
        } else if (timerOn) {
            addCountDownValueToScreen(value);
        }
    }

    private void addCountDownValueToScreen(String value) {
        Text text = getConfigText(value);
        if (previousText != null) ScreenManager.getCurrentScreen().removeNode(previousText);
        ScreenManager.getCurrentScreen().addNode(text);
        previousText = text;
    }

    private Text getConfigText(String value) {
        Text text = new Text(x, y, value);
        text.setFont(Font.font(size));
        return text;
    }

    public boolean isTimerOn() {
        return timerOn;
    }

    public void setTimerOn(boolean timerOn) {
        this.timerOn = timerOn;
        if (!timerOn && previousText != null) {
            ScreenManager.getCurrentScreen().removeNode(previousText);
        }
    }
}

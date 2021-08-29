package Screen;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ScreenCounter {
    private static Text previousText;

    public static void updateCounterValue(String value) {
        if (value.equals("0")){
            ScreenManager.getCurrentScreen().removeNode(previousText);
        }else {
            addCountDownValueToScreen(value);
        }
    }

    private static void addCountDownValueToScreen(String value){
        Text text = getConfigText(value);
        ScreenManager.getCurrentScreen().removeNode(previousText);
        ScreenManager.getCurrentScreen().addNode(text);
        previousText = text;
    }

    private static Text getConfigText(String value){
        Text text = new Text(300, 200, value);
        text.setFont(Font.font(50));
        return text;
    }
}

package ScreenCounter;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameStartCounter extends ScreenCounter{
    @Override
    public Text getConfigText(String value) {
        Text text = new Text(300, 200, value);
        text.setFont(Font.font(50));
        return text;
    }
}

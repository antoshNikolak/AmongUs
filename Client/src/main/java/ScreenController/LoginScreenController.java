package ScreenController;

import Screen.EntryScreen;
import Screen.ScreenManager;
import javafx.scene.input.MouseEvent;

public class LoginScreenController {
    public void back() {
        ScreenManager.activate(EntryScreen.class);
    }
}

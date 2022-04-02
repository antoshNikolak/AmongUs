package ScreenController;

import Screen.EntryScreen;
import Screen.ScreenManager;
import javafx.scene.input.MouseEvent;

public class LoginScreenController {
    public void back() {//handle back button being pressed
        ScreenManager.activate(EntryScreen.class);
    }
}

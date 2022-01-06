package ScreenController;

import Screen.MenuScreen;
import Screen.ScreenManager;
import javafx.event.ActionEvent;

public class LeaderBoardController {
    public void activateMenu() {
        ScreenManager.activate(MenuScreen.class);
    }
}

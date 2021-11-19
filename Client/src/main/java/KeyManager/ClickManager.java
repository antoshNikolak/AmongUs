package KeyManager;

import Node.SpaceButton;
import Screen.ScreenManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

//subject
public class ClickManager {
    private List<ButtonBase> observers = new ArrayList<>();
    //buttons are the observers
    public void init(){
        ScreenManager.getScene().setOnMousePressed(event -> notifyRelevantObservers(event.getSceneX(), event.getSceneY()));

    }

    

    public void notifyRelevantObservers(double x, double y){
        for (ButtonBase button: observers){
            if (x >= button.getLayoutX() && x <= button.getLayoutX() + button.getPrefWidth()){
                if (y >= button.getLayoutY() && y <= button.getLayoutY() + button.getPrefHeight()){
                    button.fire();
//                    button.getOnAction().handle(new ActionEvent());
                }
            }
        }
    }
}

package AlertBox;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class AlertBox {

    public static void display(String message){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(message);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        alert.getDialogPane().getButtonTypes().add(type);
        alert.show();
    }
}

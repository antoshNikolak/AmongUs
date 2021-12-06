package Node;


import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;

public class SpaceButton extends Button {//todo document including fxml
    public SpaceButton() {
        setStyle();
    }

    public SpaceButton(String text) {
        super(text);
        setStyle();
    }

    private void setStyle(){
        this.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: white;" +
                " -fx-border-width: 4;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 25px;");
    }
}

package Screen;

import javafx.scene.layout.Pane;

public class GameScreen extends Screen {
    private  Pane nestedPane;

    public GameScreen(Pane pane) {
        super(pane);
    }

    public Pane getNestedPane() {
        return nestedPane;
    }

    public void setNestedPane(Pane nestedPane) {
        this.nestedPane = nestedPane;
    }
}

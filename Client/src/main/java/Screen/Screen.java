package Screen;

import javafx.scene.layout.Pane;

public abstract class Screen {
    private Pane pane;

    public Screen(Pane pane) {
        this.pane = pane;
    }

    public Pane getPane() {
        return pane;
    }
}

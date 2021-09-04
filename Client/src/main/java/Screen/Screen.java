package Screen;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public abstract class Screen {
    private Pane pane;

    public void addNode(Node node){
        this.pane.getChildren().add(node);
    }

    public Screen(Pane pane) {
        this.pane = pane;
    }

    public Pane getPane() {
        return pane;
    }

    public void removeNode(Node item) {
        System.out.println("removing item: " + ((Text)item).getText());
        this.pane.getChildren().remove(item);
    }
}

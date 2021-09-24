package Entity;

import Component.HitBoxComp;
import Component.PosComp;
import Position.Pos;
import javafx.scene.shape.Line;

public class CollidableRect extends Entity {
    private Line line;

    public CollidableRect(Pos pos1,  int width, int height) {
        super();
        PosComp posComp = new PosComp(pos1.getX(), pos1.getY(), width, height );
        addComponent(posComp);
        addComponent(new HitBoxComp(posComp));
    }
}

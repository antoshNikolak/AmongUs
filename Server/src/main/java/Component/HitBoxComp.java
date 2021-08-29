package Component;

import Position.Pos;
import javafx.scene.shape.Rectangle;

public class HitBoxComp implements Component{

    private Rectangle hitBox;

    public HitBoxComp(PosComp posComp){
        this.hitBox = new Rectangle(posComp.getPos().getX(), posComp.getPos().getY(), posComp.getWidth(), posComp.getHeight());
    }

    public boolean intersect(HitBoxComp hitBoxComp){
        return hitBoxComp.hitBox.intersects(hitBox.getLayoutBounds());
    }

    public void setY(double y){
        this.hitBox.setY(y);
    }

    public void setX(double x){
        this.hitBox.setX(x);
    }

    public double getX(){
        return hitBox.getX();
    }

    public double getY(){
        return hitBox.getY();
    }

    public void incrementY(double deltaY){
        this.hitBox.setY(hitBox.getY()+ deltaY);
    }

    public void incrementX(double deltaX){
        this.hitBox.setX(hitBox.getX()+ deltaX);
    }

}

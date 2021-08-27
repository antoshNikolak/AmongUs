package EntityClient;

import Packet.Position.NewEntityState;
import Position.Pos;
import Screen.TextureManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import AnimationClient.AnimationManager;

public class Entity {
    protected Pos pos;
    protected AnimationManager animationManager;//use a map aswell




//    public Entity(Pos pos, String textureName) {
//        this.textureName = textureName;
//        this.pos = pos;
//    }

    public Entity (NewEntityState newEntityState){
        this.animationManager = new AnimationManager(newEntityState.getNewAnimationReturn());
        this.pos = newEntityState.getPos();
    }

    public void render(GraphicsContext gc){
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
        gc.drawImage(image, pos.getX(), pos.getY());
    }



}

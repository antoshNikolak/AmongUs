package EntityClient;

import Packet.Position.NewEntityState;
import Position.Pos;
import Screen.TextureManager;
import StartUp.AppClient;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import AnimationClient.AnimationManager;

public class Entity {
    protected Pos pos;
    protected AnimationManager animationManager;

    public Entity (NewEntityState newEntityState){
        EntityRegistryClient.addEntity(newEntityState.getRegistrationID(), this);
        this.animationManager = new AnimationManager(newEntityState.getNewAnimationReturn());
        this.pos = newEntityState.getPos();
        AppClient.currentGame.getEntities().add(this);
    }

    public void render(GraphicsContext gc){
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
        gc.drawImage(image, pos.getX(), pos.getY());
    }



}

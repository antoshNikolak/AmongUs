package EntityClient;

import Camera.Camera;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Position.Pos;
import Screen.GameScreen;
import Screen.ScreenManager;
import Screen.TextureManager;
import StartUp.AppClient;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import AnimationClient.AnimationManager;
import javafx.scene.shape.Line;

public class Entity {
    protected Pos pos;
    protected AnimationManager animationManager;

    public Entity(NewAnimatedEntityState newEntityState) {
        if (newEntityState.getRegistrationID() != -1) {
            EntityRegistryClient.addEntity(newEntityState.getRegistrationID(), this);
        }
        this.animationManager = new AnimationManager(newEntityState);
        this.pos = newEntityState.getPos();
        ScreenManager.getScreen(GameScreen.class).getEntities().add(this);
    }

//    public Entity(NewLineState newLineState) {//todo sulution does quite fit into the rest of the code
//        System.out.println("adding new line");
//        AppClient.currentGame.getEntities().add(this);
//        Line line = new Line(newLineState.getStartPos().getX(), newLineState.getStartPos().getY(), newLineState.getFinalPos().getX(), newLineState.getFinalPos().getY());
//        ScreenManager.getCurrentScreen().getPane().getChildren().add(line);
//    }



    public void render(GraphicsContext gc) {
        Camera camera = AppClient.currentGame.getCamera();
        Image image = TextureManager.getTexture(animationManager.getCurrentFrame());
        gc.drawImage(image, pos.getX() - camera.getOffsetX(), pos.getY() - camera.getOffsetY());
    }

    public Pos getPos() {
        return pos;
    }
}

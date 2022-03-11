package Camera;

import EntityClient.Entity;
//import EntityClient.Entity;
import Screen.ScreenManager;
import Screen.TextureManager;

public class Camera {

    private double offsetY;
    private double offsetX;


    public void centreOnEntity(Entity player) {
        double centreX = ScreenManager.getScene().getWidth() / 2;//centre x co ordinate relative to left most point of scene
        double centreY = ScreenManager.getScene().getHeight() / 2;//centre y co ordinate relative up most point of scene.
        this.offsetY = player.getPos().getY() - centreY + TextureManager.getTexture("left0-red").getHeight() / 2;//smallest y position visible on screen relative to origin
        this.offsetX = player.getPos().getX() - centreX + TextureManager.getTexture("left0-red").getWidth() / 2; //smallest x position visible on screen relative to origin
    }


    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }
}

package Camera;

import EntityClient.Entity;
import Screen.ScreenManager;
import Screen.TextureManager;

public class Camera {

    private double offsetY;
    private double offsetX;


    public void centreOnEntity(Entity player) {
        double centreX = ScreenManager.getScene().getWidth() / 2;
        double centreY = ScreenManager.getScene().getHeight() / 2;
        this.offsetY = player.getPos().getY() - centreY + TextureManager.getTexture("left0-red").getHeight() / 2;
        this.offsetX = player.getPos().getX() - centreX + TextureManager.getTexture("left0-red").getWidth() / 2;
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

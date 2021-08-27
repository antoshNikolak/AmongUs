package Screen;


import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private final static Map<String, Image> images = new HashMap<>();

    public static void init() {
        loadImage("dead-green", "Texture/green-dead.png");
        loadImage("standleft-green", "Texture/standleft-green.png" );
        loadImage("standright-green", "Texture/standright-green.png" );
        loadImage("left0-green", "Texture/left0-green.png");
        loadImage("left1-green", "Texture/left1-green.png");
        loadImage("right0-green", "Texture/right0-green.png");
        loadImage("right1-green", "Texture/right1-green.png");
        loadImage("grey-tile", "Texture/grey-tile.png", 50, 50, true);
    }

    // used to scale an image when its loaded
    public static void loadImage(String name, String file, int width, int height, boolean ratio) {
        images.put(name, new Image(file, width, height, ratio, false));
    }

    public static void loadImage(String name, String file) {
        images.put(name, new Image(file));
    }

    public static Image getTexture(String name) {
        return images.get(name);
    }


}

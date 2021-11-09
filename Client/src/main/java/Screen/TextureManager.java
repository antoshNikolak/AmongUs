package Screen;


import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private final static Map<String, Image> images = new HashMap<>();

    public static void init() {
        String[] playerColours = new String[]{"green", "blue", "orange", "cyan", "yellow", "pink", "red"};
        for (String colour : playerColours) {
            loadImage("dead-" + colour, "Texture/dead-" + colour + ".png");
            loadImage("standleft-" + colour, "Texture/standleft-" + colour + ".png", 50, 63, true);
            loadImage("standright-" + colour, "Texture/standright-" + colour + ".png", 50, 63, true);
            loadImage("left0-" + colour, "Texture/left0-" + colour + ".png", 50, 63, true);
            loadImage("left3-" + colour, "Texture/left1-" + colour + ".png", 50, 63, true);
            loadImage("left2-" + colour, "Texture/left2-" + colour + ".png", 50, 63, true);
            loadImage("left1-" + colour, "Texture/left3-" + colour + ".png", 50, 63, true);
            loadImage("right0-" + colour, "Texture/right0-" + colour + ".png", 50, 63, true);
            loadImage("right3-" + colour, "Texture/right1-" + colour + ".png", 50, 63, true);
            loadImage("right2-" + colour, "Texture/right2-" + colour + ".png", 50, 63, true);
            loadImage("right1-" + colour, "Texture/right3-" + colour + ".png", 50, 63, true);
            loadImage("ghost-right-" + colour, "Texture/ghost-right-" + colour + ".png", 50, 50, true);
            loadImage("ghost-left-" + colour, "Texture/ghost-left-" + colour + ".png", 50, 50, true);
        }
        loadImage("red-rect", "Texture/red-rectangle.png", 15, 15, true);
        loadImage("grey-tile", "Texture/grey-tile.png", 50, 50, true);
        loadImage("sudoku-task", "Texture/bookshelf.png", 50, 50, false);
        loadImage("maze-task", "Texture/maze-task.png", 50, 50, false);
        loadImage("number-count-task", "Texture/number-count-task.png", 50, 50, false);
        loadImage("meeting-table", "Texture/meeting-table.png", 150, 150, false);
        loadImage("emergency-meeting", "Texture/emergency-meeting.png", 600, 200, true);
        loadImage("dead-body-reported", "Texture/dead-body-reported.png", 600, 200, true);
        loadImage("task-bar", "Texture/task-bar.png", 500, 50, true);

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

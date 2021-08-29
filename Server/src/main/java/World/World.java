package World;

import Animation.AnimState;
import Component.AnimationComp;
import Component.PosComp;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Position.Pos;
import Utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {

    private final List<Tile> tiles = new ArrayList<>();
    public static final int TILE_WIDTH = 50;
    public static final int TILE_HEIGHT = 50;

    public World(String fileName) {
        createWorld(fileName);
    }

    public void createWorld(String fileName) {
        String contents = FileUtils.getFileAsString(fileName);
        String[] tokens = contents.split("\\s");//split at space
        int width = Integer.parseInt(tokens[0]);
        int height = Integer.parseInt(tokens[1]);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int id = Integer.parseInt(tokens[(x + y * width + 2)]);
                String textureName = getTextureName(id);

                if (!textureName.isEmpty()) {//todo redundant
                    addTile(textureName, x, y);
                }
            }
        }

    }

    private String getTextureName(int id) {
        String textureName = "no-tile";
        if (id == 1) {
            textureName = "grey-tile";
        } else if (id != 0) {
            throw new IllegalStateException("the world file nums are too big");
        }
        return textureName;
    }

    public List<Integer> getTileIDs(){
        return tiles.stream().
                map(EntityRegistryServer::getEntityID).
                collect(Collectors.toList());
    }

    private void addTile(String texture, double x, double y) {
        if (!texture.equals("no-tile")) {
            Pos tilePos = new Pos(x * TILE_WIDTH, y * TILE_HEIGHT);
            AnimationComp animationComp = new AnimationComp();
            animationComp.addAnimation(AnimState.RIGHT, new String[]{texture}, 2);//todo change to const
            tiles.add(new Tile(new PosComp(tilePos, TILE_WIDTH, TILE_HEIGHT)
                    , texture));
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public static int getTILE_WIDTH() {
        return TILE_WIDTH;
    }

    public static int getTILE_HEIGHT() {
        return TILE_HEIGHT;
    }
}

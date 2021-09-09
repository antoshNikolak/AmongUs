package World;

import Animation.AnimState;
import Component.AnimationComp;
import Component.PosComp;
import Component.TaskComp;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Position.Pos;
import State.MazeTaskState;
import State.NumberCountTaskState;
import State.SudokuTaskState;
import Utils.FileUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class World {

    private final Set<Tile> tiles = new HashSet<>();
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
                Tile tile = createTile(id, x, y);
                if (tile!= null)tiles.add(tile);
            }
        }

    }

    private Tile createTile(int id, int x, int y) {
        Tile tile = null;
        Pos tilePos = new Pos(x * TILE_WIDTH, y * TILE_HEIGHT);
        if (id == 1) {
            tile = createTile(tilePos, "grey-tile");
        } else if (id == 2) {
            tile = createTile(tilePos, "sudoku-task");
            tile.addComponent(new TaskComp(new SudokuTaskState()));
        } else if (id == 3) {
            tile = createTile(tilePos, "maze-task");
            tile.addComponent(new TaskComp(new MazeTaskState()));
        } else if (id == 4) {
            tile = createTile(tilePos, "number-count-task");
            tile.addComponent(new TaskComp(new NumberCountTaskState()));
        } else if (id != 0) {
            throw new IllegalStateException("the world file nums are too big");
        }
        return tile;
    }

    private Tile createTile(Pos tilePos, String textureName) {
        return new Tile(new PosComp(tilePos, TILE_WIDTH, TILE_HEIGHT), textureName);
    }

    public List<Integer> getTileIDs() {
        return tiles.stream().
                map(EntityRegistryServer::getEntityID).
                collect(Collectors.toList());
    }


    public Set<Tile> getTiles() {
        return tiles;
    }

    public static int getTILE_WIDTH() {
        return TILE_WIDTH;
    }

    public static int getTILE_HEIGHT() {
        return TILE_HEIGHT;
    }
}

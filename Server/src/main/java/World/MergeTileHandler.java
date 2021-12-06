package World;

import Component.AnimationComp;
import Component.PosComp;
import Entity.Tile;
import Position.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static World.World.TILE_HEIGHT;
import static World.World.TILE_WIDTH;

public class MergeTileHandler {

    private final List<GroupedSquareTile> groupedTiles = new ArrayList<>();
    private final WorldDimension dimension;
    private final Tile [][] tileMatrix;

    public MergeTileHandler(Tile[][] tileMatrix, WorldDimension dimension) {
        this.tileMatrix = tileMatrix;
        this.dimension = dimension;
    }

//    public void handleMergedTiles(String [][] tokens){
//        createLargeTiles(tokens);
//        handleMergingGroupedTiles();
//    }

    public void createLargeTiles(String[][] tokens) {
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                Pos pos = new Pos(x * TILE_WIDTH, y * TILE_HEIGHT);
                if (tokens[y][x].equals("5")) {
                    Tile tile = TileFactory.createTile(pos, "meeting-table");
                    clusterGroupedTiles(tokens, tile, x, y, 5);
                }
            }
        }
    }

    public void handleMergingGroupedTiles() {
        for (GroupedSquareTile groupedSquareTile : groupedTiles) {
            Tile tile = groupedSquareTile.mergeIntoTile();
            Pos pos = tile.getComponent(PosComp.class).getPos();
            tileMatrix[(int) (pos.getY() / TILE_HEIGHT)][(int) (pos.getX() / TILE_WIDTH)] = tile;
            handleSpecialMergeTileBehaviour(tile);

        }
    }

    public void clusterGroupedTiles(String[][] tokens, Tile tile, int x, int y, int id) {
        if (Integer.parseInt(tokens[y - 1][x]) == id) {
            addDisplacedTile(tile, x, y, 0, -1);
        } else if (Integer.parseInt(tokens[y][x - 1]) == id) {
            addDisplacedTile(tile, x, y, -1, 0);
        } else {
            groupedTiles.add(new GroupedSquareTile(tile));
        }
    }

    private void addDisplacedTile(Tile tile, int x, int y, int displacementX, int displacementY) {
        Tile displacedTile = tileMatrix[y + displacementY][x + displacementX];
        for (GroupedSquareTile groupedSquareTile : groupedTiles) {
            if (groupedSquareTile.getTiles().contains(displacedTile)) {
                groupedSquareTile.getTiles().add(tile);
            }
        }
    }



    private void handleSpecialMergeTileBehaviour(Tile tile){
        if (tile.getComponent(AnimationComp.class).getCurrentAnimation().getCurrentFrame().equals("meeting-table")){
            SpawnPointHandler.handleSpawnPoints(tile.getComponent(PosComp.class).getPos(), 3);
        }
    }
}

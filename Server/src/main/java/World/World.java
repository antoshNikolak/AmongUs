package World;

import Component.TaskComp;
import Entity.EntityRegistryServer;
import Entity.Tile;
import Position.Pos;
import Utils.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class World {

    private Tile[][] tileMatrix;
    public static final int TILE_WIDTH = 50;
    public static final int TILE_HEIGHT = 50;
    private WorldDimension dimension;

    public World(String fileName) {
        createWorld(fileName);
    }

    public void createWorld(String fileName) { ;
        String[] tokens = createTokens(fileName);
        initWorldParams(tokens);
        String[][] tokenMatrix = create2DTokenArray(tokens);
        tileMatrix = new Tile[dimension.getHeight()][dimension.getWidth()];
        mapTokensToTiles(tokenMatrix);
        new MergeTileHandler(tileMatrix, dimension).handleMergedTiles(tokenMatrix);
    }

    private void mapTokensToTiles(String [][] tokenMatrix){
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                int id = Integer.parseInt(tokenMatrix[y][x]);
                Tile tile = TileFactory.handleTileCreation(id, new Pos(x * TILE_WIDTH, y * TILE_HEIGHT));
                tileMatrix[y][x] = tile;
            }
        }
    }

    private void initWorldParams(String [] tokens){
        this.dimension = new WorldDimension(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }

    private String[] createTokens(String fileName){
        String contents = FileUtils.getFileAsString(fileName);
        return contents.split("\\s");//split at space
    }

    private String[][] create2DTokenArray(String[] tokens) {
        String[][] tokens2D = new String[dimension.getHeight()][dimension.getWidth()];
        for (int i = 2; i < tokens.length; i++) {
            int y = (int) Math.floor((i - 2) / (double) dimension.getWidth());//method reutnr double for precision
            int x = (i - 2) % dimension.getWidth();
            tokens2D[y][x] = tokens[i];
        }
        return tokens2D;
    }


    public List<Integer> getTileIDs() {
        return getTiles().stream().
                map(EntityRegistryServer::getEntityID).
                collect(Collectors.toList());
    }


    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                if (tileMatrix[y][x]!= null){
                    tiles.add(tileMatrix[y][x]);
                }
            }
        }
        return tiles;
    }

    public Set<Tile> getTilesWithTask() {
        return getTiles().stream().
                filter(tile -> tile.hasComponent(TaskComp.class)).
                collect(Collectors.toSet());
    }

    public static int getTILE_WIDTH() {
        return TILE_WIDTH;
    }

    public static int getTILE_HEIGHT() {
        return TILE_HEIGHT;
    }
}

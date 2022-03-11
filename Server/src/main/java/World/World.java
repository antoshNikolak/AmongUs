package World;

import Component.TaskComp;
import Entity.Tile;
import Position.Pos;
import Registry.RegistryHandler;
import Utils.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class World {

    private Tile[][] tileMatrix;
    public static final int TILE_WIDTH = 50;
    public static final int TILE_HEIGHT = 50;
    private WorldDimension dimension;
    private MergeTileHandler mergeTileHandler;
    private Tile mainTable;

    public World(String fileName) {
        createWorld(fileName);
    }

    public void createWorld(String fileName) { ;
        String[] tokens = createTokens(fileName);//create array of numbers representing tiles
        initWorldParams(tokens);//initialise world width and height
        String[][] tokenMatrix = create2DTokenArray(tokens);
        tileMatrix = new Tile[dimension.getHeight()][dimension.getWidth()];
        mapTokensToTiles(tokenMatrix);
        this.mergeTileHandler = new MergeTileHandler(tileMatrix, dimension);
        createLargeTiles(tokenMatrix);
        mergeTileHandler.handleMergingGroupedTiles();

//        new MergeTileHandler(tileMatrix, dimension).handleMergedTiles(tokenMatrix);
    }

    private void createLargeTiles(String[][] tokens) {
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                Pos pos = new Pos(x * TILE_WIDTH, y * TILE_HEIGHT);
                if (tokens[y][x].equals("5")) {//id maps to meeting table
                    Tile tile = TileFactory.createTile(pos, "meeting-table");//creates meeting table tile obj
                    mergeTileHandler.clusterGroupedTiles(tokens, tile, x, y, 5);//record tile so can be merged later
                    this.mainTable = tile;
                }
            }
        }
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

    //create an integer which will later be mapped to tiles, so that the
    //positions of each tile can be identified during later use
    private String[][] create2DTokenArray(String[] tokens) {
        String[][] tokens2D = new String[dimension.getHeight()][dimension.getWidth()];
        for (int i = 2; i < tokens.length; i++) {
            int y = (int) Math.floor((i - 2) / (double) dimension.getWidth());//y pos of tile within matrix
            int x = (i - 2) % dimension.getWidth();//x pos of tile within matrix
            tokens2D[y][x] = tokens[i];//add to tile matrix
        }
        return tokens2D;
    }


    public List<Integer> getTileIDs() {
        return getTiles().stream().
                map(RegistryHandler.entityRegistryServer::getItemID).
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

    public Tile getMainTable() {
        return mainTable;
    }

}

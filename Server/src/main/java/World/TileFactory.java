package World;

import Component.PosComp;
import Component.TaskComp;
import Entity.Tile;
import Position.Pos;
import StartUpServer.AppServer;
import State.MazeTaskState;
import State.SudokuTaskState;

import static World.World.TILE_HEIGHT;
import static World.World.TILE_WIDTH;

public class TileFactory {

    public static Tile handleTileCreation(int id, Pos tilePos) {
        Tile tile = null;
//        Pos tilePos = new Pos(x, y);
        if (id == 1) {
            tile = createTile(tilePos, "grey-tile");
        } else if (id == 2) {
            tile = createTile(tilePos, "sudoku-task");
            tile.addComponent(new TaskComp(SudokuTaskState.class));
        } else if (id == 3) {
            tile = createTile(tilePos, "maze-task");
            tile.addComponent(new TaskComp(MazeTaskState.class));
//        else if (id == 4) {
//            tile = createTile(tilePos, "number-count-task");
//            tile.addComponent(new TaskComp(NumberCountTaskState.class));
        } else if (id != 0 && id != 5) {//check not 0, or not a type or special enlarged tile
            throw new IllegalStateException("invalid id number");
        }
        return tile;
    }

    public static Tile createTile(Pos tilePos, String textureName) {
        Tile tile = new Tile(new PosComp(tilePos, TILE_WIDTH, TILE_HEIGHT), textureName);
        AppServer.currentGame.getStateManager().getTopState().getEntities().add(tile);
        return new Tile(new PosComp(tilePos, TILE_WIDTH, TILE_HEIGHT), textureName);
    }
}

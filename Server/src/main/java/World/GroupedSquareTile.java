package World;

import Component.AnimationComp;
import Component.PosComp;
import Entity.Tile;
import Position.Pos;

import java.util.ArrayList;
import java.util.List;



public class GroupedSquareTile {

    private final List<Tile> tiles = new ArrayList<>();
    private final Pos pos;
    private final String texture;

    public GroupedSquareTile(Tile tile) {
        this.pos = tile.getComponent(PosComp.class).getPos();
        this.texture = tile.getComponent(AnimationComp.class).getCurrentAnimation().getCurrentFrame();
    }

    public Tile mergeIntoTile(){
        double tileDimension =  Math.sqrt(tiles.size());
        if (isInteger(tileDimension)) {//ensure the merged tile will come out as a square
            PosComp posComp = new PosComp(pos, tileDimension * World.TILE_WIDTH, tileDimension *World.TILE_HEIGHT);
            return new Tile(posComp, texture);
        }else {
            throw new IllegalStateException("tiles cant be merged, they arent square");
        }
    }

    public boolean isInteger(double x){
        return Math.floor(x) == x;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}

package World;

import Component.PosComp;
import Component.RespawnComp;
import Entity.Player;
import Position.Pos;
import Math.*;

import java.util.List;

import static StartUpServer.AppServer.currentGame;
import static World.World.TILE_WIDTH;

public class SpawnPointHandler {

    public static void handleSpawnPoints(Pos tablePos, int tilesPerTableWidth) {
        assignPlayerTablePositions(
                new Pos(tablePos.getX() + (((double)TILE_WIDTH *tilesPerTableWidth)/2),  tablePos.getY() + ((double)TILE_WIDTH *tilesPerTableWidth)/2), tilesPerTableWidth);
        //call which finds the mid point of the table
    }

    private static void assignPlayerTablePositions(Pos tableCentre, int tilesPerTableWidth) {
        double outerCircleRadius = (tilesPerTableWidth * TILE_WIDTH) / Math.sqrt(2);
        CircleProperty circleProperty = new CircleProperty(outerCircleRadius, tableCentre);
        assignPlayerRespawnComps(getPlacementPositions(circleProperty), tableCentre);
    }

    private static void assignPlayerRespawnComps(Pos[] placements, Pos tableCentre) {
        List<Player> players = currentGame.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PosComp playerPosComp = player.getComponent(PosComp.class);
            adjustPlacementPosition(placements[i], tableCentre, playerPosComp);
            player.addComponent(new RespawnComp(placements[i]));
            playerPosComp.setPos(placements[i]);
        }
    }

    private static void adjustPlacementPosition(Pos placement, Pos tableCentre, PosComp playerPosComp){//compensate for player width and height
        if(placement.getX() <= tableCentre.getX())placement.incrementX(-playerPosComp.getWidth());
        if(placement.getY() <= tableCentre.getY())placement.incrementY(-playerPosComp.getHeight());
    }

    private static Pos[] getPlacementPositions(CircleProperty circleProperty){
        Pos [] placements = new Pos[8];
        Pos tableCentre = circleProperty.getCentre();
        double circleRadius = circleProperty.getRadius();

        //create positions by changing y.
        double placementX1 = tableCentre.getX() - (circleRadius/3);
        double [] y1Values = circleProperty.getYValues(placementX1);
        placements[0] = new Pos(placementX1, y1Values[0]);
        placements[1] = new Pos(placementX1, y1Values[1]);

        //create positions by changing y.
        double placementX2 = tableCentre.getX() + (circleRadius/3);
        double [] y2Values = circleProperty.getYValues(placementX2);
        placements[2] = new Pos(placementX2, y2Values[0]);
        placements[3] = new Pos(placementX2, y2Values[1]);

        //create positions by changing x.
        double placementY1 = tableCentre.getY() - (circleRadius/3);
        double [] x1Values = circleProperty.getXValues(placementY1);
        placements[4] = new Pos(x1Values[0], placementY1);
        placements[5] = new Pos(x1Values[1], placementY1);

        //create positions by changing x.
        double placementY2 = tableCentre.getY() + (circleRadius/3);
        double [] x2Values = circleProperty.getXValues(placementY2);
        placements[6] = new Pos(x2Values[0], placementY2);
        placements[7] = new Pos(x2Values[1], placementY2);

        return placements;
    }

}

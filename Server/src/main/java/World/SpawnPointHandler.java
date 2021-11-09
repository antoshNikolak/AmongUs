package World;

import Component.PosComp;
import Component.RespawnComp;
import Entity.Player;
import Position.Pos;
import StartUpServer.AppServer;
import Math.*;

import static World.World.TILE_WIDTH;

public class SpawnPointHandler {

    public static void handleSpawnPoints(Pos tablePos, int tileDimensions) {
        assignPlayerTablePositions(
                new Pos(tablePos.getX() + (((double)TILE_WIDTH *tileDimensions)/2),  tablePos.getY() + ((double)TILE_WIDTH *tileDimensions)/2));
    }

    private static void assignPlayerTablePositions(Pos tableCentre) {
        double inscribedCircleRadius = (3 * TILE_WIDTH) / Math.sqrt(2);
        CircleProperty circleProperty = new CircleProperty(inscribedCircleRadius, tableCentre);//calculate inscribed circle radius
        assignPlayerRespawnComps(getPlacementPositions(circleProperty), tableCentre);
    }

    private static void assignPlayerRespawnComps(Pos[] placements, Pos tableCentre) {
        for (int i = 0; i < AppServer.currentGame.getPlayers().size(); i++) {
            Player player = AppServer.currentGame.getPlayers().get(i);
            PosComp playerPosComp = AppServer.currentGame.getPlayers().get(i).getComponent(PosComp.class);
            adjustPlacementPosition(placements[i], tableCentre, playerPosComp);
            player.addComponent(new RespawnComp(placements[i]));
            playerPosComp.setPos(placements[i]);
//            System.out.println("setting player to pos: "+ player.getComponent(PosComp.class).getPos());
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

        double placementX1 = tableCentre.getX() - (circleRadius/3);
        double [] y1Values = circleProperty.getYValues(placementX1);
        placements[0] = new Pos(placementX1, y1Values[0]);
        placements[1] = new Pos(placementX1, y1Values[1]);

        double placementX2 = tableCentre.getX() + (circleRadius/3);
        double [] y2Values = circleProperty.getYValues(placementX2);
        placements[2] = new Pos(placementX2, y2Values[0]);
        placements[3] = new Pos(placementX2, y2Values[1]);

        double placementY1 = tableCentre.getY() - (circleRadius/3);
        double [] x1Values = circleProperty.getXValues(placementY1);
        placements[4] = new Pos(x1Values[0], placementY1);
        placements[5] = new Pos(x1Values[1], placementY1);

        double placementY2 = tableCentre.getY() + (circleRadius/3);
        double [] x2Values = circleProperty.getXValues(placementY2);
        placements[6] = new Pos(x2Values[0], placementY2);
        placements[7] = new Pos(x2Values[1], placementY2);

        return placements;
    }

}

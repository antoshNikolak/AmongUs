package DistanceFinder;

import Component.PosComp;
import Entity.Entity;
import Position.Pos;

public class DistanceFinder {

    public static <T extends Entity, S extends Entity> EntityDistance<T, S> getDistanceBetweenEntities(T e1, S e2) {
//        if (!e1.hasComponent(PosComp.class) || !e2.hasComponent(PosComp.class)) {
//            return null;
//        }
        checkEntitiesHavePosComps(e1, e2);
        Pos pos1 = e1.getComponent(PosComp.class).getPos();
        Pos pos2 = e2.getComponent(PosComp.class).getPos();
        double deltaX = pos1.getX() - pos2.getX();
        double deltaY = pos1.getY() - pos2.getY();
        return new EntityDistance<T, S>(e1, e2, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    private static void checkEntitiesHavePosComps(Entity ... entities){
        for (Entity entity: entities){
            if (!entity.hasComponent(PosComp.class)){
                throw new IllegalArgumentException("entity doesnt have pos comp");
            }
        }
    }
}

package DistanceFinder;

import Component.PosComp;
import Entity.Entity;
import Position.Pos;

import java.util.*;

public class DistanceFinder {

    public static <T extends Entity, S extends Entity> Optional<S> getClosestEntity(T e1, Collection<S> e2List, int range) {
        PriorityQueue<EntityDistance<S, T>> entities = getEntityDistanceQueue(e1, e2List);
        if (entities.peek() != null && entities.peek().getDistance() <= range) {
            return Optional.of(entities.peek().getEntity());
        } else {
            return Optional.empty();
        }
    }

    private static <T extends Entity, S extends Entity> PriorityQueue<EntityDistance<S, T>> getEntityDistanceQueue(T e1, Collection<S> e2List) {
        PriorityQueue<EntityDistance<S, T>> entityDistanceQueue = new PriorityQueue<>();
        for (S e2 : e2List) {
            entityDistanceQueue.add(DistanceFinder.getDistanceBetweenEntities(e2, e1));
        }
        return entityDistanceQueue;
    }

    public static <T extends Entity, S extends Entity> EntityDistance<T, S> getDistanceBetweenEntities(T e1, S e2) {
        checkEntitiesHavePosComps(e1, e2);
        Pos pos1 = e1.getComponent(PosComp.class).getPos();
        Pos pos2 = e2.getComponent(PosComp.class).getPos();
        double deltaX = pos1.getX() - pos2.getX();
        double deltaY = pos1.getY() - pos2.getY();
        return new EntityDistance<T, S>(e1, e2, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    private static void checkEntitiesHavePosComps(Entity... entities) {
        for (Entity entity : entities) {
            if (!entity.hasComponent(PosComp.class)) {
                throw new IllegalArgumentException("entity doesnt have pos comp");
            }
        }
    }
}

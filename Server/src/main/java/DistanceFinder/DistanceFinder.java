package DistanceFinder;

import Component.PosComp;
import Entity.Entity;
import Position.Pos;

import java.util.*;

public class DistanceFinder {

    /**
     * @return returns optional wrapper of closest entity to entity e1.
     * @param e1 is the entity which is the entity from which all distances are calculated from.
     * @param e2List is a collection of entities which will be considered in as entity population
     * @param range max range in which this algorithms will consider entities.
     */
    public static <T extends Entity, S extends Entity> Optional<S> getClosestEntity(T e1, Collection<S> e2List, int range) {
        PriorityQueue<EntityDistance<S, T>> entities = getEntityDistanceQueue(e1, e2List);//create priority queue to sort entities closest to furthest
        if (entities.peek() != null && entities.peek().getDistance() <= range) {//check closest entity is in range
            return Optional.of(entities.peek().getEntity());//return optional of entity with shortest distance
        } else {
            return Optional.empty();//return empty
        }
    }

    /**
     * @return priority queue of the entities of entity distance objects with entities of closest distance at the front of queue
     * @param e1 is the entity which is the entity from which all distances are calculated from.
     * @param e2List is a collection of entities which will be considered in as entity population
     */
    private static <T extends Entity, S extends Entity> PriorityQueue<EntityDistance<S, T>> getEntityDistanceQueue(T e1, Collection<S> e2List) {
        PriorityQueue<EntityDistance<S, T>> entityDistanceQueue = new PriorityQueue<>();
        for (S e2 : e2List) {
            entityDistanceQueue.add(DistanceFinder.getDistanceBetweenEntities(e2, e1));
        }
        return entityDistanceQueue;
    }

    //returns distance between 2 entities as an entity distance object
    public static <T extends Entity, S extends Entity> EntityDistance<T, S> getDistanceBetweenEntities(T e1, S e2) {
        checkEntitiesHavePosComps(e1, e2);
        Pos pos1 = e1.getComponent(PosComp.class).getPos();
        Pos pos2 = e2.getComponent(PosComp.class).getPos();
        double deltaX = pos1.getX() - pos2.getX();
        double deltaY = pos1.getY() - pos2.getY();
        //apply Pythagoras Theorem
        return new EntityDistance<T, S>(e1, e2, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    //throw error if an entity doesnt have a pos cop
    private static void checkEntitiesHavePosComps(Entity... entities) {
        for (Entity entity : entities) {
            if (!entity.hasComponent(PosComp.class)) {//method cant be used on an entity without position component
                throw new IllegalArgumentException("entity doesnt have pos comp");
            }
        }
    }
}

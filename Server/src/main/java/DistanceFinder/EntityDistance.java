package DistanceFinder;

import Entity.Entity;

public final class EntityDistance<T extends Entity, S extends Entity>  implements Comparable<EntityDistance<T, S>>{
    private final T entity1;
    private final S entity2;
    private final double distance;//distance between entity 1 and entity 2.

    public EntityDistance(T entity, S entity2, double distance) {
        this.entity1 = entity;
        this.entity2 = entity2;
        this.distance = distance;
    }

    @Override
    public int compareTo(EntityDistance entityDistance) {
        return Double.compare(distance, entityDistance.getDistance());
        //natural order sorts entity distance lowest to largest distance
    }

    public T getEntity() {
        return entity1;
    }

    public S getEntity2() {
        return entity2;
    }

    public double getDistance() {
        return distance;
    }
}

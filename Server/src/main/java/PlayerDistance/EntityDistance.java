package PlayerDistance;

import Entity.Entity;

public class EntityDistance <T> implements Comparable<EntityDistance>{
    private T entity1, entity2;
    private double distance;

    public EntityDistance(T entity, T entity2, double distance) {
        this.entity1 = entity;
        this.entity2 = entity2;
        this.distance = distance;
    }

    @Override
    public int compareTo(EntityDistance entityDistance) {
        return Double.compare(distance, entityDistance.getDistance());
    }

    public T getEntity() {
        return entity1;
    }

    public double getDistance() {
        return distance;
    }
}

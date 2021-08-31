package Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityDestination {
    private Entity entity;
    private List<Integer> destinationIDs;

    public EntityDestination(Entity entity, List<Integer> connectionIDs){
        this.entity = entity;
        this.destinationIDs = connectionIDs;
    }

    public Entity getEntity() {
        return entity;
    }

    public List<Integer> getDestinationIDs() {
        return destinationIDs;
    }
}

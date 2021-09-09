package State;

import Client.ClientOperator;
import Entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import System.*;

public abstract class State implements ClientOperator {

    private final Map<Class<? extends BaseSystem>, BaseSystem> systems = new HashMap<>();
    protected final List<Entity> entities = new CopyOnWriteArrayList<>();
    private boolean blocksUpdate = false;

    public State(boolean blocksUpdate) {
        this.blocksUpdate = blocksUpdate;
    }

    public void addSystem(BaseSystem system){
        this.systems.put(system.getClass(), system);
    }

    @SuppressWarnings("unchecked")
    public <T extends  BaseSystem> T getSystem( Class<T> system) {
        return  (T) systems.get(system);
    }

    public void clearSystems(){
        this.systems.clear();
    }

    public void update(){
        for (BaseSystem system: systems.values()){
            system.update();
        }
    }

    public abstract void init();
    protected abstract void startSystems();

    public List<Entity> getEntities() {
        return entities;
    }

    public boolean isBlocksUpdate() {
        return blocksUpdate;
    }

    public void setBlocksUpdate(boolean blocksUpdate) {
        this.blocksUpdate = blocksUpdate;
    }
}

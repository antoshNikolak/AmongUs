package State;

import Entity.Entity;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Packet.Position.InputRequest;
import System.*;
import Entity.*;

public abstract class State{

    private final Map<Class<? extends BaseSystem>, BaseSystem> systems = new HashMap<>();
    //maps class instance to its corresponding object
    protected final List<Entity> entities = new CopyOnWriteArrayList<>();
    //stores a thread safe list of all entities that exists in a state

    public void addSystem(BaseSystem system) {
        this.systems.put(system.getClass(), system);
    }
    public void removeSystem(Class<? extends BaseSystem> baseSystemClass) {
        this.systems.remove(baseSystemClass);
    }
    @SuppressWarnings("unchecked")
    public <T extends BaseSystem> T getSystem(Class<T> system) {
        return (T) systems.get(system);
    }
    public boolean hasSystem(Class<? extends BaseSystem> system) {
        return systems.containsKey(system);
    }
    public void clearSystems() {
        this.systems.clear();
    }

    public void update() {
        for (BaseSystem system : new HashSet<>(systems.values())) {
            //new hashset to remove concurrent sys modification
            system.update();
        }
    }

    public void processPlayingSystems(Player player, InputRequest packet) {
        for (BaseSystem system : new HashSet<>(systems.values())) {
            //new hashset to remove concurrent sys modification
            system.handleAction(player, packet);
        }
    }

    public abstract void init();//initialize state
    protected abstract void startSystems();//add all required systems of the state
    protected abstract void close();//perform actions to safely close state

    public List<Entity> getEntities() {
        return entities;
    }
}


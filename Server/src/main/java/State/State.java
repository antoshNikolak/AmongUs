package State;

import Client.ClientOperator;
import DistanceFinder.DistanceFinder;
import Entity.Entity;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Packet.Position.PosRequest;
import StartUpServer.AppServer;
import System.*;
import Entity.*;

public abstract class State{

    private final Map<Class<? extends BaseSystem>, BaseSystem> systems = new HashMap<>();
    protected final List<Entity> entities = new CopyOnWriteArrayList<>();

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

    public void processPlayingSystems(Player player, PosRequest packet) {
        for (BaseSystem system : new HashSet<>(systems.values())) {
            //new hashset to remove concurrent sys modification
            system.handleAction(player, packet);
        }
    }

    public abstract void init();
    protected abstract void startSystems();
    protected abstract void close();

    public List<Entity> getEntities() {
        return entities;
    }
}


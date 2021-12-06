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

public abstract class State implements ClientOperator {

    private final Map<Class<? extends BaseSystem>, BaseSystem> systems = new HashMap<>();
    private final List<BaseSystem> systemRemoveBuffer = new ArrayList<>();
    private final List<BaseSystem> addSystemBuffer = new ArrayList<>();
    protected final List<Entity> entities = new CopyOnWriteArrayList<>();


    public State() {
    }

    public void addSystem(BaseSystem system) {
        if (system.getClass() == EmergencyTableSystem.class) {
            System.out.println("adding system " + system.getClass() + " on thread: " + Thread.currentThread().getName());
        }
        this.systems.put(system.getClass(), system);
    }

    public void removeSystem(Class<? extends BaseSystem> baseSystemClass) {
        if (baseSystemClass == EmergencyTableSystem.class) {
            System.out.println("removing system " + baseSystemClass + " on thread: " + Thread.currentThread().getName());
        }
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
        for (BaseSystem system : new HashSet<>(systems.values())) {//new hashmap to remove concurrent sys modification
            system.update();
        }
    }

    public void processPlayingSystems(Player player, PosRequest packet) {
        for (BaseSystem system : new HashSet<>(systems.values())) {
            system.handleAction(player, packet);//this should exclude emergency system
        }
//        if (packet.isEmergencyMeetingKey() && !hasSystem(EmergencyTableSystem.class)) {
//            new EmergencyTableSystem().handleAction(player, packet);
////            EmergencyTableSystem emergencyTableSystem = new EmergencyTableSystem();
////            emergencyTableSystem.handleAction(player, packet);
//
//        }
    }

    public abstract void init();

    protected abstract void startSystems();

    public List<Entity> getEntities() {
        return entities;
    }

//    public boolean isBlocksUpdate() {
//        return blocksUpdate;
//    }
//
//    public void setBlocksUpdate(boolean blocksUpdate) {
//        this.blocksUpdate = blocksUpdate;
//    }


}


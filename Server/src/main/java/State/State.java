package State;

import Client.ClientOperator;
import Entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import Packet.Position.PosRequest;
import System.*;
import Entity.*;

public abstract class State implements ClientOperator {

    private final Map<Class<? extends BaseSystem>, BaseSystem> systems = new HashMap<>();
    protected final List<Entity> entities = new CopyOnWriteArrayList<>();


    public State() {}

    public void addSystem(BaseSystem system){
        this.systems.put(system.getClass(), system);
    }

    public void removeSystem(Class<? extends BaseSystem> baseSystemClass){
        this.systems.remove(baseSystemClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends  BaseSystem> T getSystem( Class<T> system) {
        return  (T) systems.get(system);
    }

    public boolean hasSystem( Class<? extends BaseSystem> system) {
        return  systems.containsKey(system);
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

//    public boolean isBlocksUpdate() {
//        return blocksUpdate;
//    }
//
//    public void setBlocksUpdate(boolean blocksUpdate) {
//        this.blocksUpdate = blocksUpdate;
//    }

    public void processInputSystems(Player player, PosRequest packet) {
        if (hasSystem(PhysicsSystem.class)) {
            getSystem(PhysicsSystem.class).processPlayerMove(player, packet);
        }
        if (hasSystem(ImposterActionsSystem.class)) {
            getSystem(ImposterActionsSystem.class).handleSpecialActions(player, packet);
        }
        if (hasSystem(CrewMateActionSystem.class)){
            getSystem(CrewMateActionSystem.class).handleSpecialAction(player, packet);
        }
        if (hasSystem(ReportBodySystem.class)){
            getSystem(ReportBodySystem.class).handleReport(player, packet);
        }
        if (hasSystem(TaskSystem.class)) {
            getSystem(TaskSystem.class).handleTaskAction(player, packet);
        }
    }

}

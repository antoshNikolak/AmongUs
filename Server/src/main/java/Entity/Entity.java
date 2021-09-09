package Entity;

import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import StartUpServer.AppServer;

import java.util.HashMap;
import java.util.Map;

import Component.*;

public abstract class Entity {

    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    protected Entity() {
        EntityRegistryServer.addEntity(this);
        AppServer.currentGame.getStateManager().getCurrentState().getEntities().add(this);
    }

    public ExistingEntityState adaptToEntityState() {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = EntityRegistryServer.getEntityID(this);
        return new ExistingEntityState(registrationID, posComp.getPos(), animationComp.getCurrentAnimationState(), animationComp.getCurrentAnimation().getIndex());
    }

    public NewAnimatedEntityState adaptToNewAnimatedEntityState() {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = EntityRegistryServer.getEntityID(this);
        return new NewAnimatedEntityState(registrationID, posComp.getPos(), animationComp.adaptToAllNewAnimations(), animationComp.getCurrentAnimationState());
    }

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> component) {
        return (T) components.get(component);
    }

    public int getComponentsSize() {
        return components.size();

    }

    public boolean hasComponent(Class<? extends Component> component) {
        return components.containsKey(component);
    }

    public void removeComponent(Class<? extends Component> component){
        this.components.remove(component);
    }

}

package Entity;

import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewLineState;
import Position.Pos;

import java.util.HashMap;
import java.util.Map;

import Component.*;
import Registry.RegistryHandler;

public abstract class Entity {

    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    protected Entity() {
        RegistryHandler.entityRegistryServer.addEntity(this);
    }

    public ExistingEntityState adaptToEntityState() {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = RegistryHandler.entityRegistryServer.getItemID(this);
        return new ExistingEntityState(registrationID, posComp.getPos(), animationComp.getCurrentAnimationState(), animationComp.getCurrentAnimation().getIndex());
    }

    public NewAnimatedEntityState adaptToNewAnimatedEntityState(boolean scrollable) {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = RegistryHandler.entityRegistryServer.getItemID(this);
        NewAnimatedEntityState newAnimatedEntityState = new NewAnimatedEntityState(registrationID, posComp.getPos(), animationComp.adaptToAllNewAnimations(), animationComp.getCurrentAnimationState());
        newAnimatedEntityState.setScrollable(scrollable);
        return newAnimatedEntityState;
    }

    public NewLineState adaptToNewLineState() {
        PosComp posComp = getComponent(PosComp.class);
        Pos endPos = new Pos(posComp.getPos().getX() + posComp.getWidth(), posComp.getPos().getY() + posComp.getHeight());
        return new NewLineState(posComp.getPos(), endPos, 10);

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

    public void removeComponent(Class<? extends Component> component) {
        this.components.remove(component);
    }

}

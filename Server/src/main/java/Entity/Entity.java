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

    //maps class wrapper of component to the component object
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    protected Entity() {
        RegistryHandler.entityRegistryServer.addEntity(this);
    }

    //return entity state of this entity which can be sent to client.
    public ExistingEntityState adaptToEntityState() {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = RegistryHandler.entityRegistryServer.getItemID(this);
        return new ExistingEntityState(registrationID, posComp.getPos(), animationComp.getCurrentAnimationState(), animationComp.getCurrentAnimation().getIndex());
    }

    //return new entity state of this entity which can be sent to client
    public NewAnimatedEntityState adaptToNewAnimatedEntityState(boolean scrollable) {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = RegistryHandler.entityRegistryServer.getItemID(this);
        NewAnimatedEntityState newAnimatedEntityState = new NewAnimatedEntityState(registrationID, posComp.getPos(), animationComp.adaptToAllNewAnimations(), animationComp.getCurrentAnimationState());
        newAnimatedEntityState.setScrollable(scrollable);
        return newAnimatedEntityState;
    }

    //return line state, based of this entity
    public NewLineState adaptToNewLineState() {
        PosComp posComp = getComponent(PosComp.class);
        Pos endPos = new Pos(posComp.getPos().getX() + posComp.getWidth(), posComp.getPos().getY() + posComp.getHeight());
        return new NewLineState(posComp.getPos(), endPos, 10);

    }

    //add new component to hashmap
    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    //returns component of type T
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> component) {
        return (T) components.get(component);
    }

    public int getComponentsSize() {
        return components.size();

    }

    //returns true if this entity has the component input as parameter
    public boolean hasComponent(Class<? extends Component> component) {
        return components.containsKey(component);
    }

    //removes component of type ? from hashmap
    public void removeComponent(Class<? extends Component> component) {
        this.components.remove(component);
    }

}

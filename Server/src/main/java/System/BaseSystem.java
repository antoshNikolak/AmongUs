package System;

import Component.Component;
import Entity.Entity;
import Packet.Position.InputRequest;
import Entity.Player;

import java.util.ArrayList;

public abstract class BaseSystem {

    public abstract void update();
    public abstract void handleAction(Player player, InputRequest packet);


    @SafeVarargs
    public final <T extends Class<? extends Component>> boolean checkEntityHasComponents(Entity entity, T... components){
        boolean hasComponents = true;
        for (T component: components){
            if (!entity.hasComponent(component)){
                hasComponents = false;
            }
        }
        return hasComponents;
    }
}

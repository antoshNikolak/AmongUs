package System;

import Component.Component;
import Entity.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseSystem {

    public abstract void update();

        public  <T extends Class<? extends Component>> boolean checkEntityHasComponents(Entity entity, ArrayList<T> components){
        boolean hasComponents = true;
        for (T component: components){
            if (!entity.hasComponent(component)){
                hasComponents = false;
            }
        }
        return hasComponents;
    }


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

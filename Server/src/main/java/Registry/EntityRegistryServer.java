package Registry;

import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Packet.Position.ClearEntityReturn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import StartUpServer.AppServer;
import Utils.*;
import Entity.DeadBody;

public class EntityRegistryServer extends Registry<Entity> {


    @Override
    public void removeEntity(int entityID) {
        Entity entity = IDMap.get(entityID);
        synchronized (AppServer.currentGame.getEntityReturnBuffer()) {//obtain entity buffer lock to mutate its attributes
            AppServer.currentGame.getEntityReturnBuffer().removeEntity(entity);//remove from buffer
            ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityID));//removes entity on the client side
            IDMap.remove(entityID);//remove entity from registry
        }

    }


}

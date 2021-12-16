package System;

import Component.PosComp;
import Entity.Player;
import Packet.Position.PosRequest;
import Packet.UserTag.UserTagState;
import Position.Pos;
import StartUpServer.AppServer;

public class NameTagSystem extends BaseSystem{
    @Override
    public void update() {
        for (Player player: AppServer.currentGame.getPlayers()){
            Pos pos = player.getComponent(PosComp.class).getPos();
            Pos tagPos = new Pos(pos);
            tagPos.incrementY(-10);
            UserTagState userTagState = new UserTagState(player.getNameTag(), tagPos);
            AppServer.currentGame.getEntityReturnBuffer().addUserTag(userTagState);
        }
    }

    @Override
    public void handleAction(Player player, PosRequest packet) {

    }
}

package Packet.NestedPane;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.NestedPane.AddsPane;
import Packet.NestedPane.NodeInfo;
import Packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class AddVotingPane   implements Packet, AddsPane {

    private int paneX, paneY, paneWidth, paneHeight;
    List<String> playerTextures = new ArrayList<>();

    public AddVotingPane(List<String> playerTextures, int paneX, int paneY, int paneWidth, int paneHeight) {
        this.playerTextures = playerTextures;
        this.paneX = paneX;
        this.paneY = paneY;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
    }

    private AddVotingPane() {
    }

    @Override
    public int getPaneWidth() {
        return paneWidth;
    }

    @Override
    public int getPaneHeight() {
        return paneHeight;
    }

    @Override
    public int getPaneX() {
        return paneX;
    }

    @Override
    public int getPaneY() {
        return paneY;
    }

    @Override
    public List<NewAnimatedEntityState> getNewEntityStates() {
        return new ArrayList<>();
    }

    @Override
    public List<NodeInfo> getNodes() {
        return new ArrayList<>();
    }

    public List<String> getPlayerTextures() {
        return playerTextures;
    }
}

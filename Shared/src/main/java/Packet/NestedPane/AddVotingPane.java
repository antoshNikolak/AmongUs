package Packet.NestedPane;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.Packet;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVotingPane implements Packet, AddsPane {

    public int paneX, paneY, paneWidth, paneHeight;
    public Map<String, String> textureNameTagMap = new HashMap<>();


    public AddVotingPane(Map<String, String> textureNameTagMap, int paneX, int paneY, int paneWidth, int paneHeight) {
        this.textureNameTagMap = textureNameTagMap;
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
    public Color getColour() {
        return Color.BLUE;
    }

    @Override
    public List<NewAnimatedEntityState> getNewEntityStates() {
        return new ArrayList<>();
    }

    @Override
    public List<NodeInfo> getNodes() {
        return new ArrayList<>();
    }

    public Map<String, String> getTextureNameTagMap() {
        return textureNameTagMap;
    }
}

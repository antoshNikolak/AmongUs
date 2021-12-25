package Packet.NestedPane;

import Packet.AddEntityReturn.AddEntityReturn;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.Packet;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class AddNestedPane implements AddsPane, Packet {

    private int paneWidth, paneHeight ,paneX, paneY;
    private List<NodeInfo> nodes = new ArrayList<>();
    private List<NewAnimatedEntityState> newEntityStates = new ArrayList<>();

    private AddNestedPane() {}//private constructor, use of builder pattern


    @Override
    public List<NodeInfo> getNodes() {
        return nodes;
    }

    @Override
    public List<NewAnimatedEntityState> getNewEntityStates() {
        return newEntityStates;
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
        return Color.WHITE;
    }

    public  static class Builder{
        private final int paneWidth, paneHeight ,paneX, paneY;
        private List<NodeInfo> nodes = new ArrayList<>();
        private List<NewAnimatedEntityState> newEntityStates = new ArrayList<>();

        public Builder(int paneX, int paneY, int paneWidth, int paneHeight){
            this.paneWidth = paneWidth;
            this.paneHeight = paneHeight;
            this.paneX = paneX;
            this.paneY = paneY;
        }

        public Builder withNodes(List<NodeInfo> nodes) {
            this.nodes.addAll(nodes);
            return this;
        }

        public Builder withNewEntityStates(List<NewAnimatedEntityState> newEntityStates) {
            this.newEntityStates.addAll(newEntityStates);
            return this;
        }

        public Builder withNode(NodeInfo nodeInfo){
            nodes.add(nodeInfo);
            return this;
        }

        public Builder withNewEntityState(NewAnimatedEntityState newEntityState){
            newEntityStates.add(newEntityState);
            return this;
        }

        public AddNestedPane build(){
            AddNestedPane addNestedPane = new AddNestedPane();
            addNestedPane.nodes = this.nodes;
            addNestedPane.newEntityStates = this.newEntityStates;
            addNestedPane.paneWidth = this.paneWidth;
            addNestedPane.paneHeight = this.paneHeight;
            addNestedPane.paneX = this.paneX;
            addNestedPane.paneY = this.paneY;
            return addNestedPane;
        }
    }
}

package Screen;

import Camera.Camera;
import EntityClient.Entity;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.NestedPane.NodeInfo;
import Packet.NestedPane.NodeType;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen extends Screen {
    private GameScreen nestedScreen;
    private Camera camera;
    private final List<Entity> entities;
    private final Map<NodeType, List<Node>> nodesMap;

    private GameScreen (Builder builder){
        super(builder.pane);
        this.nodesMap = builder.nodesMap;
        this.entities = builder.entities;
    }

    public void render() {
        if (nodesMap.containsKey(NodeType.CANVAS)) {
            GraphicsContext gc = ((Canvas) nodesMap.get(NodeType.CANVAS).get(0)).getGraphicsContext2D();//returns the same object that already exists
            gc.clearRect(0, 0, pane.getPrefWidth(), pane.getPrefHeight());//clears entire screen
            for (Entity entity : entities) {
                if (camera != null && entity.isScrollable()) {
                    entity.render(gc, camera);//entity rendered relative to top left corner of map
                } else{
                    entity.render(gc);//entity rendered relative to top left corner of screen
                }
            }
        }
        if (nestedScreen != null) {
            nestedScreen.render();
        }
    }

    public void setNestedScreen(GameScreen nestedPane) {
        if (nestedScreen!= null)removeNestedScreen();
        pane.getChildren().add(nestedPane.getPane());
        this.nestedScreen = nestedPane;
    }

    public void removeNestedScreen(){
        pane.getChildren().remove(nestedScreen.getPane());
        this.nestedScreen = null;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameScreen getNestedScreen() {
        return nestedScreen;
    }

    public  static class Builder{//use of builder pattern below
        private final Pane pane;
        private final List<Entity> entities = new CopyOnWriteArrayList<>();
        private final Map<NodeType, List<Node>> nodesMap = new HashMap<>();


        public Builder(Pane pane){
            this.pane = pane;
        }

        public Builder withNodes(List<NodeInfo> nodes) {
            for (NodeInfo nodeInfo: nodes){
                withNode(nodeInfo);
            }
            return this;
        }

        public Builder withNewEntityStates(List<NewAnimatedEntityState> newEntityStates) {
            for (NewAnimatedEntityState newEntityState: newEntityStates){
                withNewEntityState(newEntityState);
            }
            return this;
        }

        public Builder withNode(NodeInfo nodeInfo){
            switch (nodeInfo.getNodeType()){
                case CANVAS:
                    Canvas canvas = new Canvas(nodeInfo.getWidth(), nodeInfo.getHeight());
                    canvas.setLayoutX(nodeInfo.getX());
                    canvas.setLayoutY(nodeInfo.getY());
                    addNodeToMap(nodeInfo, canvas);
                    break;
                case LINE:
                    Line line = new Line(nodeInfo.getX(), nodeInfo.getY(), nodeInfo.getX() + nodeInfo.getWidth(), nodeInfo.getY() + nodeInfo.getHeight());
                    line.setStrokeWidth(nodeInfo.getLineWidth());
                    addNodeToMap(nodeInfo, line);
                    break;
                default:
                    return null;
            }
            return this;
        }

        private void addNodeToMap(NodeInfo nodeInfo, Node node){
            if (nodesMap.containsKey(nodeInfo.getNodeType())){
                nodesMap.get(nodeInfo.getNodeType()).add(node);
            }else {
                List <Node> nodeList = new ArrayList<>();
                nodeList.add(node);
                nodesMap.put(nodeInfo.getNodeType(), nodeList);
            }

        }

        public Builder withNewEntityState(NewAnimatedEntityState newEntityState){
            entities.add(new Entity(newEntityState));
            return this;
        }

        public GameScreen build(){
            for (List<Node> nodes: nodesMap.values()){
                for (Node node: nodes){
                    this.pane.getChildren().add(node);
                }
            }
            return new GameScreen(this);
        }


    }
}

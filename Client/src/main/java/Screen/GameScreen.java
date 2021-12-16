package Screen;

import Camera.Camera;
import ConnectionClient.ConnectionClient;
import EntityClient.Entity;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.NestedPane.ActionEvent;
import Packet.NestedPane.NodeInfo;
import Packet.NestedPane.NodeType;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

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
        this.entities = builder.entites;
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
        pane.getChildren().add(nestedPane.getPane());
        this.nestedScreen = nestedPane;
    }

    public void removeNestedScreen(){
        pane.getChildren().remove(nestedScreen.getPane());
        this.nestedScreen = null;
    }

//    public void setClearBoundaries(int x1, int y1, int width, int height) {
//        this.clearX1 = x1;
//        this.clearY1 = y1;
//        this.clearWidth = width;
//        this.clearHeight = height;
//    }

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

    public  static class Builder{
        private final Pane pane;
        private final List<Entity> entites = new CopyOnWriteArrayList<>();
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
//                case BUTTON:
//                    Button button = new Button(nodeInfo.getText());
//                    setNodeLayout(button, nodeInfo);
//                    button.setOnAction(event-> ConnectionClient.sendTCP(new ActionEvent(nodeInfo.getOnActionID())));
//                    addNodeToMap(nodeInfo, button);
//                    break;
                case TEXT_FIELD:
                    TextField textField = new TextField();
                    setNodeLayout(textField, nodeInfo);
                default:
                    return null;
            }
            return this;
        }

        private void setNodeLayout(Node node, NodeInfo nodeInfo){
            node.setLayoutX(nodeInfo.getX());
            node.setLayoutY(nodeInfo.getY());
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
            entites.add(new Entity(newEntityState));
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

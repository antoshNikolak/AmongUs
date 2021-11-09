package Packet.NestedPane;

public class NodeInfo  {//todo investigate
    private NodeType nodeType;
    private double x, y, width, height;
    private String text;
    private int lineWidth;
    private String onActionID;

    public NodeInfo(NodeType nodeType, double x, double y, double width, double height) {
        this.nodeType = nodeType;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private NodeInfo(){

    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOnActionID() {
        return onActionID;
    }

    public void setOnActionID(String onActionID) {
        this.onActionID = onActionID;
    }
}

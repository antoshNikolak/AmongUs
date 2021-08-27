package Component;

public class RenderComp implements Component {
    private String textureName;

    public RenderComp(String textureName) {
        this.textureName = textureName;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }
}

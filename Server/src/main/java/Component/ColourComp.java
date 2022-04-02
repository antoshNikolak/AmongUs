package Component;

public class ColourComp implements Component{

    private final String colour;//stores colour of player

    public ColourComp(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }
}

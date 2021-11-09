package Component;

import Position.Pos;

public class RespawnComp implements Component{
    private Pos pos;

    public RespawnComp(Pos pos) {
        this.pos = pos;
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }
}

package Packet.Camera;

import Packet.Packet;
import Position.Pos;

public class ScrollingEnableReturn implements Packet {
    private boolean scrollingEnabled ;
    private Pos pos;

    public ScrollingEnableReturn(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }

    public ScrollingEnableReturn( Pos pos) {
        this.scrollingEnabled = true;
        this.pos = pos;
    }

    private ScrollingEnableReturn(){}

    public boolean isScrollingEnabled() {
        return scrollingEnabled;
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }

    public Pos getPos() {
        return pos;
    }
}

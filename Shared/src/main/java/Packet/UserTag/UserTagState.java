package Packet.UserTag;

import Packet.Packet;
import Position.Pos;

public class UserTagState implements Packet {
    private String userName;
    private Pos pos;

    public UserTagState(String userName, Pos pos) {
        this.userName = userName;
        this.pos = pos;
    }

    private UserTagState() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

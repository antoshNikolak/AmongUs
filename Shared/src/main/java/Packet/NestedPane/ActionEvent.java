package Packet.NestedPane;

import Packet.Packet;

public class ActionEvent implements Packet {
    private String event;

    public ActionEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

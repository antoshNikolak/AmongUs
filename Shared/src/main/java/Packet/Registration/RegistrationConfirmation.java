package Packet.Registration;

import Packet.Packet;

public class RegistrationConfirmation implements Packet {

    private boolean authorized;
    private String failMessage;

    public RegistrationConfirmation() {
    }

    public RegistrationConfirmation(boolean authorized) {
        this.authorized = authorized;
    }

    public RegistrationConfirmation(String failMessage) {
        this.authorized = false;
        this.failMessage = failMessage;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getFailMessage() {
        return failMessage;
    }
}

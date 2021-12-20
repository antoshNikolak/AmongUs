package Packet.Registration;

import Packet.Packet;

public class RegistrationConfirmation implements Packet {
    public String failMessage = "";

    public RegistrationConfirmation() {}

    public RegistrationConfirmation(String failMessage) {
        this.failMessage = failMessage;
    }

    public boolean isAuthorized(){
        return failMessage.isEmpty();
    }


}

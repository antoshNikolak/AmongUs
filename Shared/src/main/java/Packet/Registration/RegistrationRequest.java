package Packet.Registration;

import Packet.Packet;
import Packet.UserData.UserData;

public abstract class RegistrationRequest implements Packet {
    private UserData userData;

    public RegistrationRequest(UserData userData) {
        this.userData = userData;
    }

    public RegistrationRequest() {
    }

    public UserData getUserData() {
        return userData;
    }
}

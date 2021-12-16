package Packet.Registration;

import Packet.UserData.UserData;

public class LoginRequest extends RegistrationRequest {
    public LoginRequest(UserData userData) {
        super(userData);
    }

    public LoginRequest() {
    }
}

package Packet.UserData;

import java.util.Objects;
//todo document over riding equals and making class immutable for more safety
public final class UserData {
    private String userName;
    private String password;

    public UserData(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    private UserData() {}

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return userName.equals(userData.userName) && password.equals(userData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }
}

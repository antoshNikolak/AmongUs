package Packet.UserData;

public class UserData {

    String userName;
    String password;

    public UserData(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public UserData() {}

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }




}

package StartUp;

import java.net.URL;
import java.net.URLClassLoader;

public class FakeMain {//fake main required for creating a jar file, as main class cannot extend Application
    public static void main(String[] args) {
        AppClient.main(args);
    }
}

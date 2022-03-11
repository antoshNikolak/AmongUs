package StartUp;

import java.net.URL;
import java.net.URLClassLoader;

public class FakeMain {//for creating a jar file
    public static void main(String[] args) {
//        ClassLoader cl = ClassLoader.getSystemClassLoader();
//
//        URL[] urls = ((URLClassLoader)cl).getURLs();
//
//        for(URL url: urls){
//            System.out.println(url.getFile());
//        }
//        System.out.println("class path: " +System.getProperty("java.class.path").replace(":", "\n"));
        AppClient.main(args);
    }
}

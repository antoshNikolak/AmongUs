package Screen;

import Packet.NestedPane.NodeInfo;
import Packet.NestedPane.NodeType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class ScreenManager {
    private final static Map<Class<? extends Screen>, Screen> screens = new HashMap<>();
    private static Screen currentScreen;
    private static Scene scene;

    static { //todo metntion refactoring, and this is more efficient in terms of performance, less scalable
//        List<Class<? extends Screen>> screens = getScreenClassObjects();
//        loadScreens(screens);
        Pane loginPane = loadPane("LoginScreen");
        addScreen(new LoginScreen(loginPane));

        Pane menuPane = loadPane("MenuScreen");
        addScreen(new MenuScreen(menuPane));

        Pane entryPane = loadPane("EntryScreen");
        addScreen(new EntryScreen(entryPane));

        Pane gamePane = loadPane("GameScreen");
        addScreen(new GameScreen.Builder(gamePane).withNode(new NodeInfo(NodeType.CANVAS, 0, 0, 600, 400)).build());
    }

    private static Pane loadPane(String paneName) {
        try {
            return new FXMLLoader().load(Thread.currentThread().getContextClassLoader().
                    getResourceAsStream("Screen/" + paneName + ".fxml"));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error loading: "+ paneName);
            return null;
        }
        }

        private static List<Class<? extends Screen>> getScreenClassObjects () {
            return Arrays.asList(EntryScreen.class, LoginScreen.class, MenuScreen.class, GameScreen.class);
        }

        private static void loadScreens (List < Class < ? extends Screen >> screens){
            for (Class<? extends Screen> screen : screens) {
                try {
                    loadScreen(screen);
                } catch (IOException e) {
                    System.out.println("Error loading: " + screen.getName());
                    e.printStackTrace();
                }
            }
        }

        private static void loadScreen (Class < ? extends Screen > screenClass) throws IOException {
            Pane pane = new FXMLLoader().load(Thread.currentThread().getContextClassLoader().
                    getResourceAsStream("Screen/" + screenClass.getSimpleName() + ".fxml"));
            Screen screen = createInstanceFromClass(screenClass, pane);
            addScreen(screen);
        }

//    private static Pane createPane(String paneName){
//        try {
//            return new FXMLLoader().load(Thread.currentThread().getContextClassLoader().
//                    getResourceAsStream("Screen/" + paneName + ".fxml"));
//        } catch (IOException e) {
//            System.out.println("error loading screen: "+ paneName);
//            e.printStackTrace();
//            return null;
//        }
//
//    }

        private static Screen createInstanceFromClass (Class < ? extends Screen > screenClass, Pane pane){
            try {
                return screenClass.getDeclaredConstructor(Pane.class).newInstance(pane);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void activate (Class < ? extends Screen > screen){
            Screen entry = screens.get(screen);
            currentScreen = entry;
            scene.setRoot(entry.getPane());
        }

        public static void addScreen (Screen screen){
            screens.put(screen.getClass(), screen);
        }

//    public static Node getNode(Class<? extends Screen> screen, String node) {
//        return screens.get(screen).getPane().lookup("#" + node);
//    }

        public static Screen getCurrentScreen () {
            return currentScreen;
        }

        @SuppressWarnings("unchecked")
        public static <T extends Screen > T getScreen(Class < T > screen) {
            return (T) screens.get(screen);
        }

        public static Scene getScene () {
            return scene;
        }

        public static void setScene (Scene _scene){
            scene = _scene;
        }

    }

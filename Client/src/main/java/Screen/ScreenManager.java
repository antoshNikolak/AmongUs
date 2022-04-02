package Screen;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.NodeInfo;
import Packet.NestedPane.NodeType;
import Packet.ScreenData.ScreenInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class ScreenManager {
    private final static Map<Class<? extends Screen>, Screen> screens = new HashMap<>();
    private static Screen currentScreen;
    private static Scene scene;

    public static final int STAGE_WIDTH = 800;
    public static final int STAGE_HEIGHT = 450;


    static {
        //notify server of resolution being played on
        ConnectionClient.sendTCP(new ScreenInfo(STAGE_WIDTH, STAGE_HEIGHT));

        //load all screen which will be used later in the game
        Pane loginPane = loadPane("LoginScreen");
        addScreen(new LoginScreen(loginPane));

        Pane menuPane = loadPane("MenuScreen");
        addScreen(new MenuScreen(menuPane));

        Pane entryPane = loadPane("EntryScreen");
        addScreen(new EntryScreen(entryPane));

        Pane crewWinPane = loadPane("CrewWinScreen");
        addScreen(new CrewWinScreen(crewWinPane));

        Pane impostorWinPane = loadPane("ImpostorWinScreen");
        addScreen(new ImpostorWinScreen(impostorWinPane));

        Pane leaderBoardPane = loadPane("LeaderBoardScreen");
        addScreen(new LeaderBoardScreen(leaderBoardPane));

        Pane gamePane = loadPane("GameScreen");
        addScreen(new GameScreen.Builder(gamePane).withNode(new NodeInfo(NodeType.CANVAS, 0, 0, STAGE_WIDTH, STAGE_HEIGHT)).build());
    }

    private static Pane loadPane(String paneName) {
        try {
            return new FXMLLoader().load(Thread.currentThread().getContextClassLoader().
                    getResourceAsStream("Screen/" + paneName + ".fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error loading: " + paneName);
            return null;
        }
    }

    private static Screen createInstanceFromClass(Class<? extends Screen> screenClass, Pane pane) {
        try {
            return screenClass.getDeclaredConstructor(Pane.class).newInstance(pane);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void activate(Class<? extends Screen> screen) {
        //show screen to the one input
        Screen entry = screens.get(screen);
        currentScreen = entry;
        scene.setRoot(entry.getPane());
    }

    public static void addScreen(Screen screen) {
        screens.put(screen.getClass(), screen);
    }


    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Screen> T getScreen(Class<T> screen) {
        return (T) screens.get(screen);
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene _scene) {
        scene = _scene;
    }

}

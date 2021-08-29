package PlayerColourManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayerColourFactory {
    private static List<String> availableColours = new ArrayList<>();

    static {
        String [] playerColours = new String[]{"green", "blue", "orange", "cyan", "yellow", "pink", "red"};
        availableColours = new ArrayList<>(Arrays.asList(playerColours));
    }

    public static String getRandomColour(){
        int index = new Random().nextInt(availableColours.size());
        String colour = availableColours.get(index);
        availableColours.remove(index);
        return colour;
    }
}

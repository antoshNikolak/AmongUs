package State;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class StateManager {

    private final Deque<GameState> states = new LinkedList<>();
    //todo use a stack probably


    public void updateTop(){
        states.getLast().update();
    }

    public void update(){
        for (GameState state : states) {
            state.update();
        }
    }

    public void pushState(GameState state){
        states.add(state);
        state.init();
    }

    public void popState(){
        states.pop().close();
    }

    public GameState getCurrentState(){
        return states.peek();
    }
}

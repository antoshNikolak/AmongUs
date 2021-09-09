package State;

import java.util.*;

public class StateManager {

    private final Stack<GameState> states = new Stack<>();//wont allow duplicates
    
    public void updateTop() {
        states.peek().update();
    }

    public void update() {
        ListIterator<GameState> stateItr = states.listIterator(states.size());
        while (stateItr.hasPrevious()){
            GameState state = stateItr.previous();
            if (!state.isBlocksUpdate()){
                state.update();
            }else {
                break;
            }
        }
    }

    public void pushState(GameState state) {
        if (states.contains(state)) return;
        states.add(state);
        state.init();
    }

    public void popState() {
        states.pop().close();
    }

    public GameState getCurrentState() {
        return states.peek();
    }

    @SuppressWarnings("unchecked")
    public <T extends State> T getState(Class<T> stateClass) {
        for (State state : states) {
            if (state.getClass().isAssignableFrom(stateClass)) {
                return (T) state;
            }
        }
        return null;
    }
}

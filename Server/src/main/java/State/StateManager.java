package State;

import java.util.*;

public class StateManager {

    private final Stack<PlayingState> states = new Stack<>();//wont allow duplicates

    public synchronized void updateTop() {
        states.peek().update();
    }

    public synchronized void update() {
        ListIterator<PlayingState> stateItr = states.listIterator(states.size());
        while (stateItr.hasPrevious()) {
            PlayingState state = stateItr.previous();
            state.update();
        }
    }

    public void pushState(PlayingState state) {
        if (states.contains(state)) return;
        synchronized (this) {
            states.add(state);
        }
        state.init();
    }

    public synchronized void popState() {
        states.pop().close();
    }

    public synchronized boolean hasState(Class<? extends PlayingState> playingStateClass) {
        for (PlayingState playingState : states) {
            if (playingStateClass.isAssignableFrom(playingState.getClass())) {
                return true;
            }
        }
        return false;
    }


    public synchronized PlayingState getCurrentState() {
        return states.peek();
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends State> T getState(Class<T> stateClass) {
        for (State state : states) {
            if (state.getClass().isAssignableFrom(stateClass)) {
                return (T) state;
            }
        }
        return null;
    }
}

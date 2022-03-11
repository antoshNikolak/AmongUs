package State;

import java.util.*;

public class StateManager {
    private final Stack<PlayingState> states = new Stack<>();//wont allow duplicates

    public synchronized void updateTop() {
        states.peek().update();//updates playing state on top of the stack
    }

    public void pushState(PlayingState state) {
        synchronized (this) {
            if (states.contains(state)) return;
            states.add(state);//pushes a new playing state on top of the stack
        }
        state.init();//invoke init method of playing state so it is initialized properly
    }

    //removes the playing state on top of the class, invoking the
    // the states close method
    public synchronized void popState() {
        states.pop().close();

    }

    //returns true if playing state exists in the class that user entered
    public synchronized boolean hasState(Class<? extends PlayingState> playingStateClass) {
        for (PlayingState playingState : states) {
            if (playingStateClass.isAssignableFrom(playingState.getClass())) {
                return true;
            }
        }
        return false;
    }

    public synchronized PlayingState getTopState() {
        return states.peek();
    }

    //returns playing state object, of the class object entered, if exists in the stack.
    //if doesnt exist, return null
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

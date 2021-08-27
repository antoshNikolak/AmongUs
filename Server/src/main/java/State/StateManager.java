package State;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class StateManager {

    private final Deque<State> states = new LinkedList<>();
    //todo use a stack probably


    public void updateTop(){
        states.getLast().update();
    }

    public void update(){
        for (State state : states) {
            state.update();
        }
    }

    //        ListIterator<State> stateItr = states.listIterator(states.size());

    public void pushState(State state){
        states.add(state);
        state.init();
    }

    public void popState(){
        states.pop();
    }

    public State getCurrentState(){
        return states.peek();
    }
}

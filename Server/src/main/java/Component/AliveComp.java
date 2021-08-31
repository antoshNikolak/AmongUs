package Component;

public class AliveComp implements  Component{
    private boolean isAlive;

    public AliveComp(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}

package Component;

public class ImpostorComp implements Component{
    private boolean ableToKill = true;

    public ImpostorComp() {
    }

    public boolean isAbleToKill() {
        return ableToKill;
    }

    public void setAbleToKill(boolean ableToKill) {
        this.ableToKill = ableToKill;
    }
}

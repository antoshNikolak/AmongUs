package Component;

public class ImposterComp implements Component{
    private boolean ableToKill = true;

    public ImposterComp() {
    }

    public boolean isAbleToKill() {
        return ableToKill;
    }

    public void setAbleToKill(boolean ableToKill) {
        this.ableToKill = ableToKill;
    }
}

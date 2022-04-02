package Component;

public class ImpostorComp implements Component{
    private boolean ableToKill = true;//true if impostor can kill, false if cant due to cool down

    public ImpostorComp() {
    }

    public boolean isAbleToKill() {
        return ableToKill;
    }

    public void setAbleToKill(boolean ableToKill) {
        this.ableToKill = ableToKill;
    }
}

package AnimationFactory;

import Packet.Animation.AnimationDisplayReturn;
import Position.Pos;

public class AnimationFactory {

    public static AnimationDisplayReturn createAnimationDisplayReturn(String texture) {
        if (texture.equals("dead-body-reported")) {
            return new AnimationDisplayReturn("dead-body-reported", new Pos(0, 200), 600, 200, 2000);
        }else if (texture.equals("emergency-meeting")){
            return new AnimationDisplayReturn("emergency-meeting", new Pos(0, 200), 600, 200, 2000);
        }
        return null;

    }
}

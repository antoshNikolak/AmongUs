package ConnectionClient;

import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class PacketListenerClient extends Listener {
    private final  PacketControllerClient packetController = new PacketControllerClient();

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof RegistrationConfirmation){
            packetController.handleRegistrationConfirmation((RegistrationConfirmation)object);
        }else if (object instanceof StartGameReturn){
            packetController.handleStartGameReturn((StartGameReturn)object);
        }else if(object instanceof AddLineReturn){
            packetController.handleAddLineReturn((AddLineReturn) object);
        }else if (object instanceof AddNestedPane){
            packetController.handleAddNestedPane((AddNestedPane)object);
        }else if (object instanceof AddLocalEntityReturn){
            packetController.handleAddLocalEntityReturn((AddLocalEntityReturn)object);
        }else if (object instanceof AddChangingEntityReturn){
            packetController.handleAddChangingEntityReturn((AddChangingEntityReturn) object);
        } else if (object instanceof AddStationaryEntityReturn){
            packetController.handleNewStationaryEntityReturn((AddStationaryEntityReturn)object);
        }else if(object instanceof StateReturn){
            packetController.handleStateReturn((StateReturn) object);
        }else if (object instanceof GameStartTimer){
            packetController.handleGameStartTimerReturn((GameStartTimer)object);
        }else if (object instanceof KillCoolDownTimer){
            packetController.handleKillCoolDownTimer((KillCoolDownTimer) object);
        } else if (object instanceof ClearEntityReturn){
            packetController.handleClearEntityReturn((ClearEntityReturn) object);
        }else if (object instanceof ScrollingEnableReturn){
            packetController.handleScrollingEnableReturn((ScrollingEnableReturn)object);
        }else if (object instanceof RemoveNestedScreen){
            packetController.removeNestedScreen();
        }
    }

}

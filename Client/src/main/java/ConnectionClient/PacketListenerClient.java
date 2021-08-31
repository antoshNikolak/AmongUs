package ConnectionClient;

import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Timer.GameStartTimerReturn;
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
        }else if (object instanceof AddLocalEntityReturn){
            packetController.handleAddLocalEntityReturn((AddLocalEntityReturn)object);
        }else if (object instanceof AddChangingEntityReturn){
            packetController.handleAddChangingEntityReturn((AddChangingEntityReturn) object);
        } else if (object instanceof AddStationaryEntityReturn){
            packetController.handleNewStationaryEntityReturn((AddStationaryEntityReturn)object);
        }else if(object instanceof StateReturn){
            packetController.handleStateReturn((StateReturn) object);
        }else if (object instanceof GameStartTimerReturn){
            packetController.handleGameStartTimerReturn((GameStartTimerReturn)object);
        }else if (object instanceof ClearEntityReturn){
            packetController.handleClearWorldReturn((ClearEntityReturn) object);
        }
    }

}

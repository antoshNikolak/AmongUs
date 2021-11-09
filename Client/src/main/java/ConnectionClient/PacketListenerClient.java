package ConnectionClient;

import Animation.AnimationDisplayReturn;
import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.GameStart.StartGameReturn;
import Packet.NestedPane.*;
import Packet.Position.*;
import Packet.Registration.RegistrationConfirmation;
import Packet.Sound.CloseRecordHandler;
import Packet.Sound.OpenRecordHandler;
import Packet.Sound.Sound;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Packet.Timer.VotingTimer;
import SudokuPacket.VerifySudokuReturn;
import Voting.ElectionReturn;
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
        }else if (object instanceof AddEntityReturn){
            packetController.handleAddEntityReturn((AddEntityReturn) object);
        }
//        else if (object instanceof AddChangingEntityReturn){
//            packetController.handleAddChangingEntityReturn((AddChangingEntityReturn) object);
//        } else if (object instanceof AddStationaryEntityReturn){
//            packetController.handleNewStationaryEntityReturn((AddStationaryEntityReturn)object);
//        }
        else if(object instanceof StateReturn){
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
        } else if (object instanceof AddSudokuPane){
            packetController.handleAddSudokuPane((AddSudokuPane) object);
        } else if (object instanceof VerifySudokuReturn){
            packetController.handleVerifySudokuReturn((VerifySudokuReturn)object);
        }else if (object instanceof AnimationDisplayReturn){
            packetController.handleAnimationDisplayReturn((AnimationDisplayReturn)object);
        } else if (object instanceof AddVotingPane){
            packetController.handleAddVotingPane((AddVotingPane) object);
        }else if (object instanceof Sound){
            packetController.handleSound((Sound) object);
        }else if (object instanceof OpenRecordHandler){
            packetController.handleOpenMicAndSpeaker();
        }else if (object instanceof CloseRecordHandler){
            packetController.handleCloseMicAndSpeaker();
        }else if (object instanceof ElectionReturn){
//            packetController.handleElectionReturn((ElectionReturn) object);
        }else if (object instanceof RemoveVotingScreen){
            packetController.handleRemoveVotingScreen((RemoveVotingScreen) object);
        }else if (object instanceof VotingTimer){
            packetController.handleVotingTimer((VotingTimer) object);
        }else if (object instanceof TaskBarUpdate){
            packetController.handleTaskBarUpdate((TaskBarUpdate) object);
        }
    }

}

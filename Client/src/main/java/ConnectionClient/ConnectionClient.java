package ConnectionClient;

import Packet.Animation.AnimState;
import Packet.Animation.AnimationDisplayReturn;
import Packet.Animation.AnimationOver;
import Packet.*;
import Packet.AddEntityReturn.*;
import Packet.Animation.NewAnimationReturn;
import Packet.Camera.ScrollingEnableReturn;
import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.ImpostorWin;
import Packet.GameStart.RoleNotify;
import Packet.GameStart.StartGameRequest;
import Packet.GameStart.StartGameReturn;
import Packet.NestedPane.*;
import Packet.Position.*;
import Packet.Registration.LoginRequest;
import Packet.Registration.LogoutRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
import Packet.Sound.CloseRecordHandler;
import Packet.Sound.OpenRecordHandler;
import Packet.Sound.Sound;
import Packet.SudokuPacket.SudokuFailedReturn;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Packet.Timer.Timer;
import Packet.Timer.VotingTimer;
import Position.Pos;
import Packet.SudokuPacket.VerifySudokuRequest;
import Packet.SudokuPacket.VerifySudokuReturn;
import Packet.UserData.UserData;
import Packet.Voting.ElectionReturn;
import Packet.Voting.ImpostorVote;
import Packet.Voting.VoteOption;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;


public final class ConnectionClient {
    private final static Client client = new Client(1000000, 1000000);

    public static void sendTCP(Packet packet) {
        client.sendTCP(packet);
    }

    public static void sendUDP(Packet packet) {
        client.sendUDP(packet);
    }

    static {

//        Log.set(Log.LEVEL_DEBUG);//print all level above and including debug
        startConnection();
        registerPackets();
        client.addListener(new PacketListenerClient());
    }

    private static void startConnection() {
        try {
            client.start();
//            client.connect(5000, "localhost", 49159, 65520);
            client.connect(5000, "192.168.1.73", 49159, 65520);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void registerPackets() {
        Kryo kryo = client.getKryo();
        kryo.register(LoginRequest.class);
        kryo.register(SignupRequest.class);
        kryo.register(RegistrationConfirmation.class);
        kryo.register(UserData.class);
        kryo.register(StartGameRequest.class);
        kryo.register(StartGameReturn.class);
        kryo.register(StateReturn.class);
        kryo.register(PosRequest.class);
        kryo.register(Pos.class);
//        kryo.register(AddStationaryEntityReturn.class);
        kryo.register(ExistingEntityState.class);
        kryo.register(ArrayList.class);
        kryo.register(AddLocalEntityReturn.class);
//        kryo.register(AddChangingEntityReturn.class);
        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(NewEntityState.class);
        kryo.register(String[].class);
        kryo.register(NewAnimationReturn[].class);
        kryo.register(NewAnimationReturn.class);
        kryo.register(AnimState.class);
        kryo.register(Timer.class);
        kryo.register(ClearEntityReturn.class);
        kryo.register(GameStartTimer.class);
        kryo.register(KillCoolDownTimer.class);
        kryo.register(ScrollingEnableReturn.class);
        kryo.register(NewAnimatedEntityState.class);
        kryo.register(AddLineReturn.class);
        kryo.register(NewLineState.class);
        kryo.register(AddNestedPane.class);
        kryo.register(RemoveNestedScreen.class);
        kryo.register(AddsPane.class);
        kryo.register(AddSudokuPane.class);
        kryo.register(int[][].class);
        kryo.register(int[].class);
        kryo.register(VerifySudokuReturn.class);
        kryo.register(VerifySudokuRequest.class);
        kryo.register(Integer[][].class);
        kryo.register(Integer[].class);
        kryo.register(Integer.class);
        kryo.register(AnimationDisplayReturn.class);
        kryo.register(NodeInfo.class);
        kryo.register(NodeType.class);
        kryo.register(OpenRecordHandler.class);
        kryo.register(CloseRecordHandler.class);
        kryo.register(Sound.class);
        kryo.register(AddVotingPane.class);
        kryo.register(byte[].class);
        kryo.register(ImpostorVote.class);
        kryo.register(VotingTimer.class);
        kryo.register(ElectionReturn.class);
        kryo.register(DisplayVoteResults.class);
        kryo.register(HashMap.class);
        kryo.register(AnimationOver.class);
        kryo.register(TaskBarUpdate.class);
        kryo.register(AddEntityReturn.class);
        kryo.register(VoteOption.class);
        kryo.register(LogoutRequest.class);
        kryo.register(RoleNotify.class);
        kryo.register(CrewWin.class);
        kryo.register(ImpostorWin.class);
        kryo.register(ScreenInfo.class);
        kryo.register(SudokuFailedReturn.class);
    }
}

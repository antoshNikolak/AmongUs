package ConnectionClient;

import Packet.Animation.AnimState;
import Packet.Animation.AnimationDisplayReturn;
import Packet.Animation.AnimationOver;
import Packet.AddEntityReturn.*;
import Packet.Animation.NewAnimationReturn;
import Packet.Camera.ScrollingEnableReturn;
import Packet.CountDown.RemoveCountDown;
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
import Packet.Packet;
import Packet.Position.*;
import Packet.Registration.LoginRequest;
import Packet.Registration.LogoutRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
//import Packet.Sound.CloseRecordHandler;
import Packet.Sound.Sound;
import Packet.SudokuPacket.SudokuFailedReturn;
//import Packet.Timer.GameStartCountDown;
//import Packet.Timer.KillCoolDownCountDown;
//import Packet.Timer.CountDown;
//import Packet.Timer.VotingCountDown;
import Packet.CountDown.CountDown;
import Packet.Voting.*;
import Position.Pos;
import Packet.SudokuPacket.VerifySudokuRequest;
import Packet.SudokuPacket.VerifySudokuReturn;
import Packet.UserData.UserData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import Packet.LeaderBoard.*;

import java.io.IOException;
import java.util.*;
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
        startConnection();
        registerPackets();
        client.addListener(new PacketListenerClient());
    }

    private static void startConnection() {
        try {
            client.start();//start client thread
            client.connect(5000, "localhost", 49158, 65521);//Opens a TCP and UDP client
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
        kryo.register(InputRequest.class);
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
        kryo.register(CountDown.class);
        kryo.register(ClearEntityReturn.class);
//        kryo.register(GameStartCountDown.class);
//        kryo.register(KillCoolDownCountDown.class);
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
        kryo.register(Sound.class);
        kryo.register(AddVotingPane.class);
        kryo.register(byte[].class);
        kryo.register(ImpostorVote.class);
//        kryo.register(VotingCountDown.class);
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
        kryo.register(ChatMessageReturn.class);
        kryo.register(ChatMessageRequest.class);
        kryo.register(LeaderBoardReturn.class);
        kryo.register(RequestLeaderBoard.class);
        kryo.register(TreeMap.class);
        kryo.register(LinkedHashMap.class);
        kryo.register(RemoveCountDown.class);

    }
}

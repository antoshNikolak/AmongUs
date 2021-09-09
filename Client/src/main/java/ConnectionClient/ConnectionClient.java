package ConnectionClient;

import Animation.AnimState;
import Packet.*;
import Packet.AddEntityReturn.*;
import Packet.Camera.ScrollingEnableReturn;
import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.GameStart.StartGameRequest;
import Packet.GameStart.StartGameReturn;
import Packet.Position.*;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.Timer.GameStartTimer;
import Packet.Timer.KillCoolDownTimer;
import Packet.Timer.Timer;
import Position.Pos;
import UserData.UserData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.ArrayList;
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
        startConnection();
        registerPackets();
        client.addListener(new PacketListenerClient());
    }

    private static void startConnection() {
        try {
            client.start();
            client.connect(5000, "localhost", 49158, 65529);
//            client.connect(5000, "192.168.1.73", 49158, 65529);
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
        kryo.register(AddStationaryEntityReturn.class);
        kryo.register(ExistingEntityState.class);
        kryo.register(ArrayList.class);
        kryo.register(AddLocalEntityReturn.class);
        kryo.register(AddChangingEntityReturn.class);
        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(NewEntityState.class);
        kryo.register(String[].class);
        kryo.register(Animation.NewAnimationReturn[].class);
        kryo.register(Animation.NewAnimationReturn.class);
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

    }
}

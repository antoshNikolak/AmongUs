module Shared {
    exports Packet;
    exports UserData;
    exports Packet.Registration;
    exports Packet.Position;
    exports Packet.GameStart;
    exports Position;
    opens Packet;
    opens Packet.Registration;
    opens Packet.GameStart;
    opens UserData;
    opens Packet.Position;
    opens Position;
    exports Animation;
    opens Animation;
    exports Packet.Timer;
    opens Packet.Timer;


}
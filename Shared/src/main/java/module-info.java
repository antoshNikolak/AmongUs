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
    exports Packet.Camera;
    opens Packet.Camera;
    exports Packet.EntityState;
    opens Packet.EntityState;
    exports Packet.AddEntityReturn;
    opens Packet.AddEntityReturn;
    exports SudokuPacket;
    opens SudokuPacket;
    exports Packet.NestedPane;
    opens Packet.NestedPane;
    exports Packet.Sound;
    opens Packet.Sound;
    exports Voting;
    opens Voting;
    exports Packet.GameEnd;


}
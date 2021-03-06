module Shared {
    requires javafx.graphics;
    exports Packet;
    exports Packet.UserData;
    exports Packet.Registration;
    exports Packet.Position;
    exports Packet.GameStart;
    exports Position;
    opens Packet;
    opens Packet.Registration;
    opens Packet.GameStart;
    opens Packet.UserData;
    opens Packet.Position;
    opens Position;
    exports Packet.Animation;
    opens Packet.Animation;
    exports Packet.CountDown;
    opens Packet.CountDown;
    exports Packet.Camera;
    opens Packet.Camera;
    exports Packet.EntityState;
    opens Packet.EntityState;
    exports Packet.AddEntityReturn;
    opens Packet.AddEntityReturn;
    exports Packet.SudokuPacket;
    opens Packet.SudokuPacket;
    exports Packet.NestedPane;
    opens Packet.NestedPane;
    exports Packet.Sound;
    opens Packet.Sound;
    exports Packet.Voting;
    opens Packet.Voting;
    exports Packet.GameEnd;
    exports Packet.ScreenData;
    exports Utils;
    exports Packet.LeaderBoard;

}
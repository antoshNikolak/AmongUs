package Packet.Voting;

public enum VoteOption {
    PLAYER_GREEN{
        @Override
        public String getColour() {
            return "green";
        }
    },
    PLAYER_ORANGE{
        @Override
        public String getColour() {
            return "orange";
        }
    },
    PLAYER_RED{
        @Override
        public String getColour() {
            return "red";
        }
    },
    PLAYER_CYAN{
        @Override
        public String getColour() {
            return "cyan";
        }
    },
    PLAYER_BLUE{
        @Override
        public String getColour() {
            return "blue";
        }
    },
    PLAYER_PINK{
        @Override
        public String getColour() {
            return "pink";
        }
    },
    PLAYER_YELLOW{
        @Override
        public String getColour() {
            return "yellow";
        }
    },
    SKIP{
        @Override
        public String getColour() {
            return null;
        }
    };

    public abstract String getColour();

}

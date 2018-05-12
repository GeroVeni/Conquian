package dummies.conquian.database;

public class ConquianDbSchema {
    public static final class GameTable {
        public static final String NAME = "games";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String DATE = "date";
        }
    }

    public static final class PlayerTable {
        public static final String NAME = "players";

        public static final class Cols {
            public static final String GAME_UUID = "game";
            public static final String NAME = "name";
            public static final String SCORE = "score";
            public static final String HATS = "hats";
            public static final String CROWNS = "crowns";
            public static final String ACTIVE = "active";
        }
    }
}

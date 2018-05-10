package dummies.conquian;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameBase {
    private static GameBase sGameBase;

    public static final int MAX_PLAYERS = 3;

    private Context mContext;

    private List<Game> mGames;

    public static GameBase get(Context context) {
        if (sGameBase == null) {
            sGameBase = new GameBase(context);
        }
        return sGameBase;
    }

    private GameBase(Context context) {
        mContext = context.getApplicationContext();

        mGames = new ArrayList<>();
    }

    public List<Game> getGames() {
        return mGames;
    }

    public Game getGame(UUID gameId) {
        for (Game game : mGames) {
            if (game.getId().equals(gameId)) {
                return game;
            }
        }
        return null;
    }
}

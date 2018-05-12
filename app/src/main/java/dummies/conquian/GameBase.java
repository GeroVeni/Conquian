package dummies.conquian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dummies.conquian.database.ConquianBaseHelper;
import dummies.conquian.database.ConquianDbSchema;
import dummies.conquian.database.ConquianDbSchema.GameTable;
import dummies.conquian.database.ConquianDbSchema.PlayerTable;
import dummies.conquian.database.GameCursorWrapper;
import dummies.conquian.database.PlayerCursorWrapper;

public class GameBase {
    private static GameBase sGameBase;

    public static final int MAX_PLAYERS = 3;

    private Context mContext;

    private List<Game> mGames;
    private SQLiteDatabase mDatabase;

    public static GameBase get(Context context) {
        if (sGameBase == null) {
            sGameBase = new GameBase(context);
        }
        return sGameBase;
    }

    private GameBase(Context context) {
        mContext = context.getApplicationContext();

        mDatabase = new ConquianBaseHelper(mContext)
                .getWritableDatabase();
        mGames = null;
    }

    public void saveGames() {
        if (mGames == null) return;

        for (Game game : mGames) {
            updateGame(game);
            for (Player player : game.getPlayers()) updatePlayer(player);
        }
    }

    public void addGame(Game game) {
        ContentValues values = getGameContentValues(game);

        mDatabase.insert(GameTable.NAME, null, values);

        for (Player player : game.getPlayers()) {
            values = getPlayerContentValues(player);

            mDatabase.insert(PlayerTable.NAME, null, values);
        }

        getGames().add(game);
    }

    public void deleteGame(Game game) {
        String uuidString = game.getId().toString();
        mDatabase.delete(GameTable.NAME,
                GameTable.Cols.UUID + " = ?",
                new String[] { uuidString });
        mDatabase.delete(PlayerTable.NAME,
                PlayerTable.Cols.GAME_UUID + " = ?",
                new String[] { uuidString });

        mGames.remove(game);
    }

    public List<Game> getGames() {
        if (mGames == null) {
            List<Game> games = new ArrayList<>();

            GameCursorWrapper gameCursor = queryGames(null, null);

            try {
                gameCursor.moveToFirst();
                while (!gameCursor.isAfterLast()) {
                    games.add(gameCursor.getGame());
                    gameCursor.moveToNext();
                }
            } finally {
                gameCursor.close();
            }

            mGames = games;

            PlayerCursorWrapper playerCursor = queryPlayer(null, null);

            try {
                playerCursor.moveToFirst();
                while(!playerCursor.isAfterLast()) {
                    Player player = playerCursor.getPlayer();
                    getGame(player.getGameUUID()).addPlayer(player);
                    playerCursor.moveToNext();
                }
            } finally {
                playerCursor.close();
            }
        }

        return mGames;
    }

    public Game getGame(UUID gameId) {
        for (Game game : getGames()) {
            if (game.getId().equals(gameId)) {
                return game;
            }
        }
        return null;

//        GameCursorWrapper cursor = queryGames(
//                GameTable.Cols.UUID + " = ?",
//                new String[] { gameId.toString() }
//        );
//
//        try {
//            if (cursor.getCount() == 0) {
//                return null;
//            }
//
//            cursor.moveToFirst();
//            return cursor.getGame();
//        } finally {
//            cursor.close();
//        }
    }

    private void updateGame(Game game) {
        String uuidString = game.getId().toString();
        ContentValues values = getGameContentValues(game);

        mDatabase.update(GameTable.NAME, values,
                GameTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private void updatePlayer(Player player) {
        String gameUUIDString = player.getGameUUID().toString();
        String name = player.getName();
        ContentValues values = getPlayerContentValues(player);

        mDatabase.update(PlayerTable.NAME, values,
                PlayerTable.Cols.GAME_UUID + " = ? AND " +
                        PlayerTable.Cols.NAME + " = ?",
                new String[] { gameUUIDString, name });
    }

    private static ContentValues getGameContentValues(Game game) {
        ContentValues values = new ContentValues();
        values.put(GameTable.Cols.UUID, game.getId().toString());
        values.put(GameTable.Cols.NAME, game.getName());
        values.put(GameTable.Cols.DATE, game.getDate().getTime());

        return values;
    }

    private static ContentValues getPlayerContentValues(Player player) {
        ContentValues values = new ContentValues();
        values.put(PlayerTable.Cols.GAME_UUID, player.getGameUUID().toString());
        values.put(PlayerTable.Cols.NAME, player.getName());
        values.put(PlayerTable.Cols.SCORE, player.getScore());
        values.put(PlayerTable.Cols.HATS, player.getHats());
        values.put(PlayerTable.Cols.CROWNS, player.getCrowns());
        values.put(PlayerTable.Cols.ACTIVE, player.isActive() ? 1 : 0);

        return values;
    }

    private GameCursorWrapper queryGames(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                GameTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new GameCursorWrapper(cursor);
    }

    private PlayerCursorWrapper queryPlayer(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PlayerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PlayerCursorWrapper(cursor);
    }
}

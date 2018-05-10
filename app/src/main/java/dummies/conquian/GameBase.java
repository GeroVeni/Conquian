package dummies.conquian;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dummies.conquian.database.ConquianBaseHelper;
import dummies.conquian.database.ConquianDbSchema;
import dummies.conquian.database.ConquianDbSchema.GameTable;

public class GameBase {
    private static GameBase sGameBase;

    public static final int MAX_PLAYERS = 3;

    private Context mContext;

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
    }

    public void addGame(Game game) {
        ContentValues values = getContentValues(game);

        mDatabase.insert(GameTable.NAME, null, values);
    }

    public List<Game> getGames() {
        return new ArrayList<>();
    }

    public Game getGame(UUID gameId) {
        return null;
    }

    public void updateGame(Game game) {
        String uuidString = game.getId().toString();
        ContentValues values = getContentValues(game);

        mDatabase.update(GameTable.NAME, values,
                GameTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Game game) {
        ContentValues values = new ContentValues();
        values.put(GameTable.Cols.UUID, game.getId().toString());
        values.put(GameTable.Cols.NAME, game.getName());
        values.put(GameTable.Cols.DATE, game.getDate().getTime());

        return values;
    }
}

package dummies.conquian.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import dummies.conquian.Game;
import dummies.conquian.database.ConquianDbSchema.GameTable;

public class GameCursorWrapper extends CursorWrapper {
    public GameCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Game getGame() {
        String uuidString = getString(getColumnIndex(GameTable.Cols.UUID));
        String name = getString(getColumnIndex(GameTable.Cols.NAME));
        long date = getLong(getColumnIndex(GameTable.Cols.DATE));

        UUID id = UUID.fromString(uuidString);

        return new Game(id, name, new Date(date), 0);
    }
}

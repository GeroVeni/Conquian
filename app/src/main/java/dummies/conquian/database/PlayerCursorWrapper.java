package dummies.conquian.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import dummies.conquian.Player;
import dummies.conquian.database.ConquianDbSchema.PlayerTable;

public class PlayerCursorWrapper extends CursorWrapper {
    public PlayerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Player getPlayer() {
        String gameUUIDString = getString(getColumnIndex(PlayerTable.Cols.GAME_UUID));
        UUID gameUUID = UUID.fromString(gameUUIDString);
        String name = getString(getColumnIndex(PlayerTable.Cols.NAME));
        int score = getInt(getColumnIndex(PlayerTable.Cols.SCORE));
        int hats = getInt(getColumnIndex(PlayerTable.Cols.HATS));
        int crowns = getInt(getColumnIndex(PlayerTable.Cols.CROWNS));
        boolean isActive = getInt(getColumnIndex(PlayerTable.Cols.ACTIVE)) != 0;

        Player player = new Player(gameUUID, name);
        player.setScore(score);
        player.setHats(hats);
        player.setCrowns(crowns);
        player.setActive(isActive);

        return player;
    }
}

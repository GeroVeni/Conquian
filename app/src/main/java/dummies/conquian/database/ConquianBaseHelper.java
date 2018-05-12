package dummies.conquian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dummies.conquian.database.ConquianDbSchema.GameTable;
import dummies.conquian.database.ConquianDbSchema.PlayerTable;

public class ConquianBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "conquianBase.db";

    public ConquianBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + GameTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GameTable.Cols.UUID + " text, " +
                GameTable.Cols.NAME + " text, " +
                GameTable.Cols.DATE + " integer" +
                ")"
        );

        sqLiteDatabase.execSQL("create table " + PlayerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PlayerTable.Cols.GAME_UUID + " text, " +
                PlayerTable.Cols.NAME + " text, " +
                PlayerTable.Cols.SCORE + " integer, " +
                PlayerTable.Cols.HATS + " integer, " +
                PlayerTable.Cols.CROWNS + " integer, " +
                PlayerTable.Cols.ACTIVE + " integer" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

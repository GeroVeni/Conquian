package dummies.conquian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dummies.conquian.database.ConquianDbSchema.GameTable;

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
                GameTable.Cols.UUID + ", " +
                GameTable.Cols.NAME + ", " +
                GameTable.Cols.DATE +
                ")"
        );

        // TODO: 11/5/2018 Create player table
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

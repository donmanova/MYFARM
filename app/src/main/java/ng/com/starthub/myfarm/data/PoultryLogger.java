package ng.com.starthub.myfarm.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Gideon on 2/16/2015.
 */
public class PoultryLogger {

    public static final String TABLE_LOG = "logTable";    //formerly location table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOG = "log";



    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_LOG
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " REAL NOT NULL, "
            + COLUMN_LOG + " TEXT NOT NULL "


            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(PoultryLogger.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        onCreate(database);
    }

}
